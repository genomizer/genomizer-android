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
import se.umu.cs.pvt151.model.ProcessStatus;

/**
 * This class takes care of the communication with the server.
 * 
 * @author Rickard dv12rhm
 *
 */
public class ComHandler {

	private static String serverURL = "dumbledore.cs.umu.se:7000/";



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
	 * Visualizes a toast with a message based on which
	 * responsecode is given as parameter.
	 * 
	 * @param requestType
	 * @param responseCode
	 */
	private static void responseDecode(String requestType, int responseCode) {
		switch(responseCode) {		
		case 204: 
			Genomizer.makeToast(requestType + ": No Content.");
			break;
		case 400:
			Genomizer.makeToast(requestType + ": Bad Request.");
			break;
		case 401:
			Genomizer.makeToast(requestType + ": Access Denied -" +
					"Invalid username or password");
			break;
		case 403:
			Genomizer.makeToast(requestType + ": Forbidden - "
					+ "access denied.");
			break;
		case 404:
			Genomizer.makeToast(requestType + ": Not Found - "
					+ "resource was not found.");
			break;
		case 405:
			Genomizer.makeToast(requestType + ": Method Not Allowed - "
					+ "requested method is not supported for resource.");
			break;
		case 429:
			Genomizer.makeToast(requestType + ": Too Many Requests - "
					+ "please try again later.");
			break;
		case 503:
			Genomizer.makeToast(requestType + ": Service Unavailable - "
					+ "service is temporarily unavailable.");
			break;
		}

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
		if(Genomizer.isOnline()) {
			try {
				JSONObject msg = MsgFactory.createLogin(username, password);			

				GenomizerHttpPackage loginResponse = Communicator.sendHTTPRequest(msg, RESTMethod.POST, "login");	
				if (loginResponse.getCode() == 200) { // OK - the request was successful
					String jsonString = loginResponse.getBody();				
					JSONObject jsonPackage = new JSONObject(jsonString);				
					Communicator.setToken(jsonPackage.get("token").toString());
					return true;

				} else {
					responseDecode("Login response", loginResponse.getCode());
					return false;
				}
			} catch (JSONException e) {
				//This is only an issue if the server is changed.
				throw new IOException("JSONException on response body. Has the server API changed?");
			}
		}
		Genomizer.makeToast("Internet access unavailable.");
		return false;
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
		if(Genomizer.isOnline()) {
			try {					
				JSONObject msg = new JSONObject();
				GenomizerHttpPackage searchResponse = Communicator.sendHTTPRequest
						(msg, RESTMethod.GET, "search/?annotations=" + generatePubmedQuery(annotations));

				if (searchResponse.getCode() == 200) { // OK - the request was successful
					JSONArray jsonPackage = new JSONArray(searchResponse.getBody());
					return MsgDeconstructor.deconSearch(jsonPackage);

				} else { 
					//If the search yields no result.
					responseDecode("Search response", searchResponse.getCode());
					return new ArrayList<Experiment>();
				} 

			} catch (JSONException e) {
				throw new IOException("Unable to understand server response. Has response messages been modified?");
			}		
		}
		throw new IOException("Internet access unavailable.");

	}


	/**
	 * Search with existing Pubmed Query String
	 * @param pubmedQuery
	 * @return
	 * @throws IOException
	 */
	public static ArrayList<Experiment> search(String pubmedQuery) throws IOException {
		if(Genomizer.isOnline()) {
			try {						
				JSONObject msg = new JSONObject();
				GenomizerHttpPackage searchResponse = Communicator.sendHTTPRequest
						(msg, RESTMethod.GET, "search/?annotations=" + pubmedQuery);

				if (searchResponse.getCode() >= 200 && searchResponse.getCode() < 300) {
					JSONArray jsonPackage = new JSONArray(searchResponse.getBody());
					return MsgDeconstructor.deconSearch(jsonPackage);

				} else {
					responseDecode("Search response", searchResponse.getCode());
					return new ArrayList<Experiment>();
				}
			} catch (JSONException e) {
				throw new IOException("Unable to understand server response. "
						+ "Has response messages been modified? " + e.getMessage());
			}

		}
		throw new IOException("Internet access unavailable.");
	}


