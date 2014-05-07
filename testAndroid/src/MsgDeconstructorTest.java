

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import se.umu.cs.pvt151.Experiment;
import se.umu.cs.pvt151.com.ComHandler;
import se.umu.cs.pvt151.com.MsgDeconstructor;
import android.util.Log;
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
	
	
	public void testCanDeconstructSearchResults() {
		HashMap<String, String> searchValues = new HashMap<String, String>();
		searchValues.put("Species", "Human");
		searchValues.put("Sex", "Male");
		try {
			ComHandler.login("John", "password");
			JSONArray experiments = ComHandler.search(searchValues);
			
			ArrayList<Experiment> experimentsArray = MsgDeconstructor.searchJSON(experiments);
			
			assertEquals("experimentName", experimentsArray.get(0).getName());
		} catch (IOException e) {
			e.printStackTrace();
			fail("IOException!");
		} catch (JSONException e) {
			Log.d("TestLog", e.getMessage());
			fail("JSONException!");
			e.printStackTrace();
		}
	}
}
