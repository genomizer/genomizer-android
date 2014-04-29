package se.umu.cs.pvt151.com;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import se.umu.cs.pvt151.CommunicationHandler;
import android.util.Log;

public class ComHandler {

	private static String serverURL = "http://genomizer.apiary-mock.com/";

	
	public static boolean login(String username, String password) {
		
		
		try {
			Log.d("DEBUG", "Creating communicator");
			Communicator communicator = new Communicator(serverURL + "login");
			
			Log.d("DEBUG", "Communicator created");
			
			Log.d("DEBUG", "Creating msg");
			JSONObject msg = MsgFactory.createLogin(username, password);
			
			Log.d("DEBUG", "Sending msg");
			communicator.sendRequest(msg);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			Log.d("DEBUG", e.getMessage());
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			Log.d("DEBUG", e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
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
