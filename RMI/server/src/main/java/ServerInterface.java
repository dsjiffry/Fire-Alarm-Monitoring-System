import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;

import javax.ws.rs.core.Response;


public interface ServerInterface extends Remote
{
	public void addSensor(int floorNumber, int roomNumber) throws RemoteException;
	public void removeSensor(String floorNumber, String roomNumber) throws RemoteException;
	public void changeLevels(String floorNumber, String roomNumber, int smokeLevel, int co2Level) throws RemoteException;
	public void changeState(String floorNumber, String roomNumber, boolean state) throws RemoteException;
	public void viewSensors() throws RemoteException;
}
