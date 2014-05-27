

import org.json.JSONException;
import org.json.JSONObject;

import se.umu.cs.pvt151.com.MsgFactory;
import junit.framework.TestCase;

public class MsgFactoryTest extends TestCase {
	
	public void setUp() {
		
	}
	
	
	public void testLogIn() {
		try {
			JSONObject msg = MsgFactory.createLogin("user", "pass");
			assertEquals("user", msg.get("username"));
			assertEquals("pass", msg.get("password"));
		} catch (JSONException e) {
			fail("JSON exception was thrown");
		}
	}
	
	
	public void testSearch() {
		
	}
	
	
	public void tearDown() {
		
	}
}
