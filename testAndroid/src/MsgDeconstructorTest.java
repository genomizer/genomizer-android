

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
}
