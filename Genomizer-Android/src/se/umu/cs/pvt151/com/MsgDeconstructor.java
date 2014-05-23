package se.umu.cs.pvt151.com;

import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import se.umu.cs.pvt151.model.Annotation;
import se.umu.cs.pvt151.model.Experiment;
import se.umu.cs.pvt151.model.GeneFile;
import se.umu.cs.pvt151.model.GenomeRelease;
import se.umu.cs.pvt151.model.Process;

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

		for (int i = 0; i < json.length(); i++) {
			
			JSONObject obj = (JSONObject) json.get(i);
			GeneFile file = new GeneFile();

			file.setFileId(getStringFromJObj(obj, "id"));
			file.setExpId(getStringFromJObj(obj, "expId"));
			file.setType(getStringFromJObj(obj, "type"));
			file.setName(getStringFromJObj(obj, "filename"));
			file.setAuthor(getStringFromJObj(obj, "author"));
			file.setUploadedBy(getStringFromJObj(obj, "uploader"));
			file.setDate(getStringFromJObj(obj, "date"));
			file.setUrl(getStringFromJObj(obj, "url"));
			file.setPath(getStringFromJObj(obj, "path"));
			file.setGrVersion(getStringFromJObj(obj, "grVersion"));

			files.add(file);
		}
		return files;
	}
	
	private static String getStringFromJObj(JSONObject obj, String str) {
		try {
			return obj.getString(str);
		} catch (JSONException e) {
			return "Missing value";
		}
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


	public static ArrayList<GenomeRelease> deconGenomeReleases(JSONArray json) throws JSONException {
		ArrayList<GenomeRelease> genomeReleases = new ArrayList<GenomeRelease>();

		for (int i = 0; i < json.length(); i++) {
			JSONObject obj = (JSONObject) json.get(i);
			
			GenomeRelease release = new GenomeRelease();
			
			release.setGenomeVersion(obj.getString("genomeVersion"));
			release.setSpecie(obj.getString("specie"));
			release.setPath(obj.getString("path"));
//			release.setFileName(obj.getString("fileName"));
			
			genomeReleases.add(release);
		}
		return genomeReleases;
	}


	public static ArrayList<Process> deconProcessPackage(JSONArray json) throws JSONException {
		ArrayList<Process> processes = new ArrayList<Process>();

		for (int i = 0; i < json.length(); i++) {
			JSONObject obj = (JSONObject) json.get(i);
			
			Process process = new Process();
			
			process.setExperimentName(obj.getString("experimentName"));
			process.setStatus(obj.getString("status"));
			process.setAuthor(obj.getString("author"));
			
			process.setTimeAdded(obj.getLong("timeAdded"));
			process.setTimeStarted(obj.getLong("timeStarted"));
			process.setTimeFinnished(obj.getLong("timeFinished"));
			
			JSONArray outputFiles = obj.getJSONArray("outputFiles");
			String[] files = new String[outputFiles.length()];
			
			for (int j = 0; j < files.length; j++) {
				files[j] = outputFiles.getString(j);
			}
			
			process.setOutputFiles(files);
			
			processes.add(process);
		}
		return processes;
	}
}
