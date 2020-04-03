package controller;


import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;  

@Path("/hello")  
public class HelloService
{  
	
	/**
	 * Checking Service to make sure REST works
	 * make a GET request to http://localhost:8080/Fire_Alarm_System/rest/hello
	 * @return a status of 200 and a message
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
    public Response getMsg() {  
        String output = "Hello world";  
        return Response.status(200).entity(output).build();  
    }  
    
    
    
    
    
    
    
    
    
    
    
    
    
    
}  