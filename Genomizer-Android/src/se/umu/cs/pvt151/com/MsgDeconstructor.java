package se.umu.cs.pvt151.com;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import se.umu.cs.pvt151.model.Annotation;
import se.umu.cs.pvt151.model.Experiment;
import se.umu.cs.pvt151.model.GeneFile;
import se.umu.cs.pvt151.model.GenomeRelease;
import se.umu.cs.pvt151.model.ProcessStatus;

/**
 * MsgDeconstructor can be used to deconstruct a number of
 * different packages, in the form of JSONArrays.
 * 
 * @author Rickard dv12rhm
 *
 */
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

		//for each annotation..
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

		//for each file..
		for (int i = 0; i < json.length(); i++) {
			JSONObject obj = (JSONObject) json.get(i);
			GeneFile file = new GeneFile();

			//Sets the files values
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
	
	
	/**
	 * Returns a value from a JSONObject based on a string.
	 * 
	 * @param obj
	 * @param str
	 * @return
	 */
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

		//For each experiment..
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
	 * Deconstructs a JSONArray object into an ArrayList with
	 * GenomeRelease objects and returns it.
	 * 
	 * @param json
	 * @return
	 * @throws JSONException
	 */
	public static ArrayList<GenomeRelease> deconGenomeReleases(JSONArray json) throws JSONException {
		ArrayList<GenomeRelease> genomeReleases = new ArrayList<GenomeRelease>();

		//For each Genome release..
		for (int i = 0; i < json.length(); i++) {
			JSONObject obj = (JSONObject) json.get(i);
			
			GenomeRelease release = new GenomeRelease();
			
			release.setGenomeVersion(obj.getString("genomeVersion"));
			release.setSpecie(obj.getString("species"));
			release.setPath(obj.getString("folderPath"));
			
			genomeReleases.add(release);
		}
		return genomeReleases;
	}


	/**
	 * Deconstructs a JSONArray object into an ArrayList with Process objects
	 * and returns it.
	 * 
	 * @param json
	 * @return
	 * @throws JSONException
	 */
	public static ArrayList<ProcessStatus> deconProcessPackage(JSONArray json) throws JSONException {
		ArrayList<ProcessStatus> processes = new ArrayList<ProcessStatus>();

		//For each process..
		for (int i = 0; i < json.length(); i++) {
			JSONObject obj = (JSONObject) json.get(i);
			
			ProcessStatus process = new ProcessStatus();
			
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
