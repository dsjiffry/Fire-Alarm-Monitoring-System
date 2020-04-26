let express = require("express");
let app = express();
let nodemailer = require('nodemailer');

let fetch = require("node-fetch");
app.use(express.json());

let getEmailsURL = "http://localhost:8080/getEmails";
let getphoneURL = "http://localhost:8080/getPhoneNumbers";

// In case the authentication server is unreachable, the email/sms will be sent to these.
let emailCache = [];
let smsCache = [];

/**
 * Send email Alert
 * POST to http://localhost:8081/emailAlert
 * with JSON keys: "message"
 */
app.post('/emailAlert', async (req, res) => {
  let message = req.body.message; // Body of the Email
  let emailAddresses;

  if (!(message)) {
    return res.status(404).send('Error in JSON body');
  }

  emailAddresses = await fetch(getEmailsURL, {
    method: 'POST',
    headers: {
      'Accept': 'application/JSON',
      'Content-Type': 'application/JSON',
    }
  }).then((response) => {
    if (response.ok) {
      return response.json();
    } else {
      throw new Error('Something went wrong');
    }
  }).catch((err) => {
  });

  if (!(emailAddresses)) {
    emailAddresses = emailCache.slice();
  }
  else
  {
    emailCache = emailAddresses.slice();
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
    from: '"Fire Alarm Service" <Alert@FireAlarm.com>', // sender address
    to: emailAddresses, // list of receivers
    subject: "Fire Alarm Service Alert", // Subject line
    text: message, // plain text body
  });

  console.log("Email sent");
  return res.status(200).send(nodemailer.getTestMessageUrl(info)); //Will return a URL to preview the email that was sent
});

/**
 * Send email Alert to a user
 * POST to http://localhost:8081/emailAlertToUser
 * with JSON keys: "message", "username"
 */
app.post('/emailAlertToUser', async (req, res) => {
  let message = req.body.message; // Body of the Email
  let username = req.body.username;
  let emailAddresses;

  if (!(message) || !(username) ) {
    return res.status(404).send('Error in JSON body');
  }

  emailAddresses = await fetch(getEmailsURL, {
    method: 'POST',
    headers: {
      'Accept': 'application/JSON',
      'Content-Type': 'application/JSON',
    },
    body: JSON.stringify({"username":username})
  }).then((response) => {
    if (response.ok) {
      return response.json();
    } else {
      throw new Error('Something went wrong');
    }
  }).catch((err) => {
  });

  if (!(emailAddresses) && (username)) {
    emailAddresses = emailCache.slice();
  }
  else
  {
    emailCache = emailAddresses.slice();
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
    from: '"Fire Alarm Service" <Alert@FireAlarm.com>', // sender address
    to: emailAddresses, // list of receivers
    subject: "Fire Alarm Service Alert", // Subject line
    text: message, // plain text body
  });

  console.log("Email sent");
  return res.status(200).send(nodemailer.getTestMessageUrl(info)); //Will return a URL to preview the email that was sent
});

/**
 * Send SMS Alert
 * POST to http://localhost:8081/smsAlert
 * JSON keys: "message"
 */
app.post('/smsAlert', async (req, res) => {
  let message = req.body.message;
  let smsNumbers;

  if (!(message)) {
    return res.status(404).send('Error in JSON body');
  }

  smsNumbers = await fetch(getphoneURL, {
    method: 'POST',
    headers: {
      'Accept': 'application/JSON',
      'Content-Type': 'application/JSON',
    }
  }).then((response) => {
    if (response.ok) {
      return response.json();
    } else {
      throw new Error('Something went wrong');
    }
  }).catch((err) => {

  });

  if (!(smsNumbers)) {
    smsNumbers = smsCache.slice();
  }
  else
  {
    smsCache = smsNumbers.slice();
  }

  console.log("Sms sent");
  return res.status(200).send("Sms message: " + message + " sent to: " + smsNumbers);

});

/**
 * Send SMS Alert to user
 * POST to http://localhost:8081/smsAlertToUser
 * JSON keys: "message", "username"
 */
app.post('/smsAlertToUser', async (req, res) => {
  let message = req.body.message;
  let username = req.body.username;
  let smsNumbers;

  if (!(message) || !(username)) {
    return res.status(404).send('Error in JSON body');
  }

  smsNumbers = await fetch(getphoneURL, {
    method: 'POST',
    headers: {
      'Accept': 'application/JSON',
      'Content-Type': 'application/JSON',
    },
    body: JSON.stringify({"username":username})
  }).then((response) => {
    if (response.ok) {
      return response.json();
    } else {
      throw new Error('Something went wrong');
    }
  }).catch((err) => {

  });

  if (!(smsNumbers)) {
    smsNumbers = smsCache.slice();
  }
  else
  {
    smsCache = smsNumbers.slice();
  }

  console.log("Sms sent");
  return res.status(200).send("Sms message: " + message + " sent to: " + smsNumbers);

});

/**
 * Maintaining a cache in case authorization server is down during a fire
 */
async function updateCache() {

  emailCache = await fetch(getEmailsURL, {
    method: 'POST',
    headers: {
      'Accept': 'application/JSON',
      'Content-Type': 'application/JSON',
    }
  }).then((response) => {
    if (response.ok) {
      return response.json();
    } else {
      throw new Error('Something went wrong');
    }
  }).catch((err) => {

  });

  smsCache = await fetch(getphoneURL, {
    method: 'POST',
    headers: {
      'Accept': 'application/JSON',
      'Content-Type': 'application/JSON',
    }
  }).then((response) => {
    if (response.ok) {
      return response.json();
    } else {
      throw new Error('Something went wrong');
    }
  }).catch((err) => {

  });

  if (!(smsCache)) {
    smsCache = ["119"];
  }
  if (!(emailCache)) {
    emailCache = ["damn@weScrewed.com"];
  }

}


//Keep server running on port
const port = 8081;
app.listen(port, () => {
  updateCache();
  console.log(`Server running on port:${port}`);
});

