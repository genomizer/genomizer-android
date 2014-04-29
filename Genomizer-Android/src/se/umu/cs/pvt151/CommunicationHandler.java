package se.umu.cs.pvt151;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.Socket;
import java.net.URL;

import org.json.JSONObject;

import android.util.Base64;
import android.util.Log;

public class CommunicationHandler {
	
	Socket socket;
	
	HttpURLConnection connection;
	
	OutputStream out;
	InputStream in;
	
	public CommunicationHandler() throws IOException {
		
	}
	
	public void setupConnection(String messageType) throws IOException {
		URL url = new URL("http://genomizer.apiary-mock.com/"+messageType);
		
		connection = (HttpURLConnection) url.openConnection();
		connection.setDoOutput(true);
		connection.setDoInput (true);
		connection.setRequestMethod("POST");
		connection.setUseCaches(false);
		connection.setRequestProperty("Content-Type", "application/json");
		connection.setRequestProperty("Accept", "application/json");
		connection.setChunkedStreamingMode(100);
		
		out = connection.getOutputStream();
		in = connection.getInputStream();
		
		connection.connect();
	}
	
	public void sendPackage(JSONObject jsonPackage, String messageType) throws IOException {
		setupConnection(messageType);
		byte[] pack = jsonPackage.toString().getBytes("UTF-8");
		
		Log.d("DEBUG", pack.length+"");
		
		out.write(pack);
		out.flush();
		out.close();
	}
	
	
	private void connect() {
		
	}
	
	
	private void disconnect() {
		
	}
}
