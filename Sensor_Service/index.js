const express = require('express');
const mongoose = require("mongoose");
const {Kafka} = require("kafkajs")

const Sensor = require('./Sensor')

const routes = require("./routes") 
const bodyParser = require("body-parser")

mongoose.set('useFindAndModify', false);


//On the start of the server this method runs
//It grabs all the sensors from the Database
//Then it will start a kafka topic(channel) for each unique sensor
//This is to make sure that even if the kafka server is restarted or lost 
//the new instance will contain channels for each sensor
async function startSensorChannels () 
{
    try {

        //Getting all sensors
        let sensors = await Sensor.find();
        console.log(sensors)
    
        //Connecting to Kafka 
        const kafka = new Kafka({
            "clientId": "node-sensor-api",
            "brokers" :["localhost:9092"]
       })

        const admin = kafka.admin();
        console.log("Connecting.....")
        await admin.connect()
        console.log("Connected!")

        let topicContetnt = [];

        //creating a kafka topic list to send as parameter for create.topic method
        sensors.forEach(
            (sensor) => 
            {
                topicContetnt.push(
                    {
                        "topic" : sensor.sensorUID,
                        "numPartitions" : 1
                    }
                )
            }
        )

        //Creating topics in kafka cluster
        await admin.createTopics({
            "topics": topicContetnt
        })

        console.log("Created Successfully!")
        await admin.disconnect();
        
    } 
    catch (error) 
    {
        
        console.error(`Something bad happened ${error}`)
    
    }
   
    return 0;
}


const port = process.env.PORT || 5001;

//Connecting to mongodb instance 
mongoose
  .connect("mongodb://localhost:28017/acmedb", 
  { 
      useNewUrlParser: true ,
      "auth": {"authSource":"admin"} ,
      "user": "root",
      "pass": "root"
    },
     
      )
    .then(() => {
    
    const app = express()
    app.use(bodyParser.json()) 
    app.use("/api", routes)
    
    //Starting the sensor Channels
    startSensorChannels();
    
    //Starting the server
    app.listen(5000, () => {
      console.log("Server has started!")
    })
    
})
