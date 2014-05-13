package se.umu.cs.pvt151.com;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
			String metadata, String processType, String genomeRelease) throws JSONException {
		
		JSONObject obj = new JSONObject();
		
		obj.put("filename", file.getName());
		obj.put("fileid", file.getFileId());
		obj.put("expid", file.getExpId());
		
		obj.put("processtype", processType);
		obj.put("parameters", parametersToJson(param));
		
		obj.put("metadata", metadata);
		obj.put("genomeRelease", genomeRelease);
		obj.put("author", file.getAuthor());
		
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
		String parameterString = "[";
		
		for (int i = 0; i < param.size(); i++) {
			if (i != 0) {
				parameterString += ",";
			}
			parameterString += param.getParameter(i);
		}
		parameterString += "]";
		
		JSONArray json = new JSONArray(parameterString);
		
		return json;
	}
}
