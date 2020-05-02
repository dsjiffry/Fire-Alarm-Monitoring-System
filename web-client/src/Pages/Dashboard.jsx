import React from 'react'
import "../App.css"
import SensorBoard from '../Components/Organisms/SensorBoard'
import {AuthContext} from "../App.js"

export default function Dashboard() {

  const {state, dispatch } = React.useContext(AuthContext);

  const [sensorBoards , setSensorBoards] = React.useState([]);

  console.log(state )

  React.useEffect( () => {

    let floors = []

    state.sensors.forEach(element => {
      floors.push(element.floor)
    });

    //console.log(floors)

    //Removes duplicates and sorts array
    function uniq(a) {
      return a.sort().filter(function(item, pos, ary) {
          return !pos || item != ary[pos - 1];
      })
    }

    let sorted_unique_floors = uniq(floors)
    //console.log(sorted_unique_floors)

    let SensorBoards = [];

      sorted_unique_floors.forEach(element => {
      //console.log(<SensorBoard key = {element} floor = {element}/>)
      SensorBoards.push(<SensorBoard key = {element} floor = {element}/>)
    });

    

    setSensorBoards(SensorBoards);

    console.log(SensorBoards)

  } , [])



    return (
        <div className="dashboard">
          <h3>{JSON.parse(window.localStorage.getItem('user')) }</h3>
          <hr/>

          {sensorBoards}

        </div>
    )
}
