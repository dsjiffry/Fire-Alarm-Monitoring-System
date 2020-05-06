import React from 'react'
import "../App.css"
import SensorBoard from '../Components/Organisms/SensorBoard'
import { AuthContext } from "../App.js"
import useInterval from '../hooks/useInterval.js'

export default function Dashboard() {

  const { state, dispatch } = React.useContext(AuthContext);

  const [sensorBoards, setSensorBoards] = React.useState([]);

  // Time between two request to fetch data from api in seconds
  let refresh_period = 10;

  //This custom react hook is from Dan abromov personel blog https://overreacted.io/making-setinterval-declarative-with-react-hooks/
  useInterval(() => {
    console.log(`Run Me every ${refresh_period} seconds`)

    let user = state.user;

    console.log(state)
    //fetching Data From Sensor API 
    fetch(`http://localhost:5000/api/getSensorsByUsername/${user}`,
            {
              method: "get",
            })
            .then(
              (response) => {
                console.log(response)
                response.json().then(
                  (res) => {
                    
                    console.log(res)

                    //Updating the senors (LOGIN reducer method is called for first time fetch and subsequent periodic refetch)
                    
                    dispatch({
                      type: "LOGIN",
                      payload: { sensors: res, user: state.user }
                    })

                  });
              }
            )
            .catch(
              (err) => {
                console.log(err)
              }
            )

  }, refresh_period * 1000);

  
  React.useEffect(() => {

    let floors = []

    state.sensors.forEach(element => {
      floors.push(element.floor)
    });

    //console.log(floors)

    //Removes duplicates and sorts array
    function uniq(a) {
      return a.sort().filter(function (item, pos, ary) {
        return !pos || item != ary[pos - 1];
      })
    }

    let sorted_unique_floors = uniq(floors)
    //console.log(sorted_unique_floors)

    let SensorBoards = [];

    sorted_unique_floors.forEach(element => {
      //console.log(<SensorBoard key = {element} floor = {element}/>)
      SensorBoards.push(<SensorBoard key={element} floor={element} />)
    });

    setSensorBoards(SensorBoards);

    //console.log(SensorBoards)

  }, [])



  return (
    <div className="dashboard">
      <h3>{JSON.parse(window.localStorage.getItem('user'))}</h3>
      <hr />

      {sensorBoards}

    </div>
  )
}
