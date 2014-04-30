package se.umu.cs.pvt151.com;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

import android.util.Log;

public class Communicator {

	
	private HttpURLConnection connection;
	private DataOutputStream out;
	private String urlString;
	private static String token = "";
	
	public Communicator(String urlString) {
		this.urlString = urlString;
	}
	
	public void setToken(String token) {
		Communicator.token = token;
	}

	public void setupConnection() throws ConnectionException  {
		try {
			URL url = new URL(urlString);
			connection = (HttpURLConnection) url.openConnection();
		} catch (IOException e) {
			throw new ConnectionException("Could not connect to the server");
		}
		connection.setDoOutput(true);
		connection.setDoInput (true);
		
		connection.setUseCaches(false);
		connection.setRequestProperty("Content-Type", "application/json");
		if (token.length()>0) {
			connection.setRequestProperty("Authorization", token);
		}
		//connection.setRequestProperty("Accept", "application/json");
		connection.setChunkedStreamingMode(100);
		
		connection.setConnectTimeout(3000);
		connection.setReadTimeout(3000);
	}
	
	public String sendRequest(JSONObject jsonPackage, String packageType) throws IOException {
		
		connection.setRequestMethod(packageType);
		out = new DataOutputStream(connection.getOutputStream());
		
		byte[] pack = jsonPackage.toString().getBytes("UTF-8");
		
		Log.d("DEBUG", "SENDING: " + pack.toString());
		out.write(pack);
		out.flush();
		
		int responseCode = connection.getResponseCode();
		Log.d("DEBUG", "Response: " + responseCode);
		
		BufferedReader in = new BufferedReader(
		        new InputStreamReader(connection.getInputStream()));
		
		StringBuffer response = new StringBuffer();
		String inputLine;
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		System.out.println(response.toString());
		Log.d("DEBUG", "Communicator response: "+ response.toString());
		
		if (responseCode == 200) {
			return response.toString();
		} else {
			return null;
		}
	}
}
