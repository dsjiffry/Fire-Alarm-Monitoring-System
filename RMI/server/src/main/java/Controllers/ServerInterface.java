package Controllers;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Map;

import model.Sensor;

public interface ServerInterface extends Remote {

	public boolean addSensor(int floorNumber, int roomNumber) throws RemoteException;

	public boolean removeSensor(int floorNumber, int roomNumber) throws RemoteException;

	public boolean changeState(int floorNumber, int roomNumber, boolean state) throws RemoteException;

	public ArrayList<Sensor> viewSensors() throws RemoteException;
}
