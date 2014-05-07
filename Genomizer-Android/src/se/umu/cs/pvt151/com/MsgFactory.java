package se.umu.cs.pvt151.com;

import org.json.JSONException;
import org.json.JSONObject;

import se.umu.cs.pvt151.GeneFile;
import se.umu.cs.pvt151.ProcessParameters;

public class MsgFactory {

	/**
	 * Creates a login JSONObject that contains username and password.
	 * 
	 * @param username
	 * @param password
	 * @return JSONObject
	 * @throws JSONException
	 */
	public static JSONObject createLogin(String username, String password) throws JSONException {
		JSONObject obj = new JSONObject();
		obj.put("username", username);
		obj.put("password", password);		
		return obj;
	}

	
	/**
	 * Creates a standard JSONObject used to communicate with a server.
	 * 
	 * @return JSONObject
	 * @throws JSONException
	 */
	public static JSONObject createRegularPackage() throws JSONException {
		return new JSONObject();		
	}
	
	
	public static JSONObject createConversionRequest(ProcessParameters param, GeneFile file) {
		JSONObject obj = new JSONObject();
		
		return obj;
	}
}
