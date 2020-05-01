import React from 'react'
import "../App.css"
import SensorBoard from '../Components/Organisms/SensorBoard'
export default function Dashboard() {
    return (
        <div className="dashboard">
          <h3>Username</h3>
          <hr/>

          <SensorBoard floor = {1}/>

        </div>
    )
}
