package se.umu.cs.pvt151;

import org.json.JSONException;
import org.json.JSONObject;

public class MessageHandler {
	
	
	public static JSONObject createLoginRequest(String userName, String password) throws JSONException {
		JSONObject object = new JSONObject();
		
		object.put("username", userName);
		object.put("password", password);
		
		return object;
	}
	
	
//	public static JSONObject createExperimentRequest(String id) throws JSONException {
//		JSONObject object = new JSONObject();
//		return object;
//	}
}
