package rmiClient;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class Main 
{
	public static void main(String[] args)
	{
		 
	
	  System.setProperty("java.security.policy", "file:allowall.policy");
	
	    ServerInterface service = null;
	    try {
	        service = (ServerInterface) Naming.lookup("//localhost/rmiServer");
	 
	        } catch (NotBoundException ex) {
	            System.err.println(ex.getMessage());
	        } catch (MalformedURLException ex) {
	            System.err.println(ex.getMessage());
	        } catch (RemoteException ex) {
	            System.err.println(ex.getMessage());
	        }
	    }

}
