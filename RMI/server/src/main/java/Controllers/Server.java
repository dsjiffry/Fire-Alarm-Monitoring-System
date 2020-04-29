package Controllers;

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

import model.Sensor;

import com.google.gson.Gson;

public class Server extends UnicastRemoteObject implements ServerInterface {

	private final String SENSOR_API_URL = "http://localhost:5000/api";
	private final String AUTHENTICATION_BASE_URL = "http://localhost:8080";

	public Server() throws RemoteException {
		super();
	}

	public static void main(String[] args) {

		// Starting the RMI registry
		try {
			LocateRegistry.createRegistry(1099);
		} catch (RemoteException ignored) {
			// Means RMI registry is already running
		}

		System.setProperty("java.security.policy", "file:allowall.policy");

		try {

			ServerInterface server = new Server();
			Registry registry = LocateRegistry.getRegistry();
			registry.bind("rmiServer", server);

			System.out.println("Service started....");

			new Server().createUser();

		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}

	/**
	 * Will make a JSON request to add a Sensor to the database
	 * 
	 * @return true if sucessful
	 */
	@Override
	public boolean addSensor(int floorNumber, int roomNumber, String username, String sensorType) {
		Map<String, String> body = new HashMap<>();
		body.put("username", username);
		body.put("sensorUID", "sensor" + floorNumber + roomNumber);
		body.put("floor", String.valueOf(floorNumber));
		body.put("room", String.valueOf(roomNumber));
		body.put("sensorType", sensorType);
		return makeRequest(body, "POST", SENSOR_API_URL + "/registerSensor");
	}

	/**
	 * Will make a JSON request to remove a Sensor from the database
	 * 
	 * @return true if sucessful
	 */
	@Override
	public boolean removeSensor(int floorNumber, int roomNumber) throws RemoteException {
		Map<String, String> body = new HashMap<>();
		String sensorUID = "sensor" + floorNumber + roomNumber;
		return makeRequest(body, "DELETE", SENSOR_API_URL + "/deleteSensor?sensorUID=" + sensorUID);
	}

	/**
	 * Will make a JSON request to change the state of a Sensor (active or inactive)
	 * 
	 * @return true if sucessful
	 */
	@Override
	public boolean changeState(int floorNumber, int roomNumber, boolean state) throws RemoteException {
		Map<String, String> body = new HashMap<>();
		String sensorUID = "sensor" + floorNumber + roomNumber;
		String status;
		if (state) {
			status = "online";
		} else {
			status = "offline";
		}

		return makeRequest(body, "PUT", SENSOR_API_URL + "/updateSensorStatus/" + sensorUID + "?status=" + status);
	}

	/**
	 * Will make a JSON request to retreive all sensors on database
	 * 
	 * @return sensor Arraylist of the sensor details
	 */
	@Override
	public ArrayList<Sensor> viewSensors(String username) throws RemoteException {
		ArrayList<Sensor> sensors = new ArrayList<>();

		HttpClient httpClient = HttpClientBuilder.create().build();
		HttpGet get = new HttpGet(SENSOR_API_URL + "/getSensorsByUsername/" + username);
		get.setHeader("Content-type", "application/json");
		get.setHeader("Accept", "application/json");

		HttpResponse response = null;
		String responseString = null;
		try {
			response = httpClient.execute(get);
			responseString = new BasicResponseHandler().handleResponse(response);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		for (String string : responseString.replaceAll("[\\[\\]]", "").split("},")) {
			string = string.replaceAll("[{}]|(\\r\\n|\\r|\\n).*}.*,", "");
			if (string.trim().length() == 0) {
				continue;
			}
			string = "{\n" + string.replace("},", "").trim() + "\n}";

			Sensor sensor = new Gson().fromJson(string, Sensor.class);
			if (sensor.getRoom() != 0 && sensor.getFloor() != 0) {
				sensors.add(sensor);
			}
		}
		return sensors;
	}

	/**
	 * Checks the login with the authorization api
	 * 
	 * @param username
	 * @param password
	 * @return true if valid login
	 */
	@Override
	public boolean login(String username, String password) throws RemoteException {
		Map<String, String> body = new HashMap<>();
		body.put("username", String.valueOf(username));
		body.put("password", String.valueOf(password));
		return makeRequest(body, "POST", AUTHENTICATION_BASE_URL + "/loginAdmin");
	}

	/**
	 * Makes sure the authorization server is reachable
	 * 
	 * @return true if reachable
	 */
	@Override
	public boolean checkAuthenticationServer() throws RemoteException {
		Map<String, String> body = new HashMap<>();
		return makeRequest(body, "POST", AUTHENTICATION_BASE_URL + "/checkAuthenticationAlive");
	}

	/**
	 * creating the default user for testing
	 */
	public void createUser() {
		Map<String, String> body = new HashMap<>();
		body.put("username", "admin");
		body.put("password", "admin");
		body.put("email", "admin@test.com");
		body.put("phoneNumber", "0112345678");
		body.put("type", "admin");
		makeRequest(body, "POST", "http://localhost:8080/register");
	}

	/**
	 * Used by add/remove/change methods to make requests.
	 */
	public boolean makeRequest(Map<String, String> body, String RequestType, String URL) {
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

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (response == null || response.getStatusLine().getStatusCode() > 399) {
			return false;
		}
		return true;
	}

	private static final long serialVersionUID = 1L;

}
