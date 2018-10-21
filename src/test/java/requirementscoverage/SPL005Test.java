package requirementscoverage;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertTrue;
import static utils.PropertyHelper.getPropertyValue;

import java.util.Iterator;

import static utils.PalindromeFinder.isPalindromeInString;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class SPL005Test {
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
    public void test_Polindrome() {
        try {
            
            endpoint = client.resource(getPropertyValue("moviesapi.uri") + "?q=batman");
            
            ClientResponse response = endpoint.accept("application/json")
                                              .get(ClientResponse.class);
            
            // Step #1
            // Verification if HTTP status code is correct
            assertEquals(response.getStatus(), 200, "Incorrect HTTP status code: "); 

            
            // Step #2
            // Go through all movies from response and look for palindrome in title 
            String output = response.getEntity(String.class);
            JSONObject jsonObject = (JSONObject) parser.parse(output);
            JSONArray moviesArray = (JSONArray) jsonObject.get("results");
            
            int palindromeCounter= 0;
            Iterator<?> i = moviesArray.iterator();
            while(i.hasNext()){
                JSONObject innerObj = (JSONObject) i.next();
                if( isPalindromeInString( (String)innerObj.get("title") ) ){
                    palindromeCounter++;
                    break;
                }
            }
            
            assertNotEquals(palindromeCounter, 0, "No palindromes found."); // Case fails if no palindromes found

        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false, "Exception. Read stack trace.");
        }
    }
    
}
