
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClientBuilder;

import com.google.gson.Gson;

public class Server extends UnicastRemoteObject implements ServerInterface
{
	
	private final String PHEONIX_API_URL = "https://webhook.site/9ea4dde3-4b4e-4c22-a317-663b386686ea";

	
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
   

   /**
    * Will make a JSON request to add a Sensor to the database
    */
   @Override
	public boolean addSensor(int floorNumber, int roomNumber)
	{
		Map<String, String> body = new HashMap<>();
		body.put("floorNumber", String.valueOf(floorNumber));
		body.put("roomNumber", String.valueOf(roomNumber));
		return makeRequest(body, "POST", PHEONIX_API_URL);
	}
	

	@Override
	public boolean removeSensor(String floorNumber, String roomNumber) throws RemoteException 
	{
		Map<String, String> body = new HashMap<>();
		body.put("floorNumber", String.valueOf(floorNumber));
		body.put("roomNumber", String.valueOf(roomNumber));
		return makeRequest(body, "DELETE", PHEONIX_API_URL);
	}

	@Override
	public boolean changeLevels(String floorNumber, String roomNumber, int smokeLevel, int co2Level) throws RemoteException 
	{
		Map<String, String> body = new HashMap<>();
		body.put("floorNumber", String.valueOf(floorNumber));
		body.put("roomNumber", String.valueOf(roomNumber));
		body.put("co2Level", String.valueOf(co2Level));
		return makeRequest(body, "PUT", PHEONIX_API_URL);
	}

	@Override
	public boolean changeState(String floorNumber, String roomNumber, boolean state) throws RemoteException 
	{
		Map<String, String> body = new HashMap<>();
		body.put("floorNumber", String.valueOf(floorNumber));
		body.put("roomNumber", String.valueOf(roomNumber));
		body.put("state", String.valueOf(state));
		return makeRequest(body, "PUT", PHEONIX_API_URL);
	}

	@Override
	public ArrayList< Map<String, String> > viewSensors() throws RemoteException 
	{
		ArrayList< Map<String, String> > sensors = new ArrayList<>();
		
		HttpClient httpClient = HttpClientBuilder.create().build();
		HttpGet get = new HttpGet(PHEONIX_API_URL);
		get.setHeader("Content-type", "application/json");
		get.setHeader("Accept", "application/json");
		
		HttpResponse response = null;
		String responseString = null;
		try {
			 response = httpClient.execute(get);
			 responseString = new BasicResponseHandler().handleResponse(response);
		} 
		catch (ClientProtocolException e) { e.printStackTrace(); } 
		catch (IOException e) { e.printStackTrace(); }
		
		for(String string : responseString.split("\".*\":\\s\\{"))
		{			
			string = string.replaceAll("[{}]|(\\r\\n|\\r|\\n).*}.*,", "");
			if(string.trim().length() == 0)
			{
				continue;
			}
			string = "{\n"+string.replace("},", "").trim() +"\n}";
		
			Map<String,String> sensor = new Gson().fromJson(string , HashMap.class);
			sensors.add(sensor);
		}		
		return sensors;
	}



	
	public boolean makeRequest(Map<String, String> body, String RequestType, String URL)
	{
		HttpResponse response = null;
		try {
			HttpClient httpClient = HttpClientBuilder.create().build();
			StringEntity postingString = new StringEntity(new Gson().toJson(body));
			
			switch (RequestType) {
			case "POST":
				HttpPost post = new HttpPost(URL);
				post.setEntity(postingString);
				post.setHeader("Content-type", "application/json");
				response = httpClient.execute(post);
				break;
				
			case "PUT":
				HttpPut put = new HttpPut(URL);
				put.setEntity(postingString);
				put.setHeader("Content-type", "application/json");
				response = httpClient.execute(put);
				break;
				
			case "DELETE":
				HttpDelete delete = new HttpDelete(URL);
				delete.setHeader("Content-type", "application/json");
				response = httpClient.execute(delete);
				break;
			}
			

			
		} 
		catch (UnsupportedEncodingException e) { e.printStackTrace(); }
		catch (ClientProtocolException e) { e.printStackTrace(); }
		catch (IOException e) { e.printStackTrace(); }
				
		if(response == null || response.getStatusLine().getStatusCode() > 399)
		{
			return false;
		}		
		return true;
	}
	
	
	private static final long serialVersionUID = 1L;
}