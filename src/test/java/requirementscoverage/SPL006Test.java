package requirementscoverage;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static utils.PropertyHelper.getPropertyValue;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class SPL006Test {
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
    public void test_Substring() {
        try {
            
            endpoint = client.resource(getPropertyValue("moviesapi.uri") + "?q=batman");
            
            ClientResponse response = endpoint.accept("application/json")
                                              .get(ClientResponse.class);
            
            // Step #1
            // Verification if HTTP status code is correct
            assertEquals(response.getStatus(), 200, "Incorrect HTTP status code: "); 

            
            // Step #2
            // Counting movies that contains title of another movie
            String output = response.getEntity(String.class);
            JSONObject jsonObject = (JSONObject) parser.parse(output);
            JSONArray moviesArray = (JSONArray) jsonObject.get("results");
            
            int substringsCounter= 0;
            // Compare each title with all titles we have in response
            for(int i = 0; i < moviesArray.size(); i++){
                String target = ((JSONObject) moviesArray.get(i)).get("title").toString();
                for(int j = 0; j < moviesArray.size(); j++){
                    if ( i != j){ // Not compare the titles for same movie
                        if( ((JSONObject) moviesArray.get(j)).get("title").toString().contains(target) )
                            substringsCounter++;
                    }
                }
            }
            
            // Call assert if count of movies less than 2
            assertTrue(substringsCounter > 2, "Movie's titles that contain another title: " + substringsCounter);
 
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false, "Exception. Read stack trace.");
        }
    }
}
