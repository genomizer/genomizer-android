package se.umu.cs.pvt151.com;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class ComHandler {

	private static String serverURL = "http://genomizer.apiary-mock.com/";

	/**
	 * Sends a login request to the server.
	 * @param username The username in the login request.
	 * @param password The password in the login request.
	 * @return True on accepted login, otherwise false.
	 * @throws IOException Is thrown when the application can't communicate 
	 * with the server.
	 * @throws ConnectionException 
	 */
	public static boolean login(String username, String password) throws IOException, ConnectionException {
		try {
			Communicator communicator = new Communicator(serverURL + "login");

			JSONObject msg = MsgFactory.createLogin(username, password);
			communicator.setupConnection("POST");
			
			String jsonString = communicator.sendRequest(msg);
			
			if (jsonString != null) {
				JSONObject jsonPackage = new JSONObject(jsonString);
				communicator.setToken(jsonPackage.get("token").toString());
				return true;
			} else {
				return false;
			}

		} catch (JSONException e) {
			Log.d("DEBUG", e.getMessage());
			e.printStackTrace();
		} catch (MalformedURLException e) {
			Log.d("DEBUG", e.getMessage());
			e.printStackTrace();
		}
		return false;
	}

	public static JSONArray search(List<String[]> annotations) throws IOException, ConnectionException {
		try {
			//TODO FIX THIS LINE - NEEDS ANNOTATIONS
			Communicator communicator = new Communicator(serverURL + "search/annotations=?");
			communicator.setupConnection("GET");
			
			JSONObject msg = MsgFactory.createRegularPackage();
			
			String jsonString = communicator.sendRequest(msg);
			
			if (jsonString != null) {
				JSONArray jsonPackage = new JSONArray(jsonString);
				Log.d("DEBUG", jsonPackage.toString());
				return jsonPackage;
			} else {
				Log.d("DEBUG", "ComHandler - NULL json paket response");
				return null;
			}
			
		} catch (JSONException e) {
			Log.d("DEBUG", "ComHandler(search) JSONException: " + e.getMessage());
			return null;
		}
	}
	
	
	public static JSONArray getServerAnnotations() throws IOException, ConnectionException {
		try {
			Communicator communicator = new Communicator(serverURL + "annotation");
			communicator.setupConnection("GET");
			JSONObject msg = MsgFactory.createRegularPackage();
			
			String jsonString = communicator.sendRequest(msg);
			
			if (jsonString != null) {
				JSONArray jsonPackage = new JSONArray(jsonString);
				
				Log.d("DEBUG", jsonPackage.toString());
				return jsonPackage;
			} else {
				Log.d("DEBUG", "ComHandler - NULL json paket response");
				return null;
			}
			
		} catch (JSONException e) {
			Log.d("DEBUG", "ComHandler(annotations) JSONException: " + e.getMessage());
			return null;
		}
	}
}
