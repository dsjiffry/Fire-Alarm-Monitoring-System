import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Map;


public interface ServerInterface extends Remote
{
	public boolean addSensor(int floorNumber, int roomNumber) throws RemoteException;
	public boolean removeSensor(String floorNumber, String roomNumber) throws RemoteException;
	public boolean changeLevels(String floorNumber, String roomNumber, int smokeLevel, int co2Level) throws RemoteException;
	public boolean changeState(String floorNumber, String roomNumber, boolean state) throws RemoteException;
	public ArrayList< Map<String, String> > viewSensors() throws RemoteException;
}
