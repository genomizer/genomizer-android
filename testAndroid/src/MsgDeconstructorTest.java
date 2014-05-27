
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import se.umu.cs.pvt151.com.ComHandler;
import se.umu.cs.pvt151.com.Communicator;
import se.umu.cs.pvt151.com.GenomizerHttpPackage;
import se.umu.cs.pvt151.com.MsgDeconstructor;
import se.umu.cs.pvt151.com.MsgFactory;
import se.umu.cs.pvt151.model.Experiment;
import junit.framework.TestCase;


public class MsgDeconstructorTest extends TestCase {
	
	
	public void testCanCreateJSONFromString() {
		JSONObject man = new JSONObject();
		try {
			man.put("head", "hat");
		} catch (JSONException e) {
			fail("Can't put field in JSON");
		}
		String jsonString = man.toString();
		try {
			assertEquals("hat", new JSONObject(jsonString).get("head"));
		} catch (JSONException e) {
			fail("Can't convert from and to JSON properly");
		}
	}
	
	
	public void testDeconstructSearchResults() {
		
		
		try {
			Communicator.initCommunicator("http://genomizer.apiary-mock.com/");
			
			HashMap<String, String> annotations = new HashMap<String, String>();
			annotations.put("Species", "Human");
			
			JSONObject msg = MsgFactory.createRegularPackage();
			GenomizerHttpPackage searchResponse;
			
			searchResponse = Communicator.sendHTTPRequest
					(msg, "GET", "search/?annotations=" + ComHandler.generatePubmedQuery(annotations));
			
			if (searchResponse.getCode() == 200) {
				JSONArray jsonPackage = new JSONArray(searchResponse.getBody());
				ArrayList<Experiment> experiments = MsgDeconstructor.deconSearch(jsonPackage);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			fail("Failed because of unsupported encoding");
		} catch (IOException e) {
			e.printStackTrace();
			fail("Failed because of IOException");
		} catch (JSONException e) {
			fail("Failed because of JSONException");
			e.printStackTrace();
		}
	}
}
