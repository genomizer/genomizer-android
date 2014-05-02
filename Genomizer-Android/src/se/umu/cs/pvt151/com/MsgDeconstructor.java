package se.umu.cs.pvt151.com;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import se.umu.cs.pvt151.Annotation;

public class MsgDeconstructor {
	
	
	public MsgDeconstructor() {
		
	}
	
	
	public static ArrayList<Annotation> deconstructJsonObject(JSONArray jsonPackage) throws JSONException {
		ArrayList<Annotation> annotations = new ArrayList<Annotation>();
		
		for (int i = 0; i < jsonPackage.length(); i++) {
			JSONObject obj = (JSONObject) jsonPackage.get(i);
			
			Annotation annotation = new Annotation();
			
			annotation.setId(obj.getInt("id"));
			annotation.setName((String) obj.get("name").toString());
			annotation.setForced(obj.getBoolean("forced"));
			JSONArray jarray = obj.getJSONArray("value");
			Log.d("DEBUG", "");
		}
		
		return annotations;
	}
	

}