	/**
	 * Returns the Annotations of the server.
	 * 
	 * @return An ArrayList of all Annotations.
	 * @throws IOException If communication with the server fails.
	 */
	public static ArrayList<Annotation> getServerAnnotations() throws IOException {
		if(Genomizer.isOnline()) {
			try {
				JSONObject msg = new JSONObject();
				GenomizerHttpPackage annotationResponse = Communicator.sendHTTPRequest(msg, RESTMethod.GET, "annotation");

				if (annotationResponse.getCode() == 200) {
					String jsonString = annotationResponse.getBody();
					JSONArray jsonPackage = new JSONArray(jsonString);

					return MsgDeconstructor.deconAnnotations(jsonPackage);
				} else {
					responseDecode("Requesting database annotations", annotationResponse.getCode());				
					return new ArrayList<Annotation>();
				}

			} catch (JSONException e) {
				throw new IOException("Unable to understand server response. "
						+ "Has response messages been modified? " + e.getMessage());
			}
		}
		throw new IOException("Internet connection unavailable.");		
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
	public static boolean rawToProfile(GeneFile file, ArrayList<String> parameters, 
			String meta, String release) throws IOException {
		if(Genomizer.isOnline()) {
			try {			
				JSONObject msg = MsgFactory.createConversionRequest(parameters, file, meta, release);
				GenomizerHttpPackage response = Communicator.sendHTTPRequest(msg, RESTMethod.PUT, "process/rawtoprofile");

				if(response.getCode() == 200) {
					return true;
				} else {
					responseDecode("Raw to profile", response.getCode());
					return false;
				}
			} catch (JSONException e) {
				//This is only an issue if the server is changed.
				throw new IOException("Unable to understand server response. "
						+ "Has response messages been modified? " + e.getMessage());
			}
			
		}
		throw new IOException("Internet connection unavailable.");
		
	}


	/**
	 * Gets and returns the genomereleases from the server as an ArrayList.
	 * 
	 * @return An ArrayList with GenomeRelease objects
	 * @throws IOException
	 */
	public static ArrayList<GenomeRelease> getGenomeReleases() throws IOException {
		if(Genomizer.isOnline()) {
			try {
				JSONObject msg = new JSONObject();
				GenomizerHttpPackage genomeResponse = Communicator.sendHTTPRequest(msg, RESTMethod.GET, "genomeRelease");

				if (genomeResponse.getCode() == 200) {
					String jsonString = genomeResponse.getBody();
					JSONArray jsonPackage = new JSONArray(jsonString);

					return MsgDeconstructor.deconGenomeReleases(jsonPackage);
				} else {
					responseDecode("Requesting genome releases", genomeResponse.getCode());
					return new ArrayList<GenomeRelease>();
				}

			} catch (JSONException e) {
				throw new IOException("Unable to understand server response. "
						+ "Has response messages been modified? " + e.getMessage());
			}
		}
		throw new IOException("Internet connection unavailable.");
	}


	/**
	 * Gets and returns the states of all processes that are currently 
	 * running on the server.
	 * 
	 * @return
	 * @throws IOException
	 */
	public static ArrayList<ProcessStatus> getProcesses() throws IOException {
		if(Genomizer.isOnline()) {
			try {
				JSONObject msg = new JSONObject();
				GenomizerHttpPackage genomeResponse = Communicator.sendHTTPRequest(msg, RESTMethod.GET, "process");

				if (genomeResponse.getCode() == 200) {
					String jsonString = genomeResponse.getBody();
					JSONArray jsonPackage = new JSONArray(jsonString);

					return MsgDeconstructor.deconProcessPackage(jsonPackage);
				} else {
					responseDecode("Requesting status of processes", genomeResponse.getCode());				
					return new ArrayList<ProcessStatus>();
				}

			} catch (JSONException e) {
				throw new IOException("Unable to understand server response. "
						+ "Has response messages been modified? " + e.getMessage());
			}
		}
		throw new IOException("Internet connection unavailable.");
		
	}


	/**
	 * Returns a pubmed query string ready to be put in a URL. It is encoded for URLs so it cannot be used elsewhere.
	 * 
	 * @param annotations HashMap with the annotation type as key and the value of the annotation as value.
	 * @return An encoded pubmed query string based on the parameter.
	 * @throws UnsupportedEncodingException If the device cannot encode the query.
	 */
	public static String generatePubmedQuery(HashMap<String, String> annotations) 
			throws UnsupportedEncodingException {
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
}
