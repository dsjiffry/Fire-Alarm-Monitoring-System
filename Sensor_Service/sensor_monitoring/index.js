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

                console.log(element.sensorUID, element.username)
                kafkaListners({topic:element.sensorUID, fromBeginning: false} , element.username )
              
        });
       
    }
)



async function kafkaListners(topic_array,username) 
{
    console.log("Called KafkaListner Method")

    const consumer = kafka.consumer({ groupId: `${topic_array.topic}-alert-monitor` })
    await consumer.connect();

    await consumer.subscribe(topic_array)

    await consumer.run({
        eachMessage: async ({ topic, partition, message }) => {
            // console.log("************\n")
            //     console.log({
            //         topic,
            //         offset: message.offset,
            //         value: message.value.toString(),
            //     })
            // console.log("************\n")
            //console.log(JSON.parse(message.value.toString()).reading, topic , username)

            let reading = Number(JSON.parse(message.value.toString()).reading);
            let timeStamp =  new Date(JSON.parse(message.value.toString()).timeStamp);

            if( reading > 5  )
            {
                    console.log(`Alert ${username} about sensor ${topic} of reading ${JSON.parse(message.value.toString()).reading} on time ${timeStamp.toDateString()} ${timeStamp.toTimeString()}`)
            }
        },
      })
}







