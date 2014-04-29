package se.umu.cs.pvt151;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;

import org.apache.http.impl.io.ChunkedInputStream;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class CommunicationHandler {
	
	HttpURLConnection connection;
	
	OutputStream out;
	InputStream in;
	
	public CommunicationHandler() throws IOException {
		
	}
	
	private void setupConnection(String messageType) throws IOException {
		URL url = new URL("http://genomizer.apiary-mock.com/"+messageType);
		
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
		
		out = connection.getOutputStream();
//		in = connection.getInputStream();
	}
	
	
	private String sendPackage(JSONObject jsonPackage, String messageType) throws IOException {
		byte[] pack = jsonPackage.toString().getBytes("UTF-8");
		
		out.write(pack);
		out.flush();
		
		int responseCode = connection.getResponseCode();
		in = (InputStream) connection.getContent();
		
		byte[] bytes = new byte[1024];
		
		int readByte = -1;
		int i = 0;
		
		while ((readByte = in.read()) != -1) {
			bytes[i] = (byte) readByte;
			i++;
		}
		
		out.close();
		
		return new String(bytes, 0, i, "UTF-8");
	}
	
	
	public boolean logIn(String userName, String password) throws IOException, JSONException {
		JSONObject loginPackage = MessageHandler.createLoginRequest(userName, password);
		
		setupConnection("login");
		String result =  sendPackage(loginPackage, "login");
		//TODO: Extract token from result
		return true;
	}
}
