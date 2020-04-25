const express = require('express');
const mongoose = require("mongoose");
const {Kafka} = require("kafkajs")

const Sensor = require('./Sensor')

const routes = require("./routes") 
const bodyParser = require("body-parser")

mongoose.set('useFindAndModify', false);


//const router = express.Router();

async function startSensorChannels () 
{
    try {

        let sensors = await Sensor.find();
        console.log(sensors)
    

        const kafka = new Kafka({
            "clientId": "node-sensor-api",
            "brokers" :["localhost:9092"]
       })

        const admin = kafka.admin();
        console.log("Connecting.....")
        await admin.connect()
        console.log("Connected!")

        let topicContetnt = [];

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

        //A-M, N-Z
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

mongoose
  .connect("mongodb://localhost:27017/acmedb", 
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
    
    startSensorChannels();

    app.listen(5000, () => {
      console.log("Server has started!")
    })
    
})
