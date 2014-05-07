package se.umu.cs.pvt151.com;

import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import se.umu.cs.pvt151.Annotation;
import se.umu.cs.pvt151.Experiment;
import se.umu.cs.pvt151.GeneFile;

public class MsgDeconstructor {
	
	
	public static ArrayList<Annotation> annotationJSON(JSONArray json) throws JSONException {
		ArrayList<Annotation> annotations = new ArrayList<Annotation>();
		
		for (int i = 0; i < json.length(); i++) {
			JSONObject obj = (JSONObject) json.get(i);
			Annotation annotation = new Annotation();
			
			annotation.setId(obj.getInt("id"));
			annotation.setName(obj.getString("name"));
			
			Object valueObject;
			try {
				valueObject = obj.get("values");
			} catch (JSONException e) {
				valueObject = obj.get("value");
			}
			
			if(valueObject instanceof String) {
				annotation.appendValue(valueObject.toString());
			} else {
				JSONArray valueArray = ((JSONArray) valueObject);
				for(int k = 0; k < valueArray.length(); k++) {
					annotation.appendValue(valueArray.getString(k));
				}
			}
			annotations.add(annotation);
		}
		
		return annotations;
	}
	
	
	public static ArrayList<GeneFile> filesJSON(JSONArray json) throws JSONException {
		ArrayList<GeneFile> files = new ArrayList<GeneFile>();
		
		for (int i = 0; i < json.length(); i++) {
			JSONObject obj = (JSONObject) json.get(i);
			GeneFile file = new GeneFile();
			
			file.setId(obj.getString("id"));
			file.setType(obj.getString("type"));
			file.setName(obj.getString("name"));
			file.setUploadedBy(obj.getString("uploadedBy"));
			file.setDate(obj.getString("date"));
			file.setSize(obj.getString("size"));
			file.setURL(obj.getString("URL"));
		}
		
		return files;
	}
	
	
	public static ArrayList<Experiment> searchJSON(JSONArray json) throws JSONException {
		ArrayList<Experiment> experiments = new ArrayList<Experiment>();
		
		for (int i = 0; i < json.length(); i++) {
			Experiment experiment = new Experiment();
			
			JSONObject jsonExperiment = (JSONObject) json.get(i);
			
			experiment.setName(jsonExperiment.getString("name"));
			experiment.setCreatedBy(jsonExperiment.getString("created by"));
			
			JSONArray files = jsonExperiment.getJSONArray("files");
			JSONArray annotations = jsonExperiment.getJSONArray("annotations");
			
			experiment.setFiles(filesJSON(files));
			experiment.setAnnotations(annotationJSON(annotations));
			
			experiments.add(experiment);
		}
		
		return experiments;
	}
	
	
	/**
	 * Om det skulle vara m�jligt att g�ra en generell deconstruct metod f�r alla typer av json objekt.
	 * @deprecated
	 * @param json
	 */
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
	

}
