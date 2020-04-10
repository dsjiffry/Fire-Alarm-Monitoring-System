
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import org.apache.http.client.HttpClient;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import com.google.gson.Gson;

import model.Sensor;

public class Server extends UnicastRemoteObject implements ServerInterface
{
	
	private final String PHEONIX_API_URL = "https://hookb.in/xYNbnkrY6KHLMbORNp7M";
	private Gson gson = new Gson();
	HttpClient httpClient = HttpClientBuilder.create().build();
	
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
        
        System.setProperty("java.security.policy", "file:allowall.policy");

        try{  

            ServerInterface server = new Server();
            Registry registry = LocateRegistry.getRegistry();
            registry.bind("rmiServer", server);

           
            System.out.println ("Service started....");
        }
        catch(Exception e){
            System.err.println(e.getMessage());
        }
    }
   

	public void addSensor(int floorNumber, int roomNumber)
	{
		Sensor sensor = new Sensor(floorNumber, roomNumber);
		System.out.println("WORKS");
		StringEntity postingString = null;
//		try {
//			postingString = new StringEntity(gson.toJson(sensor));
//			HttpPost post = new HttpPost(PHEONIX_API_URL);
//			post.setEntity(postingString);
//			post.setHeader("Content-type", "application/json");
//			HttpResponse  response = httpClient.execute(post);
//			System.out.println(response.getStatusLine().getStatusCode());
			
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		} catch (ClientProtocolException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		
		
		
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

	
	
	private static final long serialVersionUID = 1L;
}
