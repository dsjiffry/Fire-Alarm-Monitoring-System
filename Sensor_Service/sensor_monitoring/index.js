const url = "http://localhost:5000/api/getAllSensors"

const fetch = require('node-fetch');
const {Kafka} = require("kafkajs");


const kafka = new Kafka({
    clientId: 'sensor-monitor',
    brokers: ['localhost:9092']
  })



const getAllSensors = async () => 
{
    let sensorArray = await fetch(url);
    sensorArray = await sensorArray.json();
    //console.log(sensorArray);
    return sensorArray;
}



getAllSensors().then(

    (data) => { 

        let topics = [];

        data.forEach(element => {

                //console.log(element.sensorUID, element.username)
                kafkaListners({topic:element.sensorUID, fromBeginning: false} , element.username  , element.floor , element.room , element.sensorType)
              
        });
       
    }
)





async function kafkaListners(topic_array,username,floorNum, roomNum , type) 
{
    console.log("Called KafkaListner Method")

    const url2 = "http://localhost:8081/emailAlertToUser";

    const consumer = kafka.consumer({ groupId: `${topic_array.topic}-alert-monitor` })
    await consumer.connect();

    await consumer.subscribe(topic_array)

    await consumer.run({
        eachMessage: async ({ topic, partition, message }) => {
            
            //Caturing the sensor reading and timeStamp of each senor emmited
            let reading = Number(JSON.parse(message.value.toString()).reading);
            let timeStamp =  new Date(JSON.parse(message.value.toString()).timeStamp);

            //Updating the main senosor server reading of corresponding sensor reading
            updateSensorReading(topic , reading)

            if( reading > 5  )
            {
                    let msg = `${username} Your ${type} sensor ${topic} on floor ${floorNum} room ${roomNum} has shown a reading of ${JSON.parse(message.value.toString()).reading} on ${timeStamp.toDateString()} ${timeStamp.toTimeString()}`;
                    //console.log(JSON.stringify({"username":username , "message": msg}) , url2);
                    fetch(url2, { method:"POST" , body: JSON.stringify({"username": `${username}` , "message": `${msg}`}) , 
                    				headers: {      									
      									'Content-Type': 'application/JSON'
    },} ).then( res => console.log(res.status))
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



