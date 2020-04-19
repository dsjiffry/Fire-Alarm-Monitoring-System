package Controllers;

import forms.Alert;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import model.Sensor;

public class Client {

    private ServerInterface service;

    public Client() {

        System.setProperty("java.security.policy", "file:allowall.policy");

        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }

        try {
            service = (ServerInterface) Naming.lookup("rmi://localhost/rmiServer");
        } catch (MalformedURLException | NotBoundException | RemoteException ex) {
            Alert alert = new Alert("Connection error. Unable to reach RMI server");
        }
    }

    /**
     * Will as RMI server to add a server
     *
     * @param floorNumber
     * @param roomNumber
     * @return true if request was successful
     */
    public boolean addSensor(int floorNumber, int roomNumber) {
        try {
            return service.addSensor(floorNumber, roomNumber);
        } catch (RemoteException ex) {
            return false;
        }
    }

    /**
     * Will as RMI server to change the state of a sensor
     *
     * @param floorNumber
     * @param roomNumber
     * @param state true will make the sensor active, false will make it inactive
     * @return true if request was successful
     */
    public boolean changeState(int floorNumber, int roomNumber, boolean state) {
        try {
            return service.changeState(floorNumber, roomNumber, state);
        } catch (RemoteException ex) {
            return false;
        }
    }

    /**
     * Will make ask RMI server to delete a sensor
     *
     * @param floorNumber
     * @param roomNumber
     * @return true if request was successful
     */
    public boolean removeSensor(int floorNumber, int roomNumber) {
        try {
            return service.removeSensor(floorNumber, roomNumber);
        } catch (RemoteException ex) {
            return false;
        }
    }

    /**
     * Will request all the sensors in the database from the RMI server
     *
     * @return ArrayList of all the Sensors
     */
    public ArrayList<Sensor> getSensors(){
        try {
            return service.viewSensors();
        } catch (RemoteException ignored) {
        }
        return new ArrayList<>();
    }
    
    /**
     * Will check the login of the user
     *
     * @param username
     * @param password
     * @return true if login was successful
     */
    public boolean login(String username, String password) {
        try {
            return service.login(username, password);
        } catch (RemoteException ex) {
            return false;
        }
    }
    
      /**
     * Will check if the authorization server is reachable
     *
     * @return true if reachable
     */
    public boolean checkAuthenticationServer() {
        try {
            return service.checkAuthenticationServer();
        } catch (RemoteException ex) {
            return false;
        }
    }

}
