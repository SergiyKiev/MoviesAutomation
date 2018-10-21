package requirementscoverage;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static utils.PropertyHelper.getPropertyValue;
import static utils.GenresColculator.isSumOfGenresMatch;

import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class SPL004Test {
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
    public void test_Genre() {
        try {
            
            endpoint = client.resource(getPropertyValue("moviesapi.uri") + "?q=batman");
            
            ClientResponse response = endpoint.accept("application/json")
                                              .get(ClientResponse.class);
            
            // Step #1
            // Verification if HTTP status code is correct
            assertEquals(response.getStatus(), 200, "Incorrect HTTP status code: "); 

            
            // Step #2
            // Counting movies with genre_id sums > 400
            String output = response.getEntity(String.class);
            JSONObject jsonObject = (JSONObject) parser.parse(output);
            JSONArray moviesArray = (JSONArray) jsonObject.get("results");
            
            int genresCounter= 0;
            Iterator<?> i = moviesArray.iterator();
            while(i.hasNext()){
                JSONObject innerObj = (JSONObject) i.next();
                
                if( isSumOfGenresMatch( (JSONArray)innerObj.get("genre_ids")  ) )
                    genresCounter++;
            }
            
            // Call assert if movies count more than 7
            assertTrue(genresCounter < 7, "Movies with genre_id sums more than 400: " + genresCounter);
 
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false, "Exception. Read stack trace.");
        }
    }
}
