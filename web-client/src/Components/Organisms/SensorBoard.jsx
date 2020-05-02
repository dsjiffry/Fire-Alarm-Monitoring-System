import React from 'react'
import Sensor from '../Molecules/Sensor'

import {AuthContext} from '../../App.js'

export default function SensorBoard(props) {

    const { state , dispatch } = React.useContext(AuthContext);
    
    const [sensorElements , setSensorElements] = React.useState([])

    React.useEffect(() => {
        console.log(state.sensors)

        let sensor_array = state.sensors.filter(
            (sensor) =>   sensor.floor === props.floor
        )

        let sensor_elements = [];
         
        sensor_array.forEach(element => {
            sensor_elements.push(
                <>
                <Sensor 
                    key = {element.sensorUID}
                    sensorData = {element}
                    />
                <br/>
                </>
            )    
        });

        setSensorElements(sensor_elements)

    },[state])

    return (
        <div>
            {props.floor}
            <hr/>
            {sensorElements}
        </div>
    )
}
