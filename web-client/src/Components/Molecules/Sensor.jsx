import React from 'react'

export default function Sensor({sensorData}) {

    const [value , setValue] = React.useState(0)


    return (
        <div>
            {sensorData.status}
            <br/>
            {value}
            <br/>
            {sensorData.sensorType}
            <br/>
            {sensorData.sensorUID}
            <br/>
            {sensorData.room}
        </div>
    )
}
