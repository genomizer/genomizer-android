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
	
	
	/**
	 * Takes a JSONArray object as parameter and breaks it down into a list
	 * of Annotation objects, which is returned.
	 * 
	 * @param json
	 * @return An arraylist of Annotation objects.
	 * @throws JSONException
	 */
	public static ArrayList<Annotation> deconAnnotations(JSONArray json) throws JSONException {
		ArrayList<Annotation> annotations = new ArrayList<Annotation>();
		
		for (int i = 0; i < json.length(); i++) {
			JSONObject obj = (JSONObject) json.get(i);
			Annotation annotation = new Annotation();
			Log.d("LOGIN", obj.toString());
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
	
	
	/**
	 * Converts a list of files from JSON to a an ArrayList of GeneFiles.
	 * The list is broken down into a list of GeneFile objects and returned.
	 * 
	 * @param json
	 * @return An arraylist of GeneFile objects
	 * @throws JSONException
	 */
	public static ArrayList<GeneFile> deconFiles(JSONArray json) throws JSONException {
		ArrayList<GeneFile> files = new ArrayList<GeneFile>();
		
		Log.d("TestLog", "Json length " + json.length());
		
		for (int i = 0; i < json.length(); i++) {
			JSONObject obj = (JSONObject) json.get(i);
			GeneFile file = new GeneFile();
			Log.d("LOGIN", obj.toString());
			file.setFileId(obj.getString("id"));
			file.setExpId(obj.getString("expId"));
			file.setType(obj.getString("type"));
			file.setName(obj.getString("filename"));
			file.setAuthor(obj.getString("author"));
			file.setUploadedBy(obj.getString("uploader"));
			file.setDate(obj.getString("date"));
			file.setUrl(obj.getString("url"));
			file.setPath(obj.getString("path"));
			
			files.add(file);
		}
		return files;
	}
	
	
	/**
	 * Unpacks and returns a JSONArray in the form of an list of experiments.
	 * 
	 * @param json
	 * @return An arraylist with experiments
	 * @throws JSONException
	 */
	public static ArrayList<Experiment> deconSearch(JSONArray json) throws JSONException {
		ArrayList<Experiment> experiments = new ArrayList<Experiment>();

		for (int i = 0; i < json.length(); i++) {
			Experiment experiment = new Experiment();
			
			JSONObject jsonExperiment = (JSONObject) json.get(i);
			
			experiment.setName(jsonExperiment.getString("name"));
			experiment.setCreatedBy(jsonExperiment.getString("created by"));
			
			JSONArray files = jsonExperiment.getJSONArray("files");
			JSONArray annotations = jsonExperiment.getJSONArray("annotations");

			experiment.setFiles(deconFiles(files));
			experiment.setAnnotations(deconAnnotations(annotations));
			
			experiments.add(experiment);
		}
		return experiments;
	}
	
	
	/**
	 * Om det skulle vara mšjligt att gšra en generell deconstruct metod fšr alla typer av json objekt.
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
