let express = require("express");
let encode = require('hashcode').hashCode;
let app = express();
let mongoose = require('mongoose/');

//MongoDB url
let url = "mongodb://localhost:27017/Fire_Alarm_Authentication";

//Defining a Schema
let userSchema = mongoose.Schema({
  username: String,
  password: String
});

//compiling schema to model
let userModel = mongoose.model('User', userSchema, 'Users');

/**
 * Connecting to the Database
 */
function connectToDB() {
    mongoose.connect(url)
    .then(function ()
    {
      console.log('Connected to MongoDB');
    })
    .catch(function (err)
    {
      console.log('Error in Connecting to MongoDB');
      return;
    });  
}


app.use(express.json());

/**
 * Login Function
 * POST to http://localhost:8080/login
 * With JSON Keys : "username", "password"
 */
app.post('/login', (req, res) =>
{
  let username = req.body.username;
  console.log(username);
  //Hashcoding password before checking with DB
  let password = encode().value(req.body.password);

  if ( !(username) || password == 0)
  {
    return res.status(404).send('Error in JSON body');
  }
 
  connectToDB();

  userModel.findOne({ username: username, password: password }, (err, user) =>
  {
    if(err)
    {
      console.log(err);
    }

    //If a record is found
    if(user)
    {
      return res.status(200).send('Valid Login');
    }

    //If no record found
    if(!user)
    {
      console.log('Incorrect Login Details');
      res.status(404).send('Incorrect Login Details');
    }
  });


});

/**
 * Register New User Function
 * POST to http://localhost:8080/register
 * With JSON Keys : "username", "password"
 */
app.post('/register', (req, res) =>
{
  
  console.log(req.body);

  let username = req.body.username;
  //Hashcoding password for security before storing 
  let password = encode().value(req.body.password);

  if ( !(username) || password == 0)
  {
    return res.status(404).send('Error in JSON body');
  }

  connectToDB();

  userModel.findOne({ username: username }, (err, user) =>
  {
    if(err)
    {
      console.log(err);
    }

    if (user)
    {
      console.log('Username is taken');
      res.status(404).send('Username is taken');
    }
    
    if(!user)
    {
      //reference to DB
      let DB = mongoose.connection;

      //Creating a Document
      let userDoc = new userModel({
        username: username,
        password: password
      });

      //Saving to DB
      userDoc.save(function (err, user) 
      {
        if (err)
        {
          return console.error(err);
        }
        console.log(user.username + " added to DB");

      });

      //Sending client to login page
      return res.status(200).send(username+' added to Database');
    }
  });




});




//Keep server running on port
const port = 8080;
app.listen(port, () =>
{
  console.log(`Server running on port${port}`);
});

