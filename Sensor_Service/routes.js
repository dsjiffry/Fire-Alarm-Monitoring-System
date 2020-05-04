const express = require("express")

const { Kafka } = require("kafkajs")


const Sensor = require('./Sensor')

const router = express.Router()


async function addSensorChannel(sensorUID) {
    const kafka = new Kafka({
        "clientId": "node-sensor-api",
        "brokers": ["localhost:9092"]
    })

    const admin = kafka.admin();
    console.log("Connecting.....")
    await admin.connect()
    console.log("Connected!")

    await admin.createTopics({
        "topics": [{
            "topic": sensorUID,
            "numPartitions": 1
        }]
    })

    console.log("Created Successfully!")
    await admin.disconnect();

}

router
    .get("/getSensorReading/:sensorUID",
        async (req, res) => {
            let sensorUID = req.params.sensorUID;

            let reading = 0

            Sensor.exists({ sensorUID: sensorUID },
                async function (err, result) {

                    // console.log(err, "error")
                    // console.log(result, "result")

                    reading = "not found";

                    if (result === false) {
                        res.status(404)
                        res.send({reading:reading});
                    }
                    else {

                        const doc = await Sensor.findOne({ sensorUID: sensorUID });
                        res.status(200);
                        // reading = doc.reading;
                        res.send({reading:doc.reading});
                    }
                })

            

        }
    )


router
    .put("/updateSensorReading/:sensorUID",
        async (req, res) => {

            let sensorUID = req.params.sensorUID;
            let reading = req.body.reading;

            

            Sensor.exists({ sensorUID: sensorUID }, async function (err, result) {

               //console.log(`sensor exist ${err === false} result ${result === true}`)

                if (result === false) {
                    res.send({});
                    res.status(404)
                }
                else {
                    const doc = await Sensor.findOne({ sensorUID: sensorUID });
                    
                    //console.log(reading)
                    
                    doc.reading = parseInt(reading)

                    await doc.save();

                    const sensor = await Sensor.findOne({ sensorUID: req.params.sensorUID });
                    
                    res.send(sensor);
                    res.status(200);
                   
                    //res.send(sensor);
                }
            })
             
            
        }
    )

router
    .put("/updateSensorStatus/:sensorUID",
        async (req, res) => {

            let sensorUID = req.params.sensorUID;
            let status = req.query.status;

            Sensor.exists({ sensorUID: sensorUID }, async function (err, result) {

                // console.log(err, "error")
                // console.log(result, "result")

                if (result === false) {
                    res.status(404)

                }
                else {
                    // let doc = await Sensor.findOneAndUpdate({ sensorUID: req.body.sensorUID }, sensor)
                    const doc = await Sensor.findOne({ sensorUID: sensorUID });
                    doc.status = status

                    await doc.save();

                    res.status(200);
                    //res.send(sensor);
                }

            })

            const sensor = await Sensor.findOne({ sensorUID: req.body.sensorUID });
            res.send(sensor);

        }
    )








router.delete("/deleteSensor", async (req, res) => {
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

router.get("/getAllSensors", async (req, res) => {

    let sensors = await Sensor.find();

    res.send(sensors);

})

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

        // console.log(err, "error")
        // console.log(result, "result")

        if (result === false) {
            res.status(404)
        }
        else {
            // let doc = await Sensor.findOneAndUpdate({ sensorUID: req.body.sensorUID }, sensor)
            const doc = await Sensor.findOne({ sensorUID: req.body.sensorUID });
            doc.floor = req.body.floor;
            doc.room = req.body.room;
            doc.sensorType = req.body.sensorType

            await doc.save();
            res.status(200);
            //res.send(sensor);

        }

        res.send(sensor);


    })



})

router.post("/registerSensor", async (req, res) => {

    const sensor = new Sensor(
        {
            username: req.body.username,
            sensorUID: req.body.sensorUID,
            floor: req.body.floor,
            room: req.body.room,
            sensorType: req.body.sensorType,
            status: "online",
            reading: "Unread Sensor"
        })

    Sensor.exists({ sensorUID: req.body.sensorUID }, async function (err, result) {


        if (result === false) {
            await sensor.save()
            addSensorChannel(req.body.sensorUID);
            res.status(201)
        }
        else {
            res.status(409)
        }

        res.send(sensor);

    })


})




module.exports = router