package controller;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.set;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import model.Sensor;

@Path("/sensor") 
public class SensorController 
{
	//Creating MongoDB connection
	private MongoClient mongoClient = new MongoClient("localhost", 27017);
	private CodecRegistry pojoCodecRegistry = org.bson.codecs.configuration.CodecRegistries.fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), 
										org.bson.codecs.configuration.CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build()));
	private MongoDatabase database = mongoClient.getDatabase("Fire_Alarm").withCodecRegistry(pojoCodecRegistry);
	private MongoCollection<Sensor> collection = database.getCollection("sensors", Sensor.class);
	
	
	/**
	 * Will create and add a new sensor to the Database
	 * @param sensor JSON should contain keys: "floorNumber", "roomNumber"
	 * POST to http://localhost:8080/Fire_Alarm_System/rest/sensor
	 * @return HTTP code 200 if successful
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
    public Response addSensor(Map<String, String> payload) 
	{  
		if(!payload.containsKey("floorNumber") || !payload.containsKey("roomNumber"))
        {
            return Response.status(404).entity("required keys not found in JSON Body").build();  
        }
        final int floorNumber = Integer.parseInt(payload.get("floorNumber"));
        final int roomNumber = Integer.parseInt(payload.get("roomNumber"));
        final String name = payload.get("floorNumber")+payload.get("roomNumber");
		
        if(collection.countDocuments(eq("name", name)) > 0)	//Name already taken
        {
        	return Response.status(409).entity("Sensor already exists").build();  
        }
        
		collection.insertOne(new Sensor(floorNumber, roomNumber));
        return Response.status(200).entity("Sucessfully added new Sensor").build();  
    }  
	
	/**
	 * Will remove a sensor from the Database
	 * @param sensor JSON should contain keys: "floorNumber", "roomNumber"
	 * DELETE to http://localhost:8080/Fire_Alarm_System/rest/sensor
	 * @return HTTP code 200 if successful
	 */
	@DELETE
	@Consumes(MediaType.APPLICATION_JSON)
    public Response removeSensor(Map<String, String> payload) 
	{  
		if(!payload.containsKey("floorNumber") || !payload.containsKey("roomNumber"))
        {
            return Response.status(404).entity("required keys not found in JSON Body").build();  
        }
        final String Name = payload.get("floorNumber") + payload.get("roomNumber");
		
		collection.deleteOne(eq("name", Name));
		
        return Response.status(200).entity("Sucessfully removed Sensor").build();  
    }
	
	
	/**
	 * Change Sensor state
	 * @param sensor JSON should contain keys: "floorNumber", "roomNumber", "active"
	 * PUT to http://localhost:8080/Fire_Alarm_System/rest/sensor
	 * @return HTTP code 200 if successful
	 */
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
    public Response changeState(Map<String, String> payload) 
	{  
		if(!payload.containsKey("floorNumber") || !payload.containsKey("roomNumber") || !payload.containsKey("active"))
        {
            return Response.status(404).entity("required keys not found in JSON Body").build();  
        }
        final String Name = payload.get("floorNumber") + payload.get("roomNumber");
        final boolean active = Boolean.parseBoolean(payload.get("active"));
		
		collection.updateOne(eq("name", Name), set("active",active));
		
        return Response.status(200).entity("Sensor state changed sucessfully").build();  
    }
	
	/**
	 * Set Sensor smoke and Co2 levels
	 * @param sensor JSON should contain keys: "floorNumber", "roomNumber", "smokeLevel", "co2Level"
	 * POST to http://localhost:8080/Fire_Alarm_System/rest/sensor/setLevels
	 * @return HTTP code 200 if successful
	 */
	@Path("/setLevels")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
    public Response setSensorLevels(Map<String, String> payload) 
	{  
		if(!payload.containsKey("floorNumber") || !payload.containsKey("roomNumber") 
				|| !payload.containsKey("smokeLevel") || !payload.containsKey("co2Level"))
        {
            return Response.status(404).entity("required keys not found in JSON Body").build();  
        }
        final String Name = payload.get("floorNumber") + payload.get("roomNumber");
        final int smokeLevel = Integer.parseInt(payload.get("smokeLevel"));
        final int co2Level = Integer.parseInt(payload.get("co2Level"));
		
        ArrayList<Bson> attributes = new ArrayList<>();
        attributes.add(set("smokeLevel",smokeLevel));
        attributes.add(set("co2Level",co2Level));
        
		collection.updateOne(eq("name", Name), attributes );
		
        return Response.status(200).entity("Sensor state changed sucessfully").build();  
    }
	
	
	/**
	 * View status of all sensors
	 * POST to http://localhost:8080/Fire_Alarm_System/rest/sensor/getAll
	 * @return HTTP code 200 if successful along with JSON body 
	 */
	@Path("/getAll")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
    public Response viewSensors() 
	{  
		Map<String, Sensor> payload = new HashMap<>();
		FindIterable<Sensor> sensors = collection.find();
				
		for (Sensor sensor : sensors) 
		{
			payload.put(sensor.getName(), sensor);
		}
		
        return Response.status(200).entity(payload).build();  
    }
	
	
	
	
	
	
	

}
