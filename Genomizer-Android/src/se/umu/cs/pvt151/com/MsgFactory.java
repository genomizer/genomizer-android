package se.umu.cs.pvt151.com;

import org.json.JSONException;
import org.json.JSONObject;

public class MsgFactory {

	private static String serverURL = "http://genomizer.apiary-mock.com";
	private static MsgFactory factoryReference = null;
	
	private MsgFactory() {
				
	}
	
	public static MsgFactory getFactoryInstance() {
		if(factoryReference == null) {
			return new MsgFactory();
		} 
		return factoryReference;
	}
	
	public static void changeServerURL(String url) {
		serverURL = url;
	}
	
	public static String createLogin(String username, String password) throws JSONException {
		JSONObject obj = new JSONObject();
		obj.put("username", username);
		obj.put("password", password);
		
		return null;
	}
	
	
}
