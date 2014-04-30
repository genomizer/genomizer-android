package se.umu.cs.pvt151.com;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

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
			communicator.setupConnection();
			String jsonString = communicator.sendRequest(msg, "POST");
			
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

	public static JSONObject search(List<String[]> annotations) throws IOException, ConnectionException {
		try {
			Communicator communicator = new Communicator(serverURL + "search/?");//TODO FIX THIS LINE - NEEDS ANNOTATIONS
			communicator.setupConnection();
			JSONObject msg = MsgFactory.createRegularPackage();
			String jsonString = communicator.sendRequest(msg, "GET");
			
			if (jsonString != null) {
				JSONObject jsonPackage = new JSONObject(jsonString);
				return jsonPackage;
			} else {
				return null;
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
}
