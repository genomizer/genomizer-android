

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import se.umu.cs.pvt151.com.ComHandler;
import se.umu.cs.pvt151.com.GenomizerHttpPackage;
import se.umu.cs.pvt151.model.Annotation;
import se.umu.cs.pvt151.model.Experiment;
import se.umu.cs.pvt151.model.GeneFile;
import se.umu.cs.pvt151.model.GenomeRelease;
import se.umu.cs.pvt151.model.ProcessStatus;
import se.umu.cs.pvt151.model.ProcessingParameters;
import android.util.Log;
import junit.framework.TestCase;

public class ComHandlerTest extends TestCase {
	
	public void setUp() {
		ComHandler.setServerURL("http://genomizer.apiary-mock.com/");
	}
	
	public void testLoginPackage() {
		try {
			boolean test = ComHandler.login("BobSaget", "Hemligt");
			
			if (test) {
				Log.d("DEBUG", "connected");
			}
		} catch (IOException e) {
			Log.d("DEBUG", "Could not communicate with the server");
			fail("Could not communicate with the server.");
		}
	}
	
	
	public void testLogInToServer() {
		try {
			Log.d("LOGIN", "http://scratchy.cs.umu.se:7000/");
			ComHandler.setServerURL("http://scratchy.cs.umu.se:7000/");
			
			ComHandler.login("yo", "Hemligt");
			
		} catch (IOException e) {
			fail("Could not communicate with the server.");
		}
	}
	
	
	public void testSearchPackage() {
		HashMap<String, String> searchValues = new HashMap<String, String>();
		searchValues.put("Species", "Human");
		searchValues.put("Sex", "Male");
		try {
			ComHandler.login("John", "SearchTest");
			ArrayList<Experiment> experiments = ComHandler.search(searchValues);
			assertEquals("experimentName", experiments.get(0).getName());
		} catch (IOException e) {
			fail("IOException!");
			e.printStackTrace();
		} 
	}
	
	
	public void testSearchOnServer() {
		ComHandler.setServerURL("http://scratchy.cs.umu.se:7000/");
		
		HashMap<String, String> searchValues = new HashMap<String, String>();
		searchValues.put("Sex", "male");
		searchValues.put("Specie", "human");
		try {
			ComHandler.login("liveSearchTest", "password");
			ArrayList<Experiment> experiments = ComHandler.search(searchValues);
			assertNotNull(experiments);
		} catch (IOException e) {
			Log.d("TestLog", e.getMessage());
			e.printStackTrace();
			fail("IOException!");
		} 
	}
	
	
	public void testAnnotationsPackage() {
		try {
			ComHandler.login("John", "password");
			ArrayList<Annotation> experiments = ComHandler.getServerAnnotations();
			Log.d("DEBUG", "ANNOTATIONS");
			Log.d("DEBUG", experiments.toString());
			assertEquals("pubmedId", experiments.get(0).getName());
		} catch (IOException e) {
			fail("IOException!");
			e.printStackTrace();
		}
		
	}
	
	
	public void testAnnotationsPackageFromServer() {
		try {
			ComHandler.setServerURL("http://scratchy.cs.umu.se:7000/");
			
			ComHandler.login("annotationFromServer", "password");
			ArrayList<Annotation> experiments = ComHandler.getServerAnnotations();
			assertNotNull(experiments);
		} catch (IOException e) {
			fail("IOException!");
			e.printStackTrace();
		}
		
	}
	
	
	public void testAnnotationsDeconstruct() {
		try {
			ComHandler.login("John", "password");
			
			ComHandler.getServerAnnotations();
		} catch (IOException e) {
			fail("IOException!");
			e.printStackTrace();
		}
	}
	
	public void testCannotConnect() {
		ComHandler.setServerURL("http://www.thisurldefinitelydoesnotexistordoesit.com/");
		try {
			ComHandler.login("ConnectFail", "worrd");
			fail("Login didn't throw IOException");
		} catch (Exception e) {
			//Expected outcome
		}
	}
	
	/**
	 * Can't fail.
	 */
	public void testPubmedQuery() {
		HashMap<String, String> searchValues = new HashMap<String, String>();
		searchValues.put("Species", "Human");
		searchValues.put("Sex", "Male");
		try {
			String result = ComHandler.generatePubmedQuery(searchValues);
			Log.d("DEBUG", result);
		} catch (UnsupportedEncodingException e) {
			fail("Can't use UTF-8!?");
		}
	}
	
	
	public void testRawToProfile() {
		ComHandler.setServerURL("http://scratchy.cs.umu.se:7000/");
		
		HashMap<String, String> searchValues = new HashMap<String, String>();
		searchValues.put("Species", "Human");
		
		ArrayList<Experiment> experiments = new ArrayList<Experiment>();
		
		try {
			ComHandler.login("liveSearchTest", "password");
			experiments = ComHandler.search(searchValues);
		} catch (IOException e) {
			Log.d("TestLog", e.getMessage());
			e.printStackTrace();
			fail("IOException!");
		} 
		
		List<GeneFile> files = experiments.get(0).getFiles();
		
		ProcessingParameters parameters = new ProcessingParameters();
		parameters.addParameter("param1");
		parameters.addParameter("param2");
		parameters.addParameter("param3");
		parameters.addParameter("param4");
		
		try {
			boolean result = ComHandler.rawToProfile(files.get(0), parameters, "meta", "randoom");
			
			Log.d("RAW", "Code" + result);
			
		} catch (IOException e) {
			e.printStackTrace();
			fail("Could not create profile from raw data.");
		}
	}
	
	
	public void testGetGenomeReleases() {
		ComHandler.setServerURL("http://scratchy.cs.umu.se:7000/");
		
		ArrayList<GenomeRelease> gr = new ArrayList<GenomeRelease>();
		
		try {
			ComHandler.login("liveSearchTest", "password");
			gr = ComHandler.getGenomeReleases();
			
			assertNotNull(gr);
			
		} catch (IOException e) {
			e.printStackTrace();
			fail("IOException!");
		} 
	}
	
	
	public void testGetProcesses() {
		ComHandler.setServerURL("http://scratchy.cs.umu.se:7000/");
		
		
		ArrayList<ProcessStatus> processes = new ArrayList<ProcessStatus>();
		
		try {
			ComHandler.login("liveSearchTest", "password");
			processes = ComHandler.getProcesses();
			
			assertNotNull(processes);
			
		} catch (IOException e) {
			e.printStackTrace();
			fail("IOException!");
		} 
	}
}
