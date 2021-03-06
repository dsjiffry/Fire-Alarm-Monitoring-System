const fetch = require('node-fetch');
const { Kafka } = require("kafkajs");

//connecting to a kafka instance
const kafka = new Kafka({
    clientId: 'sensor-monitor',
    brokers: ['localhost:9092']
})


//Endpoint to the Sensor API to get all sensors 
const url = "http://localhost:5000/api/getAllSensors"

//Method to fetch all sensor date 
const getAllSensors = async () => {
    let sensorArray = await fetch(url);
    sensorArray = await sensorArray.json();
    console.log("Getting sensor metadata....");
    return sensorArray;
}

console.log("Initializing ......")

//Calling async method
getAllSensors().then(

    (data) => {

        //This parameter controls how frequent the service checks the sensors
        let refetch_time_frame = 10 ; //in seconds
        
        //This paramters controls the initial delay
        let initial_delay = 10 ; //in seconds

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

                // Check if they have been updated for atleast n Seconds If Not Set status as offline

                sensor_table.forEach(element => {
                    
                    let currentTime = new Date();

                    let status_checking_time_frame = 30 //(secs) the minimum gap between now and last reading for a status update online or offline 

                    //settting online or off line status depending on the gap between now and last reading
                    if( ( Math.floor( ( currentTime - element.lastReading ) / 1000) ) >= status_checking_time_frame)
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


            }, refetch_time_frame * 1000);

        }, initial_delay * 1000)

       
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

        //This method runs everytime a message is sent to the kafka topic which is the sensorUID from a client

        await consumer.run({
            eachMessage: async ({ topic, partition, message }) => {

                //console.log(topic, JSON.parse(message.value) ) 
                sensor_table[index] = {sensorUID: topic , lastReading : new Date(JSON.parse(message.value).timeStamp) }
               
                
            },
        })
        
    }

