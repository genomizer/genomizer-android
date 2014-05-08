package se.umu.cs.pvt151.com;

import org.json.JSONArray;
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
	
	
	public static JSONObject createConversionRequest(ProcessingParameters param, GeneFile file, String expId, 
			String metadata, String author, String processType, String genomeRelease) throws JSONException {
		
		JSONObject obj = new JSONObject();
		
		obj.put("filename", file.getName());
		obj.put("fileid", file.getId());
		obj.put("expid", expId);
		
		obj.put("processtype", processType);
		obj.put("parameters", parametersToJson(param));
		obj.put("metadata", metadata);
		obj.put("genomeRelease", genomeRelease);
		obj.put("author", author);
		
		return obj;
	}
	
	
	private static JSONArray parametersToJson(ProcessingParameters param) {
		JSONArray json = new JSONArray();
		
		return json;
	}
}
