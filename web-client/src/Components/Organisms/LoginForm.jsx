import React from 'react'
import { Redirect } from 'react-router-dom'
import "../../App.css"

import logo from "../../images/asset.svg"

//Importing Auth Context from App.js
import { AuthContext } from "../../App.js"

const initialState = {
  username: "",
  password: "",
  isSubmitting: false,
  errorMessage: null,
  toDashboard: false
};





export default function LoginForm() {

  const [data, setData] = React.useState(initialState);

  const { dispatch } = React.useContext(AuthContext);

  const handleInputChange = event => {
    setData({
      ...data,
      [event.target.name]: event.target.value
    });
  }

  const handleFormSubmit = event => {
    event.preventDefault();
    setData({
      ...data,
      isSubmitting: true,
      errorMessage: null
    });

    //Loggin In the User 
    fetch("http://localhost:8080/loginAdmin", {
      method: "post",
      mode: 'cors',
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify({
        username: data.username,
        password: data.password
      })
    })
      .then(res => {

        console.log(res)
        //If Credentials valid Fetching the senor Data
        if (res.ok) {
          fetch(`http://localhost:5000/api/getSensorsByUsername/${data.username}`,
            {
              method: "get",
            })
            .then(
              (response) => {
                response.json().then(
                  (res) => {
                    dispatch({
                      type: "LOGIN",
                      payload: { sensors: res, user: data.username.trim() }
                    })

                    setData({
                      ...data,
                      toDashboard: true
                    });

                  });
              }
            )
            .catch(
              (err) => {
                console.log(err)
              }
            )

        }
        else {
          alert("Username or password doesnt match !")
          throw res;
          
        }

      })

  };

  return (
    <div className="loginFormContainer">
      <form className="loginFormContainerInternal">
        <img src={logo} />

        <div>
          <h3  >Username</h3>
          <br />
          <input
            type="text"
            value={data.username}
            onChange={handleInputChange}
            name="username"
            id="username"
            placeholder="username" />
        </div>

        <div>
          <br />
          <h3 >Password</h3>
          <br />
          <input type="password"
            placeholder="Password"
            value={data.password}
            onChange={handleInputChange}
            name="password"
            id="password"
          />
        </div>

        <div>

          <br />
          <button onClick={handleFormSubmit} type="submit">Login</button>
          {data.toDashboard ? <Redirect to='/Dashboard' /> : <></>}
        </div>

      </form>

    </div>
  )
}
