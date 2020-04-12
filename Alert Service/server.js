let express = require("express");
let app = express();
let nodemailer = require('nodemailer'); 

app.use(express.json());


let emailAddresses = ["bar@example.com", "baz@example.com"];  //Email will be sent to these addresses
let smsNumbers = ["0771234567"];  //sms will be sent to these numbers

/**
 * Send email Alert
 * POST to http://localhost:8080/emailAlert
 * JSON keys: "message"
 */
app.post('/emailAlert', async(req, res) =>
{
  let message = req.body.message;

  if (!(message))
  {
    return res.status(404).send('Error in JSON body');
  }

  let testAccount = await nodemailer.createTestAccount();

  let transporter = nodemailer.createTransport({
    host: "smtp.ethereal.email",
    port: 587,
    secure: false, // true for 465, false for other ports
    auth: {
      user: testAccount.user, // generated ethereal user
      pass: testAccount.pass // generated ethereal password
    }
  });

  let info = await transporter.sendMail({
    from: '"Fire Alarm Service 👻" <Alert@FireAlarm.com>', // sender address
    to: emailAddresses, // list of receivers
    subject: "Fire Alarm Service Alert", // Subject line
    text: message, // plain text body
  });

  console.log("Email sent");
  return res.status(200).send(nodemailer.getTestMessageUrl(info));
});

/**
 * Send SMS Alert
 * POST to http://localhost:8080/smsAlert
 * JSON keys: "message"
 */
app.post('/smsAlert', async(req, res) =>
{
  let message = req.body.message;
  console.log(message);

  if (!(message))
  {
    return res.status(404).send('Error in JSON body');
  }

  console.log("Sms sent");
  return res.status(200).send("Sms sent: "+message);

});




//Keep server running on port
const port = 8080;
app.listen(port, () =>
{
  console.log(`Server running on port${port}`);
});

