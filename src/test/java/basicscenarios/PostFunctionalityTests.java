package basicscenarios;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static utils.PropertyHelper.getPropertyValue;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class PostFunctionalityTests {
    Client client;
    WebResource endpoint;
    JSONParser parser;
    JSONObject input; 

    @BeforeClass
    public void setup() {
        client  = Client.create();
        parser = new JSONParser();
        input = new JSONObject();
     }
    
    @AfterClass
    public void tearDown() {
        client.destroy();
    }
 
    // Positive scenario
    @Test(enabled = true)
    public void test_postMovie_positive() {
        try {
            
            endpoint = client.resource(getPropertyValue("moviesapi.uri"));
 
            input.put("name", "superman");
            input.put("description", "the best movie");
             
            ClientResponse response = endpoint.type("application/json")
                                              .post(ClientResponse.class, input.toString());
            
            // Step #1
            // Verification if HTTP status code is correct
            assertEquals(response.getStatus(), 200, "Incorrect HTTP status code: "); 

            
            // Step #2
            // Verification if new record appear in GET
            // This functionality does not work

        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false, "Exception. Read stack trace.");
        }

    }
    
    
    //DataProvider for all negative scenarios
    @DataProvider(name = "negative")
    public Object[][] negData() {
        return new Object[][] {
            { "",     "",          "description", "some movie description", 400}, // "name" required field in payload is empty
            { "name", "some name", "",            "",                       400}, // "description" required field in payload is empty
            { "name", "",          "description", "",                       400}, // values for required fields are empty
            { "name", "   ",       "description", "   ",                    400}, // values for required fields are spaces
            { "name", "~!@#$%",    "description", "~!@#$%",                 400}  // values for required fields are special characters
        };
    }   

    // Negative scenarios - incorrect parameters
    @Test(enabled = true, dataProvider = "negative")
    public void test_postMovie_negative(String nameField, String nameValue, String descrField, String descrValue, int statusCode) {
        try {
          
            endpoint = client.resource(getPropertyValue("moviesapi.uri"));
 
            input.put(nameField, nameValue);
            input.put(descrField, descrValue);
               
            ClientResponse response = endpoint.type("application/json")
                                              .post(ClientResponse.class, input.toString());
              
            // Verification if HTTP status code is correct
            assertEquals(response.getStatus(), statusCode, "Incorrect HTTP status code: "); 
    
            } catch (Exception e) {
                e.printStackTrace();
                assertTrue(false, "Exception. Read stack trace.");
        }

    }
    
    // Negative scenario - numbers in required field's values
    @Test(enabled = true)
    public void test_postMovieNumbersInValue_negative() {
        try {
            
            endpoint = client.resource(getPropertyValue("moviesapi.uri"));
 
            input.put("name", 1234);
            input.put("description", 1234);
             
            ClientResponse response = endpoint.type("application/json")
                                              .post(ClientResponse.class, input.toString());
            
            // Verification if HTTP status code is correct
            assertEquals(response.getStatus(), 400, "Incorrect HTTP status code: "); 

        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false, "Exception. Read stack trace.");
        }

    }
    
    // Negative scenario - no Header
    @Test(enabled = true)
    public void test_noHeader_negative() {
        try {
            
            endpoint = client.resource(getPropertyValue("moviesapi.uri"));
 
            input.put("name", "superman");
            input.put("description", "the best movie");
             
            ClientResponse response = endpoint.post(ClientResponse.class, input.toString());
            
            // Verification if HTTP status code is correct
            assertEquals(response.getStatus(), 404, "Incorrect HTTP status code: "); 
            
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false, "Exception. Read stack trace.");
        }

    }
}

