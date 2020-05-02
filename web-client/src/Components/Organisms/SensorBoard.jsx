import React from 'react'
import Sensor from '../Molecules/Sensor'

export default function SensorBoard(props) {

    

    return (
        <div>
            {props.floor}
            <hr/>
            <Sensor/>
            <br/>
            <Sensor/>
            <br/>
            <Sensor/>
            <br/>
            <Sensor/>
            <br/>
        </div>
    )
}
