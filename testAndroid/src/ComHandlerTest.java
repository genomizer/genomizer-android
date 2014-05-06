

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import se.umu.cs.pvt151.Annotation;
import se.umu.cs.pvt151.com.ComHandler;
import se.umu.cs.pvt151.com.ConnectionException;
import se.umu.cs.pvt151.com.MsgDeconstructor;
import android.util.Log;
import junit.framework.TestCase;

public class ComHandlerTest extends TestCase {
	
	public void testLoginPackage() {
		try {
			boolean test = ComHandler.login("BobSaget", "Hemligt");
			
			if (test) {
				Log.d("DEBUG", "connected");
			}
		} catch (IOException e) {
			Log.d("DEBUG", "Something's wrong");
		} catch (ConnectionException e) {
			Log.d("DEBUG", "Could not connect to server");
		}
	}
	
	
	public void testSearchPackage() {
		HashMap<String, String> searchValues = new HashMap<String, String>();
		searchValues.put("Species", "Human");
		searchValues.put("Sex", "Male");
		try {
			ComHandler.login("John", "password");
			JSONArray experiments = ComHandler.search(searchValues);
			Log.d("DEBUG", experiments.toString());
			assertEquals("experimentName", ((JSONObject) experiments.get(0)).get("name"));
		} catch (IOException e) {
			fail("IOException!");
			e.printStackTrace();
		} catch (ConnectionException e) {
			fail("Can't reach server!");
		} catch (JSONException e) {
			fail("JSONException thrown!");
		}
		
	}
	
	
	public void testAnnotationsPackage() {
		try {
			ComHandler.login("John", "password");
			JSONArray experiments = ComHandler.getServerAnnotations();
			Log.d("DEBUG", "ANNOTATIONS");
			Log.d("DEBUG", experiments.toString());
			assertEquals("pubmedId", ((JSONObject) experiments.get(0)).get("name"));
		} catch (IOException e) {
			fail("IOException!");
			e.printStackTrace();
		} catch (ConnectionException e) {
			fail("Can't reach server!");
		} catch (JSONException e) {
			fail("JSONException thrown!");
		}
		
	}
	
	
	public void testAnnotationsDeconstruct() {
		try {
			Log.d("DECONSTRUCT", "Test: MsgDeconstructor");
			ComHandler.login("John", "password");
			JSONArray jsonAnnotations = ComHandler.getServerAnnotations();
			
			Log.d("DECONSTRUCT", jsonAnnotations.toString());
			ArrayList<Annotation> annotations = MsgDeconstructor.annotationJSON(jsonAnnotations);
			for(int i = 0; i < annotations.size(); i++) {
				Log.d("DECONSTRUCT", (annotations.get(i).getName()));
			}
		} catch (IOException e) {
			fail("IOException!");
			e.printStackTrace();
		} catch (ConnectionException e) {
			fail("Can't reach server!");
		} catch (JSONException e) {
			fail("JSONException thrown!");
		}
	}
	
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
}
