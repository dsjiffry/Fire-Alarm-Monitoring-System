let express = require("express");
let encode = require('hashcode').hashCode;
let app = express();
let mongoose = require('mongoose/');

app.use(express.json());

//MongoDB url
let url = "mongodb://localhost:27017/Fire_Alarm_Authentication";

//Defining a Schema
let userSchema = mongoose.Schema({
  username: String,
  password: String,
  email: String,
  phoneNumber: String
});

//compiling schema to model
let userModel = mongoose.model('User', userSchema, 'Users');

/**
 * Login Function
 * POST to http://localhost:8080/login
 * With JSON Keys : "username", "password"
 */
app.post('/login', (req, res) => {
  let username = req.body.username;
  //Hashcoding password before checking with DB
  let password = encode().value(req.body.password);

  if (!(username) || password == 0) {
    return res.status(404).send('Error in JSON body');
  }

  connectToDB();

  userModel.findOne({ username: username, password: password }, (err, user) => {
    if (err) {
      console.log(err);
    }

    //If a record is found
    if (user) {
      return res.status(200).send('Valid Login');
    }

    //If no record found
    if (!user) {
      console.log('Incorrect Login Details');
      res.status(404).send('Incorrect Login Details');
    }
  });


});

/**
 * Register New User Function
 * POST to http://localhost:8080/register
 * With JSON Keys : "username", "password", "email", "phoneNumber"
 */
app.post('/register', (req, res) => {

  let username = req.body.username;
  let email = req.body.email;
  let phone = req.body.phoneNumber;

  //Hashcoding password for security before storing 
  let password = encode().value(req.body.password);

  if (!(username) || password == 0 || !(email) || !(phone)) {
    return res.status(404).send('Error in JSON body');
  }

  connectToDB();

  userModel.findOne({ username: username }, (err, user) => {
    if (err) {
      console.log(err);
    }

    if (user) {
      console.log('Username is taken');
      res.status(404).send('Username is taken');
    }

    if (!user) {
      //reference to DB
      let DB = mongoose.connection;

      //Creating a Document
      let userDoc = new userModel({
        username: username,
        password: password,
        email: email,
        phoneNumber: phone
      });

      //Saving to DB
      userDoc.save(function (err, user) {
        if (err) {
          return console.error(err);
        }
        console.log(user.username + " added to DB");

      });

      //Sending client to login page
      return res.status(200).send(username + ' added to Database');
    }
  });




});

/**
  * Get all emails
  * POST to http://localhost:8080/getEmails
  */
app.post('/getEmails', (req, res) => {

  connectToDB();

  userModel.find({}, '-_id email', function (err, user) {
    if (err) {
      console.log(err);
    }

    let emails = [];

    user.forEach(element => {
      emails.push(element.email);
    });

    return res.status(200).send(emails);

  });


});


/**
  * Get all phone numbers
  * POST to http://localhost:8080/getPhoneNumbers
  */
app.post('/getPhoneNumbers', (req, res) => {

  connectToDB();

  userModel.find({}, '-_id phoneNumber', function (err, user) {
    if (err) {
      console.log(err);
    }

    let numbers = [];

    user.forEach(element => {
      numbers.push(element.phoneNumber);
    });

    return res.status(200).send(numbers);

  });








  
});



/**
 * Connecting to the Database
 */
function connectToDB() {
  mongoose.connect(url)
    .then(function () {
      console.log('Connected to MongoDB');
    })
    .catch(function (err) {
      console.log('Error in Connecting to MongoDB');
      return;
    });
}





//Keep server running on port
const port = 8080;
app.listen(port, () => {
  console.log(`Server running on port:${port}`);
});

