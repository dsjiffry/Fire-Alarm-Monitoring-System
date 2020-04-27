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
  phoneNumber: String,
  type: String  // either "user" or "admin"
});

//compiling schema to model
let userModel = mongoose.model('User', userSchema, 'Users');

/**
 * Login Function for users
 * POST to http://localhost:8080/loginUser
 * With JSON Keys : "username", "password"
 */
app.post('/loginUser', (req, res) => {
  let username = req.body.username;
  //Hashcoding password before checking with DB
  let password = encode().value(req.body.password);

  if (!(username) || password == 0) {
    return res.status(404).send('Error in JSON body');
  }

  connectToDB();

  userModel.findOne({ username: username, password: password, type: "user" }, (err, user) => {
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
 * Login Function for admins
 * POST to http://localhost:8080/loginAdmin
 * With JSON Keys : "username", "password"
 */
app.post('/loginAdmin', (req, res) => {
  let username = req.body.username;
  //Hashcoding password before checking with DB
  let password = encode().value(req.body.password);

  if (!(username) || password == 0) {
    return res.status(404).send('Error in JSON body');
  }

  connectToDB();

  userModel.findOne({ username: username, password: password, type: "admin" }, (err, user) => {
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
 * With JSON Keys : "username", "password", "email", "phoneNumber", "type"
 */
app.post('/register', (req, res) => {

  let username = req.body.username;
  let email = req.body.email;
  let phone = req.body.phoneNumber;
  let type = req.body.type; // either "user" or "admin"

  //Hashcoding password for security before storing 
  let password = encode().value(req.body.password);

  if (!(username) || password == 0 || !(email) || !(phone) || !(type)) {
    return res.status(404).send('Error in JSON body');
  }

  if (type != "admin" && type != "user") {
    return res.status(406).send('incorrect type in JSON body. must be either "admin" or "user"');
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
        phoneNumber: phone,
        type: type
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
  * Get email of user
  * POST to http://localhost:8080/getEmails
  * with optional JSON keys: "username"
  */
app.post('/getEmails', (req, res) => {
  
  let username = req.body.username;
  connectToDB();

  userModel.find({}, '-_id username email', function (err, user) {
    if (err) {
      console.log(err);
    }

    let emails = [];

    if (!(username)) {
      user.forEach(element => {
        emails.push(element.email);
      });
    }
    else {
      user.forEach(element => {
        if (element.username == username) {
          emails.push(element.email);
        }
      });
    }

    return res.status(200).send(emails);

  });


});


/**
  * Get phoneNumber of user
  * POST to http://localhost:8080/getPhoneNumbers
  * with optional JSON keys: "username"
  */
app.post('/getPhoneNumbers', (req, res) => {

  let username = req.body.username;
  connectToDB();

  userModel.find({}, '-_id username phoneNumber', function (err, user) {
    if (err) {
      console.log(err);
    }

    let numbers = [];

    if (!(username)) {
      user.forEach(element => {
        numbers.push(element.phoneNumber);
      });
    }
    else {
      user.forEach(element => {
        if (element.username == username) {
          numbers.push(element.phoneNumber);
        }
      });
    }

    return res.status(200).send(numbers);

  });









});


app.post('/checkAuthenticationAlive', (req, res) => {
  return res.status(200).send();
});

/**
 * Connecting to the Database
 */
function connectToDB() {
  mongoose.connect(url,
  	{ 
      useNewUrlParser: true ,
      "auth": {"authSource":"admin"} ,
      "user": "root",
      "pass": "rootpassword"
    }
  )
    .then(function () {
      console.log('Connected to MongoDB');
    })
    .catch(function (err) {
      console.log('Error in Connecting to MongoDB');
      return;
    });
}

// Keep server running on port
const port = 8080;
app.listen(port, () => {
  console.log(`Server running on port:${port}`);
});

