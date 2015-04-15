

import org.json.JSONException;
import org.json.JSONObject;

import se.umu.cs.pvt151.com.MsgFactory;
import se.umu.cs.pvt151.model.GeneFile;
import se.umu.cs.pvt151.model.ProcessingParameters;
import junit.framework.TestCase;

public class MsgFactoryTest extends TestCase {

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
			ProcessingParameters parameters = new ProcessingParameters();
			parameters.addParameter("param1");
			parameters.addParameter("param2");
			
			GeneFile file = new GeneFile();
			
			JSONObject msg = MsgFactory.createConversionRequest(parameters, file, "meta", "release");
			assertEquals("meta", msg.getString("metadata"));
			assertEquals("release", msg.getString("genomeVersion"));
		} catch (JSONException e) {
			fail("JSON exception was thrown");
		}
	}
}
