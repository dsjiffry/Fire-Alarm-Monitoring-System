import React from 'react'
import Sensor from '../Molecules/Sensor'

import "../../App.css"

import {AuthContext} from '../../App.js'

export default function SensorBoard(props) {

    const { state , dispatch } = React.useContext(AuthContext);
    
    const [sensorElements , setSensorElements] = React.useState([])



    React.useEffect(() => {
       
        //console.log("You should re render every 10 seconds")

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
            <h2>{`Floor ${props.floor}`}</h2>
            <br/>
            <div className="sensorBoard">
                {sensorElements}
            </div>
        </div>
    )
}
