
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import se.umu.cs.pvt151.com.MsgDeconstructor;
import se.umu.cs.pvt151.model.Experiment;
import se.umu.cs.pvt151.model.GenomeRelease;
import se.umu.cs.pvt151.model.ProcessStatus;
import junit.framework.TestCase;


public class TestMsgDeconstructor extends TestCase {

	public void testDeconstructSearchResults() {
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
			assertFalse(experiments.get(0).getFiles().isEmpty());
			assertEquals("10", experiments.get(0).getFiles().get(0).getFileId());
			assertFalse(experiments.get(0).getAnnotations().isEmpty());
			assertEquals("name", experiments.get(0).getAnnotations().get(0).getName());
		} catch (JSONException e) {
			fail("Failed because of JSONException in deconstruction");
			e.printStackTrace();
		}
	}


	
	
	public void testDeconstructGenomeReleases() {
		
		
		try {
			JSONArray packageArray = new JSONArray();
			JSONObject genome = new JSONObject();
			genome.put("genomeVersion", "hy17");
			genome.put("species", "fly");
			genome.put("folderPath", "pathToVersion");
			JSONArray fileArray = new JSONArray();
			fileArray.put("filename1");
			fileArray.put("filename2");
			fileArray.put("filename3");
			genome.put("files",fileArray);
			packageArray.put(genome);
			
			ArrayList<GenomeRelease> genomeReleases = MsgDeconstructor.deconGenomeReleases(packageArray);
			
			assertFalse(genomeReleases.isEmpty());
			assertEquals("hy17", genomeReleases.get(0).getGenomeVersion());
			assertEquals("fly", genomeReleases.get(0).getSpecie());
			assertEquals("pathToVersion", genomeReleases.get(0).getPath());
		} catch (JSONException e) {
			fail("Failed because of JSONException");
			e.printStackTrace();
		}
		
	}


	
	public void testDeconstructProcessPackage() {
		try {
			JSONArray packageArray = new JSONArray();
			JSONObject process = new JSONObject();
			process.put("experimentName", "Exp1");
			process.put("status", "Finished");
			process.put("author", "yuri");
			process.put("timeAdded", 1400245668744L);
			process.put("timeStarted", 1400245668756L);
			process.put("timeFinished", 1400245669756L);
			JSONArray fileArray = new JSONArray();
			fileArray.put("file1");
			fileArray.put("file2");
			process.put("outputFiles",fileArray);
			packageArray.put(process);
			
			ArrayList<ProcessStatus> processes = MsgDeconstructor.deconProcessPackage(packageArray);
			
			assertFalse(processes.isEmpty());
			assertEquals("Exp1", processes.get(0).getExperimentName());
			assertTrue(processes.get(0).getOutputFiles().length>0);
			assertEquals("file1", processes.get(0).getOutputFiles()[0]);
			assertEquals(1400245668744L, processes.get(0).getTimeAdded());
			
		} catch (JSONException e) {
			fail("Failed because of JSONException");
			e.printStackTrace();
		}
	}
	
}
