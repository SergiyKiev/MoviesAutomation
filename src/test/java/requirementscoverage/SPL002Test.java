package requirementscoverage;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static utils.PropertyHelper.getPropertyValue;

import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class SPL002Test {
    Client client;
    WebResource endpoint;
    JSONParser parser;

    @BeforeClass
    public void setup() {
        client  = Client.create();
        parser = new JSONParser();
     }
    
    @AfterClass
    public void tearDown() {
        client.destroy();
    }
    
    @Test(enabled = true)
    public void test_PosterPath() {
        try {
            
            endpoint = client.resource(getPropertyValue("moviesapi.uri") + "?q=batman");
            
            ClientResponse response = endpoint.accept("application/json")
                                              .get(ClientResponse.class);
            
            // Step #1
            // Verification if HTTP status code is correct
            assertEquals(response.getStatus(), 200, "Incorrect HTTP status code: "); 

            
            // Step #2
            // Go through all movies and verify poster_path values 
            String output = response.getEntity(String.class);
            JSONObject jsonObject = (JSONObject) parser.parse(output);
            JSONArray moviesArray = (JSONArray) jsonObject.get("results");
            
            int notValidPostersCounter= 0; // counter for not valid parameters
            Iterator<?> i = moviesArray.iterator();
            while(i.hasNext()){
                JSONObject innerObj = (JSONObject) i.next();
                
                // if parameter is NOT missed for movie
                if (innerObj.containsKey("poster_path")){
                    // if parameter is not NULL
                    if (innerObj.get("poster_path") != null){
                        endpoint = client.resource(innerObj.get("poster_path").toString());
                        // we check a value if it's valid link and it returns 200 OK response
                        try{
                            response = endpoint.accept("application/json")
                                    .get(ClientResponse.class);
                             // if not - we increase counter
                             if (response.getStatus() != 200)
                                 notValidPostersCounter++;
                        }
                        catch (ClientHandlerException e) {
                            notValidPostersCounter++; // poster_path is not valid endpoint value
                        }
                        
                    }
                }
                else{
                    notValidPostersCounter++; // if parameter missed we increase counter
                }
                    
            }
            
            // Call assert if path is invalid or missed at all
            assertEquals(notValidPostersCounter, 0, "Not valid or missed poster_path.");

        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false, "Exception. Read stack trace.");
        }
    }
}
