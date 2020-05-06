const express = require("express")
const { Kafka } = require("kafkajs")

//Importing Mongoose Data Model
const Sensor = require('./Sensor')

const router = express.Router()

// Method Used To register A Kafka Topic for each unique sensor
async function addSensorChannel(sensorUID) {

    //Creating A kafka connection to the cluster 
    const kafka = new Kafka({
        "clientId": "node-sensor-api",
        "brokers": ["localhost:9092"]
    })

    const admin = kafka.admin();
    console.log("Connecting.....")
    await admin.connect()
    console.log("Connected!")

    //Creating a topic (Channel) for given sensor 
    await admin.createTopics({
        "topics": [{
            "topic": sensorUID,
            "numPartitions": 1
        }]
    })

    console.log("Created Successfully!")
    await admin.disconnect();

}

// GET endpoint that returns the sensor reading for specific user
router
    .get("/getSensorReading/:sensorUID",
        async (req, res) => {
            //Getting the path parameter
            let sensorUID = req.params.sensorUID;
            let reading = 0

            //Check if sensor Exist in DB If true returns reading else returns 404 error
            Sensor.exists({ sensorUID: sensorUID },
                async function (err, result) {

                    reading = "not found";

                    if (result === false) {
                        
                        res.status(404)
                        res.send({reading:reading});
                    
                    }
                    else {

                        const doc = await Sensor.findOne({ sensorUID: sensorUID });
                        
                        res.status(200);
                        res.send({reading:doc.reading});
                    }
                })

            

        }
    )

//PUT endpoint to modify sensor readuing
router
    .put("/updateSensorReading/:sensorUID",
        async (req, res) => {

              //Getting the path parameter 
            let sensorUID = req.params.sensorUID;
            //Get query parameter from JSON body
            let reading = req.body.reading;

             //Check if sensor Exist in DB If true updates reading else returns 404 error
            Sensor.exists({ sensorUID: sensorUID }, async function (err, result) {

                if (result === false) {
                    res.send({});
                    res.status(404)
                }
                else {
                    //Find and Update sensor
                    const doc = await Sensor.findOne({ sensorUID: sensorUID });
                                
                    doc.reading = parseInt(reading)
                    await doc.save();

                    const sensor = await Sensor.findOne({ sensorUID: req.params.sensorUID });
                    
                    res.send(sensor);
                    res.status(200);
                  
                }
            })
        }
    )

//PUT Route to modify sensor status to online or offline by sensor UID
router
    .put("/updateSensorStatus/:sensorUID",
        async (req, res) => {

            //Get sensor UID from path
            let sensorUID = req.params.sensorUID;
            //Get status from JSON body
            let status = req.query.status;

            //If exist find and update sensor
            Sensor.exists({ sensorUID: sensorUID }, 
                async function (err, result) {

                        if (result === false) {
                            res.status(404)

                        }
                        else {

                            const doc = await Sensor.findOne({ sensorUID: sensorUID });
                            
                            doc.status = status
                            await doc.save();

                            res.status(200);
                            
                        }

                    })

            const sensor = await Sensor.findOne({ sensorUID: req.body.sensorUID });
            res.send(sensor);

        }
    )


//DELETE route for delerting a specific sensor by sensorUID
router
    .delete("/deleteSensor", async (req, res) => {
        
        let sensorUID = req.query.sensorUID;

        Sensor.exists({ sensorUID: sensorUID }, async (err, result) => {
            if (result === true) {
                let sensor = await Sensor.deleteOne({ sensorUID: sensorUID });
                await Sensor.remove(sensor);
                res.status(200).send("Successfully removes !")
            }
            else {
                res.status(404).send("Sensor Not Found !")
            }
        })

    })

//GET route to get all sensors from the DB
router
    .get("/getAllSensors", 
        async (req, res) => {

            let sensors = await Sensor.find();
            res.send(sensors);

        }
)
//GET route to get all sensors from the DB belonging to one user
router.get("/getSensorsByUsername/:username", async (req, res) => {

    var username = req.params.username;

    Sensor.exists({ username: username }, async (err, result) => {
        if (result === true) {
            let sensors = await Sensor.find({ username: username });
            res.status(200);
            res.send(sensors);
        }
        else {
            res.status(404)
            res.send([]);
        }
    })

})


//PUT route to modify Sensor information
router.put("/updateSensor", async (req, res) => {

    const sensor = new Sensor(
        {
            username: req.body.username,
            sensorUID: req.body.sensorUID,
            floor: req.body.floor,
            room: req.body.room,
            sensorType: req.body.sensorType
        })


    Sensor.exists({ sensorUID: req.body.sensorUID }, async function (err, result) {

        if (result === false) {
            res.status(404)
        }
        else {
            const doc = await Sensor.findOne({ sensorUID: req.body.sensorUID });
            doc.floor = req.body.floor;
            doc.room = req.body.room;
            doc.sensorType = req.body.sensorType

            await doc.save();
            res.status(200);

        }
        res.send(sensor);
    })



})

//POST request to register sensor
//For each new sensor a kafka topic is created so clients can push to that topic
router
    .post("/registerSensor", 
        async (req, res) => {

            const sensor = new Sensor(
                {
                    username: req.body.username,
                    sensorUID: req.body.sensorUID,
                    floor: req.body.floor,
                    room: req.body.room,
                    sensorType: req.body.sensorType,
                    status: "online",
                    reading: 0
                })

            //Check if Exist    , if exist return error 409, else save to DB and start kafka chanel
            Sensor.exists({ sensorUID: req.body.sensorUID }, 
                
                async function (err, result) {

                    if (result === false) {
                        //save sensor to DB
                        await sensor.save()
                        //start server
                        addSensorChannel(req.body.sensorUID);
                        res.status(201)
                    }
                    else {
                        res.status(409)
                    }

                    res.send(sensor);

                }
            )
        }
    )




module.exports = router