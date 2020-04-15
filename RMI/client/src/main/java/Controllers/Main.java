package Controllers;

import forms.Login;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Sensor;

public class Main {

    private ServerInterface service;

    public Main() {

        System.setProperty("java.security.policy", "file:allowall.policy");

        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }

        try {
            service = (ServerInterface) Naming.lookup("rmi://localhost/rmiServer");
        } catch (MalformedURLException | NotBoundException | RemoteException ex) {
            ex.printStackTrace();
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
            ex.printStackTrace();
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
            ex.printStackTrace();
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
            ex.printStackTrace();
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
            return service.viewSensors();
        } catch (RemoteException ex) {
            ex.printStackTrace();
        }
        return new ArrayList<>();
    }

}
