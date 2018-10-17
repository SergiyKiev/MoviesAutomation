package basicscenarios;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import org.json.simple.parser.JSONParser;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import static utils.PropertyHelper.getPropertyValue;


public class BasicAPITests {
    Client client;
    WebResource endpoint;

    @BeforeClass
    public void setup() {
        client  = Client.create();
     }
    
    @AfterClass
    public void tearDown() {
        client.destroy();
    }
    
    @Test(enabled = true)
    public void test_getAllMovies() {
        try {
            
            endpoint = client.resource(getPropertyValue("moviesapi.uri") + "?q=123");
            
            ClientResponse response = endpoint.accept("application/json")
                                              .get(ClientResponse.class);
            
            
            assertEquals(response.getStatus(), 200, "Failed : HTTP error code : " + response.getStatus());

            String output = response.getEntity(String.class);
            System.out.println("Output from Server .... \n");
            System.out.println(output);

        } catch (Exception e) {
            
            e.printStackTrace();
            
            assertTrue(false);

        }

    }
}
