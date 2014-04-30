package se.umu.cs.pvt151.com;

import org.json.JSONException;
import org.json.JSONObject;

public class MsgFactory {

	public static JSONObject createLogin(String username, String password) throws JSONException {
		JSONObject obj = new JSONObject();
		obj.put("username", username);
		obj.put("password", password);		
		return obj;
	}

	public static JSONObject createRegularPackage() throws JSONException {
		return new JSONObject();		
	}
}
