import React from 'react';
import './App.css';
import {
  BrowserRouter as Router,
  Switch,
  Route,
  Link
} from "react-router-dom";
import WelcomePage from './Pages/WelcomePage';
import Dashboard from './Pages/Dashboard';


//Creating User Context to handle authentication functions
export const AuthContext = React.createContext();

//Initial state to User context
const initialState = {
  isAuthenticated: false,
  user: null,
  token: null,
};

//Reducer function to Login and store user credential in browser storage and to remove 
const reducer = (state, action) => {
  switch (action.type) {
    case "LOGIN":

          // localStorage.setItem("user", JSON.stringify(action.payload.user));
          // localStorage.setItem("sensors", JSON.stringify(action.payload.user));
          
      // localStorage.setItem("token", JSON.stringify(action.payload.token));
      // return {
      //   ...state,
      //   isAuthenticated: true,
      //   user: action.payload.user,
      //   token: action.payload.token
      // };
      console.log(action)
      return {};
    case "LOGOUT":
      localStorage.clear();
      return {
        ...state,
        isAuthenticated: false,
        user: null
      };
    default:
      return state;
  }
};



function App() {

  //Creating The use reducer hook to pass into the Auth Context 
  //State is the initial state we declared ,dispatch is the reducer function we created above 
  //Passing all this to the rest of the applicatin 
  const [state, dispatch] = React.useReducer(reducer, initialState);

  return (
    <Router>
      <AuthContext.Provider
        value={{
          state,
          dispatch
        }}
      >
        <div className="App">
          <Switch>
            <Route path="/Dashboard">
              <Dashboard />
            </Route>
            <Route path="/">
              <WelcomePage />
            </Route>
          </Switch>
        </div>
      </AuthContext.Provider>
    </Router>
  );
}

export default App;
