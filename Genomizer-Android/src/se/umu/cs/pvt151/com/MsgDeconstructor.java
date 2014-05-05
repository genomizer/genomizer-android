package se.umu.cs.pvt151.com;

import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import se.umu.cs.pvt151.Annotation;

public class MsgDeconstructor {
	
	
	public static void deconstruct(JSONArray json) {
		for(int i = 0; i < json.length(); i++) {
			JSONObject o = json.optJSONObject(i);
			Iterator<String> iterator = o.keys();
			while(iterator.hasNext()) {
				String name = iterator.next();
				Object jo =  o.opt(name);
				
				if(jo != null) {
					Log.d("DECON", "Class name: " +jo.getClass().getSimpleName());
					Log.d("DECON", "Object toString: " + jo.toString());
				} else {
					Log.e("DECON", "NULL OBJECT");
				}
				
				
			}
			
		}
		
	}
	
	
	
	
	public static ArrayList<Annotation> deconstructAnnotationList(JSONArray jsonPackage) throws JSONException {
		ArrayList<Annotation> annotations = new ArrayList<Annotation>();
		
		
		for (int i = 0; i < jsonPackage.length(); i++) {
			JSONObject obj = (JSONObject) jsonPackage.get(i);
			Annotation annotation = new Annotation();
			
			annotation.setId(obj.getInt("id"));
			annotation.setName(obj.getString("name"));
			annotation.setForced(obj.getBoolean("forced"));
			
			Object valueObject = obj.get("value");
			
			if(valueObject instanceof String) {
				annotation.appendValue(valueObject.toString());
			} else {
				JSONArray valueArray = ((JSONArray) valueObject);
				for(int k = 0; k < valueArray.length(); k++) {
					annotation.appendValue(valueArray.getString(k));
				}
			}
						
									
		}
		
		return annotations;
	}
	

}