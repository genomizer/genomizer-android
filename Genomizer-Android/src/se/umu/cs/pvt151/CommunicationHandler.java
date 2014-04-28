package se.umu.cs.pvt151;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;

import org.json.JSONObject;

public class CommunicationHandler implements Runnable {
	
	Socket socket;
	
	HttpURLConnection connection;
	
	public CommunicationHandler() throws IOException {
		URL url = new URL("http://www.google.com/");
		connection = (HttpURLConnection) url.openConnection();
	}

	@Override
	public void run() {
		
	}
	
	
	public void logIn(JSONObject logInPackage) {
		
	}
}
