

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import se.umu.cs.pvt151.com.MsgFactory;
import se.umu.cs.pvt151.model.GeneFile;
import junit.framework.TestCase;

public class TestMsgFactory extends TestCase {

	public void testCreateLoginPackage() {
		try {
			JSONObject msg = MsgFactory.createLogin("user", "pass");
			assertEquals("user", msg.get("username"));
			assertEquals("pass", msg.get("password"));
		} catch (JSONException e) {
			fail("JSON exception was thrown");
		}
	}
	
	public void testCreateConversionRequest() {
		try {
			ArrayList<String> parameters = new ArrayList<String>();
			parameters.add("param1");
			parameters.add("param2");
			
			GeneFile file = new GeneFile();
			
			JSONObject msg = MsgFactory.createConversionRequest(parameters, file, "meta", "release");
			assertEquals("meta", msg.getString("metadata"));
			assertEquals("release", msg.getString("genomeVersion"));
		} catch (JSONException e) {
			fail("JSON exception was thrown");
		}
	}
}
