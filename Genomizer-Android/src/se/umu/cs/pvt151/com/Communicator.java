package se.umu.cs.pvt151.com;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONObject;

import se.umu.cs.pvt151.model.Genomizer;
import android.util.Log;

public class Communicator {

	private static HttpURLConnection connection;
	private static String urlString;
	private static String token = "";
	private static Communicator staticSelfReference = null;
	

	/**
	 * Creates a new Communicator object.
	 * 
	 * @param urlString - the adress to which packages will be sent
	 */
	private Communicator(String urlString) {		
		Communicator.urlString = urlString;
	}


	/**
	 * Returns a new Communicator object if there are no Communicator
	 * object created at an earlier stage, otherwise it returns the
	 * old object.
	 * 
	 * @param serverURL
	 * @return A Communicator object
	 */
	public static Communicator initCommunicator(String serverURL) {
		if(staticSelfReference == null) {
			staticSelfReference = new Communicator(serverURL);
			return staticSelfReference;
		} else {
			Communicator.urlString = serverURL;
			return staticSelfReference;
		}
	}


	/**
	 * Sets the token which will be sent to the server with each package.
	 * The token works as an identifier for the server.
	 * 
	 * @param token
	 */
	public static void setToken(String token) {
		Communicator.token = token;
	}


	/**
	 * Sets up a connection and sends a package to the server.
	 * The response code and body will be returned in the form of a
	 * GenomizerHttpPackage object if the request succeeds. 
	 * If the request fails a IOException will be thrown.
	 * 
	 * @param jsonPackage
	 * @param requestType
	 * @param urlPostfix
	 * @return Response code and body
	 * @throws IOException
	 */
	public static GenomizerHttpPackage sendHTTPRequest(JSONObject jsonPackage, String requestType, String urlPostfix) throws IOException {
		setupConnection(requestType, urlPostfix);
		return sendRequest(jsonPackage);
	}

	
	/**
	 * Sets up a connection to a server.
	 * 
	 * @param requestType
	 * @param urlPostfix
	 * @throws IOException
	 */
	private static void setupConnection(String requestType, String urlPostfix) throws IOException  {
		URL url = new URL(urlString + urlPostfix);

		connection = (HttpURLConnection) url.openConnection();

		if (!requestType.equals("GET")) {
			connection.setDoOutput(true);
		}

		connection.setDoInput (true);
		connection.setUseCaches(false);
		connection.setRequestMethod(requestType);
		connection.setRequestProperty("Content-Type", "application/json");

		if (!urlPostfix.equals("login")) {			
			connection.setRequestProperty("Authorization", token);
		}

		connection.setChunkedStreamingMode(100);
		connection.setConnectTimeout(4000);
		connection.setReadTimeout(15000);
		connection.setRequestProperty("connection", "close");
	}	


	/**
	 * Sends a request to the server. A JSON package which has to be
	 * includes as an parameter will be sent as body.
	 * 
	 * @param jsonPackage
	 * @return GenomizerHttpPackage - The response code and body
	 * @throws IOException
	 */
	private static GenomizerHttpPackage sendRequest(JSONObject jsonPackage) throws IOException {
		DataOutputStream out = null;
		BufferedReader in = null;
		GenomizerHttpPackage httpResponse = null;
		
		try {
			if (connection.getDoOutput()) {
				out = new DataOutputStream(connection.getOutputStream());						
				byte[] pack = jsonPackage.toString().getBytes("UTF-8");							
				out.write(pack);
				out.flush();				
			}

			int responseCode = -1;
			//Android throws an exception when the response is 401.
			try {
				responseCode = connection.getResponseCode();
			} catch(Exception e) {
				return new GenomizerHttpPackage(404, "");
			}

			if (responseCode < 300 && responseCode >= 200) {
				in = new BufferedReader(
						new InputStreamReader(connection.getInputStream()));

				StringBuffer response = new StringBuffer();
				String inputLine;

				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}				
				httpResponse = new GenomizerHttpPackage(responseCode, response.toString());
			} else {
				httpResponse = new GenomizerHttpPackage(responseCode, "");
			}
		} catch (ConnectTimeoutException e) {
	       Genomizer.makeToast("Connection timed out. No response from server");
	    } catch (SocketTimeoutException e) {
	    	Genomizer.makeToast("Connection timed out. No response from server");
	    } catch(IOException ioe) {
			throw ioe;
		} finally {
			if(in != null) {
				in.close();
			}
		}
		return httpResponse;
	}
}
