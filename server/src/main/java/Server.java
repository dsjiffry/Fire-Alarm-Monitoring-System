
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.atomic.AtomicInteger;

import model.Sensor;

public class Server extends UnicastRemoteObject implements ServerInterface
{
	
	private final String PHEONIX_API_URL = "https://hookb.in/xYNbnkrY6KHLMbORNp7M";

    public Server() throws RemoteException{
        super();
    }
		
   public static void main(String[] args)
    {
     
        //Starting the RMI registry
        try {
            LocateRegistry.createRegistry(1099);
        } catch (RemoteException ignored) {
            //Means RMI registry is already running
        }

        // set the policy file as the system securuty policy
        System.setProperty("java.security.policy", "file:allowall.policy");

 

        try{

        	Server svr = new Server();
		 // Bind the remote object's stub in the registry
            Registry registry = LocateRegistry.getRegistry();
            registry.bind("rmiServer", svr);

           
            System.out.println ("Service started....");
        }
        catch(RemoteException re){
            System.err.println(re.getMessage());
        }
        catch(AlreadyBoundException abe){
            System.err.println(abe.getMessage());
        }
    }
   

	public void addSensor(int floorNumber, int roomNumber)
	{
		Sensor sensor = new Sensor(floorNumber, roomNumber);
		
		
	}

	public void removeSensor(String floorNumber, String roomNumber) throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	public void changeLevels(String floorNumber, String roomNumber, int smokeLevel, int co2Level)
			throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	public void changeState(String floorNumber, String roomNumber, boolean state) throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	public void viewSensors() throws RemoteException {
		// TODO Auto-generated method stub
		
	}

}
