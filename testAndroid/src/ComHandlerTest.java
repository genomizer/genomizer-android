

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import se.umu.cs.pvt151.Annotation;
import se.umu.cs.pvt151.com.ComHandler;
import android.util.Log;
import junit.framework.TestCase;

public class ComHandlerTest extends TestCase {
	
	public void setUp() {
		ComHandler.setServerURL("http://genomizer.apiary-mock.com/");
	}
	
	public void testLoginPackage() {
		try {
			boolean test = ComHandler.login("BobSaget", "Hemligt");
			
			if (test) {
				Log.d("DEBUG", "connected");
			}
		} catch (IOException e) {
			Log.d("DEBUG", "Could not communicate with the server");
			fail("Could not communicate with the server.");
		}
	}
	
	
	public void testLogInToServer() {
		try {
			Log.d("LOGIN", "http://scratchy.cs.umu.se:7000/");
			ComHandler.setServerURL("http://scratchy.cs.umu.se:7000/");
			
			ComHandler.login("yo", "Hemligt");
			
		} catch (IOException e) {
			fail("Could not communicate with the server.");
		}
	}
	
	
	public void testSearchPackage() {
		HashMap<String, String> searchValues = new HashMap<String, String>();
		searchValues.put("Species", "Human");
		searchValues.put("Sex", "Male");
		try {
			ComHandler.login("John", "SearchTest");
			JSONArray experiments = ComHandler.search(searchValues);
			assertEquals("experimentName", ((JSONObject) experiments.get(0)).get("name"));
		} catch (IOException e) {
			fail("IOException!");
			e.printStackTrace();
		} catch (JSONException e) {
			fail("JSONException thrown!");
		}
		
	}
	
	
	public void testSearchOnServer() {
		ComHandler.setServerURL("http://scratchy.cs.umu.se:7000/");
		
		HashMap<String, String> searchValues = new HashMap<String, String>();
		searchValues.put("Species", "Human");
		searchValues.put("Sex", "Male");
		try {
			ComHandler.login("liveSearchTest", "password");
			JSONArray experiments = ComHandler.search(searchValues);
			assertEquals("experimentName", ((JSONObject) experiments.get(0)).get("name"));
		} catch (IOException e) {
			Log.d("TestLog", e.getMessage());
			e.printStackTrace();
			fail("IOException!");
		} catch (JSONException e) {
			fail("JSONException thrown!");
		}
	}
	
	
	public void testAnnotationsPackage() {
		try {
			ComHandler.login("John", "password");
			ArrayList<Annotation> experiments = ComHandler.getServerAnnotations();
			Log.d("DEBUG", "ANNOTATIONS");
			Log.d("DEBUG", experiments.toString());
			assertEquals("pubmedId", experiments.get(0).getName());
		} catch (IOException e) {
			fail("IOException!");
			e.printStackTrace();
		}
		
	}
	
	
	public void testAnnotationsDeconstruct() {
		try {
			ComHandler.login("John", "password");
			
			ComHandler.getServerAnnotations();
		} catch (IOException e) {
			fail("IOException!");
			e.printStackTrace();
		}
	}
	
	public void testCannotConnect() {
		ComHandler.setServerURL("http://www.thisurldefinitelydoesnotexistordoesit.com/");
		try {
			ComHandler.login("ConnectFail", "worrd");
			fail("Login didn't throw IOException");
		} catch (Exception e) {
			//Expected outcome
		}
	}
	
	/**
	 * Can't fail.
	 */
	public void testPubmedQuery() {
		HashMap<String, String> searchValues = new HashMap<String, String>();
		searchValues.put("Species", "Human");
		searchValues.put("Sex", "Male");
		try {
			String result = ComHandler.generatePubmedQuery(searchValues);
			Log.d("DEBUG", result);
		} catch (UnsupportedEncodingException e) {
			fail("Can't use UTF-8!?");
		}
	}
	
	
//	public void testRawToProfile() {
//		try {
//			ComHandler.login("rawtoprofile", "password");
//			String result = ComHandler.rawToProfile("1234");
//			Log.d("RAW", result);
//			
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
}
