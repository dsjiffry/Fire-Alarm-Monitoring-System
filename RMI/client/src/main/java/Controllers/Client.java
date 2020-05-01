package Controllers;

import forms.Alert;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import model.Sensor;

public class Client {

    private ServerInterface service;
    private final String USERNAME;

    private Client() {
        USERNAME = "";
    }

    public Client(String username) {

        USERNAME = username;

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
     * @param sensorType "smoke" or "co2"
     * @return true if request was successful
     */
    public boolean addSensor(int floorNumber, int roomNumber, String sensorType) {
        try {
            return service.addSensor(floorNumber, roomNumber, USERNAME, sensorType);
        } catch (RemoteException ex) {
            return false;
        }
    }

    /**
     * Will as RMI server to change the state of a sensor
     *
     * @param floorNumber
     * @param roomNumber
     * @param state true will make the sensor active, false will make it
     * inactive
     * @param sensorType "smoke" or "co2"
     * @return true if request was successful
     */
    public boolean changeState(int floorNumber, int roomNumber, boolean state, String sensorType) {
        try {
            return service.changeState(floorNumber, roomNumber, state, sensorType);
        } catch (RemoteException ex) {
            return false;
        }
    }

    /**
     * Will make ask RMI server to delete a sensor
     *
     * @param floorNumber
     * @param roomNumber
     * @param sensorType "smoke" or "co2"
     * @return true if request was successful
     */
    public boolean removeSensor(int floorNumber, int roomNumber, String sensorType) {
        try {
            return service.removeSensor(floorNumber, roomNumber, sensorType);
        } catch (RemoteException ex) {
            return false;
        }
    }

    /**
     * Will request all the sensors in the database from the RMI server
     *
     * @return ArrayList of all the Sensors
     */
    public ArrayList<Sensor> getSensors() {
        try {
            return service.viewSensors(USERNAME);
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

    /**
     * getting the reading of a sensor
     *
     * @param sensorName
     * @return HashMap with sensorUID and reading
     */
    public HashMap<String, String> getReading(HashMap<String, String> sensorName) {
        try {
            return service.getReading(sensorName);
        } catch (RemoteException ex) {
            ex.printStackTrace();
            return new HashMap<>();
        }
    }

}
