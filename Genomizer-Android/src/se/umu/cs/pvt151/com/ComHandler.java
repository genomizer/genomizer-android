package se.umu.cs.pvt151.com;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import se.umu.cs.pvt151.model.Annotation;
import se.umu.cs.pvt151.model.Experiment;
import se.umu.cs.pvt151.model.GeneFile;
import se.umu.cs.pvt151.model.ProcessingParameters;

public class ComHandler {

	private static String serverURL = "http://scratchy.cs.umu.se:7000/";

	private static HashMap<String, ArrayList<GeneFile>> fileMap = 
			new HashMap<String, ArrayList<GeneFile>>();
	
	public static void appendFileList(String key, ArrayList<GeneFile> files) {
		fileMap.put(key, files);
	}
	
	public static ArrayList<GeneFile> getFileList(String key) {
		return fileMap.get(key);
	}
	
	/**
	 * Used to change the targeted server URL.
	 * 
	 * @param serverURL The URL of the server.
	 */
	public static void setServerURL(String serverURL) {
		ComHandler.serverURL = serverURL;
	}

	/**
	 * Sends a login request to the server.
	 * 
	 * @param username The username in the login request.
	 * @param password The password in the login request.
	 * @return True on accepted login, otherwise false.
	 * @throws IOException Is thrown when the application can't communicate 
	 * with the server.
	 * @throws ConnectionException 
	 */
	public static boolean login(String username, String password) throws IOException {
		
		try {
			Communicator communicator = new Communicator(serverURL + "login");
			
			JSONObject msg = MsgFactory.createLogin(username, password);
			communicator.setupConnection("POST");
			
			GenomizerHttpPackage loginResponse = communicator.sendRequest(msg);

			if (loginResponse.getCode() == 200) {
				String jsonString = loginResponse.getBody();
				JSONObject jsonPackage = new JSONObject(jsonString);
				communicator.setToken(jsonPackage.get("token").toString());
				return true;
				
			} else {
				return false;
			}
		} catch (JSONException e) {
			//This is only an issue if the server is changed.
			throw new IOException("JSONException. Has the server changed?");
		}
	}

	/**
	 * Sends a search request to the server. The search is based on annotations,
	 * added as a parameter.
	 * 
	 * @param annotations HashMap with the name of the field as key and the value of the field as value.
	 * @return JSONArray Contains an arbitrary amount of JSONObjects. Each object is information about a file.
	 * @throws IOException
	 */
	public static ArrayList<Experiment> search(HashMap<String, String> annotations) throws IOException {
		
		try {
			Communicator communicator = new Communicator(serverURL + "search/?annotations="+generatePubmedQuery(annotations));
			communicator.setupConnection("GET");
			
			JSONObject msg = MsgFactory.createRegularPackage();

			GenomizerHttpPackage searchResponse = communicator.sendRequest(msg);

			if (searchResponse.getCode() == 200) {
				String jsonString = searchResponse.getBody();
				JSONArray jsonPackage = new JSONArray(jsonString);
				return MsgDeconstructor.deconSearch(jsonPackage);
				
			} else if (searchResponse.getCode() == 204) { 
				//If the search yields no result.
				JSONArray jsonPackage = new JSONArray();
				return MsgDeconstructor.deconSearch(jsonPackage);
			} else {
				return null;
			}
		} catch (JSONException e) {
			return null;
		}
	}

	/**
	 * Returns the Annotations of the server.
	 * 
	 * @return An ArrayList of all Annotations.
	 * @throws IOException If communication with the server fails.
	 */
	public static ArrayList<Annotation> getServerAnnotations() throws IOException {
		
		try {

			Communicator communicator = new Communicator(serverURL + "annotation");
			communicator.setupConnection("GET");
			JSONObject msg = MsgFactory.createRegularPackage();

			GenomizerHttpPackage annotationResponse = communicator.sendRequest(msg);

			if (annotationResponse.getCode() == 200) {
				String jsonString = annotationResponse.getBody();
				JSONArray jsonPackage = new JSONArray(jsonString);
				
				return MsgDeconstructor.deconAnnotations(jsonPackage);
			} else {
				return null;
			}

		} catch (JSONException e) {
			return null;
		}
	}

	/**
	 * Returns a pubmed query string ready to be put in a URL. It is encoded for URLs so it cannot be used elsewhere.
	 * 
	 * @param annotations HashMap with the annotation type as key and the value of the annotation as value.
	 * @return An encoded pubmed query string based on the parameter.
	 * @throws UnsupportedEncodingException If the device cannot encode the query.
	 */
	public static String generatePubmedQuery(HashMap<String, String> annotations) throws UnsupportedEncodingException {
		String pubmedQuery = "";

		Set<String> ann = annotations.keySet();
		int i = 0;
		for (String searchWord : ann) {
			String value = annotations.get(searchWord);
			pubmedQuery += value + "[" + searchWord + "]";
			i++;
			if (i != ann.size()) {
				pubmedQuery+=" AND ";
			}			
		}
		return URLEncoder.encode(pubmedQuery, "UTF-8");
	}


	public static GenomizerHttpPackage rawToProfile(GeneFile file, ProcessingParameters parameters) throws IOException {

		try {
			Communicator communicator = new Communicator(serverURL + "process");
			communicator.setupConnection("PUT");
			
			JSONObject msg = MsgFactory.createConversionRequest(parameters, file, "metadata", "rawtoprofile", "release1");
			Log.d("RAW", msg.toString());
			GenomizerHttpPackage response = communicator.sendRequest(msg);

			return response;

		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
}
