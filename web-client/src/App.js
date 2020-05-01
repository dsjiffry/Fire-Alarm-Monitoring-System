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


function App() {
  return (
    <Router>
      <div className="App">
          <Switch>
              <Route path="/Dashboard">
                    <Dashboard/>
              </Route>
              <Route path = "/">
                    <WelcomePage/>
              </Route>
          </Switch>
      </div>
    </Router>
  );
}

export default App;
