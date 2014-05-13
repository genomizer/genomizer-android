
import org.json.JSONException;
import org.json.JSONObject;

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
	
	
//	public void testCanDeconstructSearchResults() {
//		HashMap<String, String> searchValues = new HashMap<String, String>();
//		searchValues.put("Species", "Human");
//		try {
//			ComHandler.login("John", "password");
//			JSONArray experiments = ComHandler.search(searchValues);
//			
//			ArrayList<Experiment> experimentArray = MsgDeconstructor.searchJSON(experiments);
//			
//			assertNotNull(experimentArray);
//		} catch (IOException e) {
//			e.printStackTrace();
//			fail("IOException!");
//		} catch (JSONException e) {
//			Log.d("TestLog", e.getMessage());
//			fail("JSONException!");
//			e.printStackTrace();
//		}
//	}
}
