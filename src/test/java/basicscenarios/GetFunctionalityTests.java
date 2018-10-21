package basicscenarios;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import static utils.PropertyHelper.getPropertyValue;


public class GetFunctionalityTests {
    Client client;
    WebResource endpoint;
    JSONParser parser;
    int expectedNumberOfRecords = 16;  // expected number of all records in response

    @BeforeClass
    public void setup() {
        client  = Client.create();
        parser = new JSONParser();
     }
    
    @AfterClass
    public void tearDown() {
        client.destroy();
    }
    
    
    // DataProvider for all positive scenarios
    @DataProvider(name = "positive")
    public Object[][] positiveData() {
        return new Object[][] {
           { "?q=batman",         expectedNumberOfRecords },
           { "?q=batman&count=0", expectedNumberOfRecords},
           { "?q=batman&count=1", 1}
        };
    }

    // All positive scenarios
    @Test(enabled = true, dataProvider = "positive")
    public void test_getMovies_positive(String params, int numOfRecords) {
        try {
            
            endpoint = client.resource(getPropertyValue("moviesapi.uri") + params);
            
            ClientResponse response = endpoint.accept("application/json")
                                              .get(ClientResponse.class);
            
            // Step #1
            // Verification if HTTP status code is correct
            assertEquals(response.getStatus(), 200, "Incorrect HTTP status code: "); 

            
            // Step #2
            // Verification if count of returned records is correct
            String output = response.getEntity(String.class);
            JSONObject jsonObject = (JSONObject) parser.parse(output);
            JSONArray moviesArray = (JSONArray) jsonObject.get("results");
            
            assertEquals(moviesArray.size(), numOfRecords, "Incorrect number of records:");

        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false, "Exception. Read stack trace.");
        }

    }
    
    
    // DataProvider for negative scenarios
    @DataProvider(name = "negative")
    public Object[][] negativeData() {
        return new Object[][] {
           { "",          404},
           { "?q=",       404},
           { "?q=123",    404},
           { "?q=notvalidname", 404},
           { "?z=batman",       404},
           { "?q=@?~&",         404},
           { "?q=batman&count=",          404},
           { "?q=batman&count=notnumber", 404},
           { "?q=batman&notcount=0",      404},
           { "?q=batman&count=-1",        404},
           { "?q=batman&count=@?~&",      404},
           { "?q=batman&count=" + Integer.MAX_VALUE, 404}
           
        };
    }
    
    // Negative scenarios - incorrect parameters
    @Test(enabled = true, dataProvider = "negative")
    public void test_getMovies_negative(String params, int returnCode) {
        try {
            
            endpoint = client.resource(getPropertyValue("moviesapi.uri") + params);
            
            ClientResponse response = endpoint.accept("application/json")
                                              .get(ClientResponse.class);
            
            assertEquals(response.getStatus(), returnCode, "Incorrect HTTP status code: ");

        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false, "Exception. Read stack trace.");

        }

    }
    
    // Negative scenario - no Header
    @Test(enabled = true)
    public void test_getMoviesNoHeader_negative() {
        try {
            
            endpoint = client.resource(getPropertyValue("moviesapi.uri") + "?q=batman");
            
            ClientResponse response = endpoint.get(ClientResponse.class);
            
            assertEquals(response.getStatus(), 404, "Incorrect HTTP status code: ");

        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false, "Exception. Read stack trace.");

        }

    }
    
    // Negative scenarios - unsupported methods
    @Test(enabled = true)
    public void test_Methods_negative() {
        try {
            
            endpoint = client.resource(getPropertyValue("moviesapi.uri") + "?q=batman");
            
            ClientResponse response = endpoint.delete(ClientResponse.class);
            assertEquals(response.getStatus(), 404, "Incorrect HTTP status code: ");
            
            response = endpoint.put(ClientResponse.class);
            assertEquals(response.getStatus(), 404, "Incorrect HTTP status code: ");
            
            response = endpoint.head();
            assertEquals(response.getStatus(), 404, "Incorrect HTTP status code: ");
            
            response = endpoint.options(ClientResponse.class);
            assertEquals(response.getStatus(), 404, "Incorrect HTTP status code: ");

        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false, "Exception. Read stack trace.");

        }

    }
    
 }
