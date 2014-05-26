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

import se.umu.cs.pvt151.model.Annotation;
import se.umu.cs.pvt151.model.Experiment;
import se.umu.cs.pvt151.model.GeneFile;
import se.umu.cs.pvt151.model.GenomeRelease;
import se.umu.cs.pvt151.model.Process;
import se.umu.cs.pvt151.model.ProcessingParameters;

public class ComHandler {

	private static String serverURL = "http://itchy.cs.umu.se:7000/";
	
	/**
	 * Used to change the targeted server URL.
	 * 
	 * @param serverURL The URL of the server.
	 */
	public static void setServerURL(String serverURL) {		
		ComHandler.serverURL = serverURL;		
		Communicator.initCommunicator(serverURL);
	}
	
	
	/**
	 * Returns the targeted URL
	 * 
	 * @return serverURL The URL of the server.
	 */
	public static String getServerURL() {		
		return ComHandler.serverURL;
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
			JSONObject msg = MsgFactory.createLogin(username, password);			
			
			GenomizerHttpPackage loginResponse = Communicator.sendHTTPRequest(msg, "POST", "login");

			if (loginResponse.getCode() == 200) {
				String jsonString = loginResponse.getBody();				
				JSONObject jsonPackage = new JSONObject(jsonString);				
				Communicator.setToken(jsonPackage.get("token").toString());
				return true;
				
			} else {
				return false;
			}
		} catch (JSONException e) {
			//This is only an issue if the server is changed.
			throw new IOException("JSONException on response body. Has the server API changed?");
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
			
			JSONObject msg = MsgFactory.createRegularPackage();
			GenomizerHttpPackage searchResponse = Communicator.sendHTTPRequest(msg, "GET", "search/?annotations=" + generatePubmedQuery(annotations));

			if (searchResponse.getCode() == 200) {
				JSONArray jsonPackage = new JSONArray(searchResponse.getBody());
				return MsgDeconstructor.deconSearch(jsonPackage);
				
			} else if (searchResponse.getCode() == 204) { 
				//If the search yields no result.
				JSONArray jsonPackage = new JSONArray();
				return MsgDeconstructor.deconSearch(jsonPackage);
			} 
			
			
		} catch (JSONException e) {
			return new ArrayList<Experiment>();
		}
		return new ArrayList<Experiment>();
	}
	
	/**
	 * Search with existing Pubmed Query String
	 * @param pubmedQuery
	 * @return
	 * @throws IOException
	 */
	public static ArrayList<Experiment> search(String pubmedQuery) throws IOException {
		try {						
			JSONObject msg = MsgFactory.createRegularPackage();
			GenomizerHttpPackage searchResponse = Communicator.sendHTTPRequest(msg, "GET", "search/?annotations=" + pubmedQuery);

			if (searchResponse.getCode() >= 200 && searchResponse.getCode() < 300) {
				JSONArray jsonPackage = new JSONArray(searchResponse.getBody());
				return MsgDeconstructor.deconSearch(jsonPackage);
				
			} else {
				return new ArrayList<Experiment>();
			}
		} catch (JSONException e) {
			return new ArrayList<Experiment>();
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
			JSONObject msg = MsgFactory.createRegularPackage();
			GenomizerHttpPackage annotationResponse = Communicator.sendHTTPRequest(msg, "GET", "annotation");

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
	
	


	/**
	 * Sends a convertion-task to the server were a specified file is to be converted
	 * from raw to profile data.
	 * 
	 * @param file
	 * @param parameters
	 * @return true if the task was recieved and validated by the server, false otherwise
	 * @throws IOException
	 */
	public static boolean rawToProfile(GeneFile file, ProcessingParameters parameters, String meta, String release) throws IOException {

		try {			
			JSONObject msg = MsgFactory.createConversionRequest(parameters, file, meta, release);
			GenomizerHttpPackage response = Communicator.sendHTTPRequest(msg, "PUT", "process/rawtoprofile");

			return response.getCode() == 200;
		} catch (JSONException e) {
			//This is only an issue if the server is changed.
			throw new IOException("JSONException. Has the server changed?");
		}
	}
	
	
	public static ArrayList<GenomeRelease> getGenomeReleases() throws IOException {
		try {
			JSONObject msg = MsgFactory.createRegularPackage();
			GenomizerHttpPackage genomeResponse = Communicator.sendHTTPRequest(msg, "GET", "genomeRelease");

			if (genomeResponse.getCode() == 200) {
				String jsonString = genomeResponse.getBody();
				JSONArray jsonPackage = new JSONArray(jsonString);
				
				return MsgDeconstructor.deconGenomeReleases(jsonPackage);
			} else {
				return null;
			}

		} catch (JSONException e) {
			return null;
		}
	}
	
	
	public static ArrayList<Process> getProcesses() throws IOException {
		try {
			JSONObject msg = MsgFactory.createRegularPackage();
			GenomizerHttpPackage genomeResponse = Communicator.sendHTTPRequest(msg, "GET", "process");

			if (genomeResponse.getCode() == 200) {
				String jsonString = genomeResponse.getBody();
				JSONArray jsonPackage = new JSONArray(jsonString);
				
				return MsgDeconstructor.deconProcessPackage(jsonPackage);
			} else {
				return null;
			}

		} catch (JSONException e) {
			return null;
		}
	}
}
