
import java.rmi.Naming;
import java.util.Map;


public class Main 
{
	public static void main(String[] args)
	{
		
		System.setProperty("java.security.policy", "file:allowall.policy");

		
        try {
        	ServerInterface service = (ServerInterface) Naming.lookup("//localhost/rmiServer");
			
			// service.addSensor(3, 5);

			for(Map<String,String> sensor : service.viewSensors())
			{
				System.out.println(sensor.get("name"));
			}

	        
	        
	        
	        
	        
	        
	 
	        } catch (Exception ex) {
	            System.err.println(ex.getMessage());
	        }
	    }

}
