
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Main 
{
	public static void main(String[] args)
	{
		
		System.setProperty("java.security.policy", "file:allowall.policy");

		
        try {
        	ServerInterface service = (ServerInterface) Naming.lookup("//localhost/rmiServer");
            
	        service.addSensor(3,6);
	        
	        
	        
	        
	        
	 
	        } catch (Exception ex) {
	            System.err.println(ex.getMessage());
	        }
	    }

}
