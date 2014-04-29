package se.umu.cs.pvt151.com;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

import android.util.Log;

public class Communicator {

	
	private HttpURLConnection connection;
	private DataOutputStream out;
	
	public Communicator(String urlString) throws IOException {
		setupConnection(urlString);
	}

	public void setupConnection(String urlString) throws IOException {
		URL url = new URL(urlString);
		Log.d("DEBUG", urlString);
		connection = (HttpURLConnection) url.openConnection();
		connection.setDoOutput(true);
		connection.setDoInput (true);
		connection.setRequestMethod("POST");
		connection.setUseCaches(false);
		connection.setRequestProperty("Content-Type", "application/json");
		connection.setRequestProperty("Accept", "application/json");
		connection.setChunkedStreamingMode(100);
		
		connection.setConnectTimeout(3000);
		connection.setReadTimeout(3000);
		
		out = new DataOutputStream(connection.getOutputStream());

	}
	
	public int sendRequest(JSONObject jsonPackage) throws IOException {
		
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
			
		return responseCode;
	}
}
