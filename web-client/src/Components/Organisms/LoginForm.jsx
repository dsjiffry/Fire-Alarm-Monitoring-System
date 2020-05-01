import React from 'react'

import "../../App.css"

import logo from "../../images/asset.svg"

export default function LoginForm() {
    return (
        <div className="loginFormContainer">
            <div className="loginFormContainerInternal">
                <img src={logo} />

                <div>
                <h3  >Username</h3>
                <br/>
                <input type = "text" placeholder="userName"/>
                </div>

                <div>
                <br/>
                <h3 >Password</h3>
                <br/>
                <input type = "password"  placeholder="Password"/>
                </div>

                <div>
                
                <br/>
                <button >Login</button>
                
                </div>

            </div>
           
        </div>
    )
}
