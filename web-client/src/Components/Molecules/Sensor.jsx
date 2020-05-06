import React from 'react'
import "../../App.css"


export default function Sensor({sensorData}) {

    const [bgColour , setBgColour]  = React.useState("#05020F") 

    React.useEffect(()=>{

        if ( Number( sensorData.reading ) >= 5)
        {
            setBgColour("#FF0000")
        }
        else
        {
            setBgColour("#05020F")
        }

    },[sensorData.reading])


    return (
        <div className="sensorBox" style={{background:bgColour}}>
            
            <div className = "txt1">
                {sensorData.status}
            </div>
            
            <div className = "txt2" >
                <div className = "sensorMiniBox">
                    <div>
                        {sensorData.reading}
                    </div>
                        
                    <div className = "txt3">
                        {sensorData.sensorType}
                        <br/>
                        level
                    </div>
                </div>
            </div>
            <div className = "txt4">
                Room {sensorData.room}
            </div>
            <div className = "txt4" style = {{fontWeight:"lighter"}}>
                {sensorData.sensorUID}
            </div>
          
           
          
            
           
           
        </div>
    )
}
