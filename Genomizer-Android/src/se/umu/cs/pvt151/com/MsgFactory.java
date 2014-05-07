package se.umu.cs.pvt151.com;

import org.json.JSONException;
import org.json.JSONObject;

import se.umu.cs.pvt151.GeneFile;
import se.umu.cs.pvt151.ProcessingParameters;

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
	
	
	public static JSONObject createConversionRequest(ProcessingParameters param, GeneFile file, 
			String metadata, String author) throws JSONException {
		
		JSONObject obj = new JSONObject();
		
		obj.put("filename", file.getName());
		obj.put("filepath", file.getURL());
		
		//TODO: FIX this method when we know how to create process packages
		obj.put("expid", file.getURL());
		
		return obj;
	}
}
