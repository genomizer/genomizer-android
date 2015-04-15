
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import se.umu.cs.pvt151.com.ComHandler;
import se.umu.cs.pvt151.com.Communicator;
import se.umu.cs.pvt151.com.GenomizerHttpPackage;
import se.umu.cs.pvt151.com.MsgDeconstructor;
import se.umu.cs.pvt151.com.MsgFactory;
import se.umu.cs.pvt151.model.Experiment;
import se.umu.cs.pvt151.model.GenomeRelease;
import se.umu.cs.pvt151.model.ProcessStatus;
import junit.framework.TestCase;


public class MsgDeconstructorTest extends TestCase {

	//TODO refactor remove?
	public void testDeconstructSearchResults() {
		try {
			Communicator.initCommunicator("http://genomizer.apiary-mock.com/");

			HashMap<String, String> annotations = new HashMap<String, String>();
			annotations.put("Species", "Human");

			JSONObject msg = new JSONObject();
			GenomizerHttpPackage searchResponse;

			searchResponse = Communicator.sendHTTPRequest
					(msg, "GET", "search/?annotations=" + ComHandler.generatePubmedQuery(annotations));

			JSONArray jsonPackage = new JSONArray(searchResponse.getBody());
			ArrayList<Experiment> experiments = MsgDeconstructor.deconSearch(jsonPackage);

			assertEquals("experimentId", experiments.get(0).getName());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			fail("Failed because of unsupported encoding");
		} catch (IOException e) {
			e.printStackTrace();
			fail("Failed because of IOException");
		} catch (JSONException e) {
			fail("Failed because of JSONException");
			e.printStackTrace();
		}
	}

	public void testDeconstructSearchResults2() {
		JSONArray packageArray = new JSONArray();
		try {
			JSONObject experiment = new JSONObject();
			

			//fake name
			experiment.put("name", "expID");

			//fake fileArray
			JSONArray fileArray = new JSONArray();
			JSONObject file = new JSONObject();
			file.put("id", 10);
			file.put("path", "path");
			file.put("url", "url");
			file.put("type", "type");
			file.put("filename", "filename");
			file.put("date", "date");
			file.put("author", "author");
			file.put("uploader", "uploader");
			file.put("expId", "expId");
			file.put("grVersion", "grVersion");
			fileArray.put(file);
			experiment.put("files", fileArray);

			//fake AnnotationsArray
			JSONArray annotationArray = new JSONArray();
			JSONObject annotation = new JSONObject();
			annotation.put("name", "name");
			annotation.put("value", "value");
			annotationArray.put(annotation);
			experiment.put("annotations", annotationArray);
			
			
			packageArray.put(experiment);
			
			
		} catch (JSONException e){
			fail("Failed because of JSONException in construction");
		}
		
		
		
		
		
		try{
			ArrayList<Experiment> experiments = MsgDeconstructor.deconSearch(packageArray);
			assertEquals("expID", experiments.get(0).getName());
		} catch (JSONException e) {
			fail("Failed because of JSONException in deconstruction");
			e.printStackTrace();
		}
	}


	public void testDeconstructGenomeReleases() {
		try {
			Communicator.initCommunicator("http://genomizer.apiary-mock.com/");

			JSONObject msg = new JSONObject();
			GenomizerHttpPackage genomeResponse = Communicator.sendHTTPRequest(msg, "GET", "genomeRelease");

			String jsonString = genomeResponse.getBody();
			JSONArray jsonPackage = new JSONArray(jsonString);

			ArrayList<GenomeRelease> genomeReleases = MsgDeconstructor.deconGenomeReleases(jsonPackage);

			assertEquals("hy17", genomeReleases.get(0).getGenomeVersion());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			fail("Failed because of unsupported encoding");
		} catch (IOException e) {
			e.printStackTrace();
			fail("Failed because of IOException");
		} catch (JSONException e) {
			fail("Failed because of JSONException");
			e.printStackTrace();
		}
	}


	public void testDeconstructProcessPackage() {
		try {
			Communicator.initCommunicator("http://genomizer.apiary-mock.com/");

			JSONObject msg = new JSONObject();
			GenomizerHttpPackage genomeResponse = Communicator.sendHTTPRequest(msg, "GET", "process");

			String jsonString = genomeResponse.getBody();
			JSONArray jsonPackage = new JSONArray(jsonString);

			ArrayList<ProcessStatus> processes = MsgDeconstructor.deconProcessPackage(jsonPackage);

			assertEquals("Exp1", processes.get(0).getExperimentName());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			fail("Failed because of unsupported encoding");
		} catch (IOException e) {
			e.printStackTrace();
			fail("Failed because of IOException");
		} catch (JSONException e) {
			fail("Failed because of JSONException");
			e.printStackTrace();
		}
	}
}
