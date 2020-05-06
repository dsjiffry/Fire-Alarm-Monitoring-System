import React from 'react'



export default function Sensor({sensorData}) {

    

    
      

    return (
        <div>
            {sensorData.status}
            <br/>
            {sensorData.reading}
            <br/>
            {sensorData.sensorType}
            <br/>
            {sensorData.sensorUID}
            <br/>
            {sensorData.room}
        </div>
    )
}
