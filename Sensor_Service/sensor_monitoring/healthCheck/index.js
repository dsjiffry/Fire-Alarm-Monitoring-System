const url = "http://localhost:5000/api/getAllSensors"

//const url_update_status = "http://localhost:5000/api/"

const fetch = require('node-fetch');
const { Kafka } = require("kafkajs");

const kafka = new Kafka({
    clientId: 'sensor-monitor',
    brokers: ['localhost:9092']
})

const getAllSensors = async () => {
    let sensorArray = await fetch(url);
    sensorArray = await sensorArray.json();
    console.log("Getting sensor metadata....");
    return sensorArray;
}

console.log("Initializing ......")

getAllSensors().then(

    (data) => {

        setTimeout(() => {

            console.log("syncing with kafka streams ......")

            //Creating an array with sensorUID and last_updated , setting last upadated to current Time

            let sensor_table = [];

            data.forEach(element => {
                sensor_table.push({ sensorUID: element.sensorUID, lastReading: new Date() })
            });


            //Running Kafka Conumer to check for new messages, If new message is detected  last_updated would be updated

            //Passing the topic array to the kafka listner method so It starts listenning

            data.forEach(element => {
                
                kafkaListners({"topic": element.sensorUID , "fromBeginning": false} , sensor_table)

            });

            //A delay is introduced to avoid start methods before kafka client has joined the cluster
            setInterval(() => {

                //Every 10 seconds a method should calculated the time between the health checker service started
                //and when the last reading is 
                sensor_table.forEach(element => {
                    let currentTime = new Date();
                    console.log(element.sensorUID, Math.floor( (currentTime - element.lastReading)/1000 ) )
                });

                // Check if they have been updated for atleast 60 Seconds If Not Set status as offline

                sensor_table.forEach(element => {
                    
                    let currentTime = new Date();

                    if( ( Math.floor( ( currentTime - element.lastReading ) / 1000) ) >= 30)
                    {
                        console.log(`sensor ${element.sensorUID} is Offline`)
                        let url = `http://localhost:5000/api/updateSensorStatus/${element.sensorUID}?status=offline`

                        //Calling sensor Api to update the sensor status

                        fetch( url, {method:'put'} )
                            .then( res => console.log(res.status))
                    }
                    else
                    {
                        console.log(`sensor ${element.sensorUID} is Online`)

                        //Calling sensor Api to update the sensor status

                        let url = `http://localhost:5000/api/updateSensorStatus/${element.sensorUID}?status=online`
                        fetch( url, {method:'put'} )
                        .   then( res => console.log(res.status))
                    }

                });


            }, 10000);

        }, 10000)

       
    }
)

    async function kafkaListners(topic_array , sensor_table) {
        //console.log("Called KafkaListner Method")

        let val = topic_array.topic;

        var index = sensor_table.findIndex(function (item, i) {
            return item.sensorUID === val
        })

        console.log("Sensor Position " , index)

        const consumer = kafka.consumer({ groupId: `${topic_array.topic}-alert-health-check` })
        await consumer.connect();

        await consumer.subscribe(topic_array)

        await consumer.run({
            eachMessage: async ({ topic, partition, message }) => {

                //console.log(topic, JSON.parse(message.value) ) 
                sensor_table[index] = {sensorUID: topic , lastReading : new Date(JSON.parse(message.value).timeStamp) }

            },
        })
        
    }