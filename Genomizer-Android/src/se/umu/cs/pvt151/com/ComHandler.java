package se.umu.cs.pvt151.com;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import se.umu.cs.pvt151.Annotation;
import android.util.Log;

public class ComHandler {

	private static String serverURL = "http://genomizer.apiary-mock.com/";

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
			Log.d("DEBUG", e.getMessage());
			e.printStackTrace();
		} catch (MalformedURLException e) {
			Log.d("DEBUG", e.getMessage());
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Sends a search request to the server. The search is based on annotations,
	 * added as a parameter.
	 * 
	 * @param annotations
	 * @return JSONArray
	 * @throws IOException
	 */
	public static JSONArray search(HashMap<String, String> annotations) throws IOException {
		try {
			Communicator communicator = new Communicator(serverURL + "search/?annotations="+generatePubmedQuery(annotations));
			communicator.setupConnection("GET");

			JSONObject msg = MsgFactory.createRegularPackage();

			GenomizerHttpPackage searchResponse = communicator.sendRequest(msg);

			if (searchResponse.getCode() == 200) {
				String jsonString = searchResponse.getBody();
				JSONArray jsonPackage = new JSONArray(jsonString);
				return jsonPackage;
			} else if (searchResponse.getCode() == 204) { 
				//If the search yields no result.
				JSONArray jsonPackage = new JSONArray();
				return jsonPackage;
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

				Log.d("DEBUG", jsonPackage.toString());
				return MsgDeconstructor.annotationJSON(jsonPackage);
			} else {
				Log.d("DEBUG", "ComHandler - NULL json paket response");
				return null;
			}

		} catch (JSONException e) {
			Log.d("DEBUG", "ComHandler(annotations) JSONException: " + e.getMessage());
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
		String pubmedQuery = "<";

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
		pubmedQuery += ">";
		return URLEncoder.encode(pubmedQuery, "UTF-8");

	}


	public static String rawToProfile(String fileID) throws IOException {

		try {
			Communicator communicator = new Communicator(serverURL + "process/rawtoprofile/" + fileID);
			communicator.setupConnection("PUT");
			JSONObject msg = MsgFactory.createRegularPackage();

			GenomizerHttpPackage annotationResponse = communicator.sendRequest(msg);
			
			String jsonString = annotationResponse.getBody();

			return jsonString;

		} catch (JSONException e) {
			return null;
		}
	}
}
