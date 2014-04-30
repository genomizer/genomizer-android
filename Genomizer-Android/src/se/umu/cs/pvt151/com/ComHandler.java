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
			Log.d("DEBUG", "Creating communicator");
			Communicator communicator = new Communicator(serverURL + "login");
			
			Log.d("DEBUG", "Creating msg");
			JSONObject msg = MsgFactory.createLogin(username, password);
			
			Log.d("DEBUG", "Sending msg");
			String jsonString = communicator.sendRequest(msg);
			if (jsonString != null) {
				JSONObject jsonPackage = new JSONObject(jsonString);
				communicator.setToken(jsonPackage.get("token").toString());
				return true;
			} else {
				return false;
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			Log.d("DEBUG", e.getMessage());
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			Log.d("DEBUG", e.getMessage());
			e.printStackTrace();
		}
		return false;
	}
	
	public static List<String[]> search(List<String[]> annotations) {
		
		
		return null;
	}
	
	
}
