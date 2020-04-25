const {Kafka} = require("kafkajs")
let myArgs = process.argv.slice(2);

function randomNumber(min, max) {
    return Math.floor(Math.random() * (max - min) + min);
}

console.log(myArgs[0])

run();



async function run(){
    try
    {
         const kafka = new Kafka({
              "clientId": "myapp",
              "brokers" :["localhost:9092"]
         })

        const producer = kafka.producer();
        console.log("Connecting.....")
        await producer.connect()
        console.log("Connected!")

        
        setInterval(() => {

            let payload = {
                "reading" : randomNumber(0, 10),
                "timeStamp" : new Date()
            }
            
            sendMessage(producer,myArgs[0], JSON.stringify(payload))
            
        }, 1000);
        
        
        
        
    }
    catch(ex)
    {
        console.error(`Something bad happened ${ex}`)
    }
    finally{
        await producer.disconnect();
        process.exit(0);
    }


}

async function sendMessage(producer, topic , message) {
    const result =  await producer.send({
        "topic": topic,
        "messages": [
            {
                "value": message,
                "partition": 0
            }
        ]
    })

    console.log(`Send Successfully! ${JSON.stringify(result)}`)
}