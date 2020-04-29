package Controllers;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import model.Sensor;

public interface ServerInterface extends Remote {

    public boolean addSensor(int floorNumber, int roomNumber, String username, String sensorType) throws RemoteException;

    public boolean removeSensor(int floorNumber, int roomNumber, String sensorType) throws RemoteException;

    public boolean changeState(int floorNumber, int roomNumber, boolean state, String sensorType) throws RemoteException;

    public ArrayList<Sensor> viewSensors(String username) throws RemoteException;

    public boolean login(String username, String password) throws RemoteException;
    
    public boolean checkAuthenticationServer()  throws RemoteException;
}