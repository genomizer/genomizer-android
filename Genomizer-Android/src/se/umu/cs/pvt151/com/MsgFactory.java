package se.umu.cs.pvt151.com;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import se.umu.cs.pvt151.model.GeneFile;
import se.umu.cs.pvt151.model.ProcessingParameters;

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
	
	
	/**
	 * Creates a conversion JSONObject that contains information about a file, 
	 * an arbitrary number of parameters, metadata, processtype and genomerelease.
	 * 
	 * @param param
	 * @param file
	 * @param metadata
	 * @param processType
	 * @param genomeRelease
	 * @return JSONObject
	 * @throws JSONException
	 */
	public static JSONObject createConversionRequest(ProcessingParameters param, GeneFile file, 
			String metadata, String genomeRelease) throws JSONException {
		
		JSONObject obj = new JSONObject();
		
		obj.put("expid", file.getExpId());
		Log.d("smurf", "Before parameters to json");
		obj.put("parameters", parametersToJson(param));
		Log.d("smurf", "After parameters to json");
		obj.put("metadata", "asddd");
		obj.put("genomeVersion", genomeRelease);
		obj.put("author", "asd");
		
		return obj;
	}
	
	
	/**
	 * Takes a ProcessingParameters object with information about an arbitrary number
	 * of parameters and returns it as an JSONArray.
	 * 
	 * @param param
	 * @return JSONArray
	 * @throws JSONException
	 */
	private static JSONArray parametersToJson(ProcessingParameters param) throws JSONException {

		JSONArray json = new JSONArray();
		for (int i = 0; i < param.size(); i++) {

			json.put(param.getParameter(i));

		}
		
		return json;
	}
}
