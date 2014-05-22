package se.umu.cs.pvt151.com;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;


public class Communicator {


	private static HttpURLConnection connection;
	private static String urlString;
	private static String token = "";
	private static Communicator staticSelfReference = null;
	
	private Communicator(String urlString) {		
		Communicator.urlString = urlString;
	}
	
	public static Communicator initCommunicator(String serverURL) {
		if(staticSelfReference == null) {
			staticSelfReference = new Communicator(serverURL);
			return staticSelfReference;
		} else {
			Communicator.urlString = serverURL;
			return staticSelfReference;
		}
	}

	public static void setToken(String token) {
		Communicator.token = token;
	}
	
	public static GenomizerHttpPackage sendHTTPRequest(JSONObject jsonPackage, String requestType, String urlPostfix) throws IOException {
		setupConnection(requestType, urlPostfix);
		return sendRequest(jsonPackage);
	}
	

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
	}	

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
			
			int responseCode = connection.getResponseCode();
			
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
		} catch(IOException ioe) {
			throw ioe;
		} finally {
			if(in != null) {
				in.close();
			}
			if(out != null) {
				out.close();
			}
		}
		
		return httpResponse;
	}
}
