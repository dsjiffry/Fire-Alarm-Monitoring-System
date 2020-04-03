package controller;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.glassfish.jersey.server.model.ParamQualifier;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import model.Sensor;

@Path("/sensor") 
public class SensorController 
{
	//Creating MongoDB connections
	MongoClient mongoClient = new MongoClient("localhost", 27017);
	CodecRegistry pojoCodecRegistry = org.bson.codecs.configuration.CodecRegistries.fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), 
										org.bson.codecs.configuration.CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build()));
	MongoDatabase database = mongoClient.getDatabase("Fire_Alarm").withCodecRegistry(pojoCodecRegistry);
	MongoCollection<Sensor> collection = database.getCollection("sensors", Sensor.class);
	
	
	/**
	 * Will create and add a new sensor to the Database
	 * @param sensor JSON should contain keys: "floorNumber", "roomNumber"
	 * @return
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
		
		collection.insertOne(new Sensor(floorNumber, roomNumber));
        return Response.status(200).entity("Sucessfully added new Sensor").build();  
    }  
	
	
	
	
	

}
