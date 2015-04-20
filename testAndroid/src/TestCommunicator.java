import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import se.umu.cs.pvt151.com.Communicator;
import se.umu.cs.pvt151.com.GenomizerHttpPackage;
import se.umu.cs.pvt151.com.MsgFactory;
import se.umu.cs.pvt151.com.RESTMethod;
import junit.framework.TestCase;


public class TestCommunicator extends TestCase {
	
	
	public void testInitCommunicator() {
		Communicator c1 = Communicator.initCommunicator("serverURL");
		Communicator c2 = Communicator.initCommunicator("serverURL");
		assertEquals(c1, c2);
	}
	
	public void testInitCommunicator2() {
		Communicator c1 = Communicator.initCommunicator("serverURL");
		Communicator c2 = Communicator.initCommunicator("serverURL2");
		assertEquals(c1, c2);
	}
	
//	//tested in TestComHandler loginTest
//	public void testSendHttpRequestLogin() {
//		
//		try {
//			JSONObject msg = MsgFactory.createLogin("username", "baguette");
//			GenomizerHttpPackage loginResponse = Communicator.sendHTTPRequest(msg, RESTMethod.POST, "login");
//			
//			assertEquals(200,loginResponse.getCode());
//			assertTrue(loginResponse.getBody().length() > 0);
//			
//		} catch (JSONException e) {
//			fail("failed due to JSONException");
//			e.printStackTrace();
//		} catch (IOException e) {
//			fail("failed due to connection error");
//			e.printStackTrace();
//		}			
//		
//	}
	
}
