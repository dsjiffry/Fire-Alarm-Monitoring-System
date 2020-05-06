const fetch = require('node-fetch');
const {Kafka} = require("kafkajs");

//This service listens to the kafka stream and update the sensor values in the sensor API
//Also if the value is above 5 the sensor calls yhe alert api to send email to the user


//connecting to kafka server
const kafka = new Kafka({
    clientId: 'sensor-monitor',
    brokers: ['localhost:9092']
  })


//url to sensor api to get all sensors
const url = "http://localhost:5000/api/getAllSensors"

//Methood to fetch All sensors
const getAllSensors = async () => 
{
    let sensorArray = await fetch(url);
    sensorArray = await sensorArray.json();
    return sensorArray;
}

//For each sensor start listening to their respective topics using kafkaListnerMethd
getAllSensors().then(

    (data) => { 

        let topics = [];

        data.forEach(element => {
                kafkaListners({topic:element.sensorUID, fromBeginning: false} , element.username  , element.floor , element.room , element.sensorType)           
        });
       
    }
)



//Handles reading through each message asynchronusly
//Reads the new value 
//Updates the new value in sensor API
async function kafkaListners(topic_array,username,floorNum, roomNum , type) 
{
    console.log("Called KafkaListner Method")

    //url to Alert to send an email when required
    const url2 = "http://localhost:8081/emailAlertToUser";

    //Starts listenning to a channel
    const consumer = kafka.consumer({ groupId: `${topic_array.topic}-alert-monitor` })
    await consumer.connect();

    await consumer.subscribe(topic_array)

    //This method runs every time a new senor value is added
    await consumer.run({
        eachMessage: async ({ topic, partition, message }) => {
            
            //Caturing the sensor reading and timeStamp of each senor emmited
            let reading = Number(JSON.parse(message.value.toString()).reading);
            let timeStamp =  new Date(JSON.parse(message.value.toString()).timeStamp);

            //Updating the main senosor server reading of corresponding sensor in the sensor API
            updateSensorReading(topic , reading)

            if( reading > 5  )
            {
                    //Compoing the email body
                    let msg = `${username} Your ${type} sensor ${topic} on floor ${floorNum} room ${roomNum} has shown a reading of ${JSON.parse(message.value.toString()).reading} on ${timeStamp.toDateString()} ${timeStamp.toTimeString()}`;
                    
                    //Calling the alert API to send a message
                    fetch( 
                        url2, 
                        { 
                            method:"POST" , 
                            body: JSON.stringify({"username": `${username}` , "message": `${msg}`}) , 
                    		headers: {      									
      							'Content-Type': 'application/JSON'
                             },
                        } 
                    )
                    .then( res => console.log(res.status))
            }
        },
      })
}

    //Updating sensor reading field in the main sensor server 
    function updateSensorReading(sensorUID, reading)
    {
        fetch(`http://localhost:5000/api/updateSensorReading/${sensorUID}`, {
            method: 'put',
            body:    JSON.stringify({"reading" : reading}),
            headers: { 'Content-Type': 'application/json' },
        })
        .then(res => console.log(res))
       
    }



