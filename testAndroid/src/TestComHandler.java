

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import se.umu.cs.pvt151.com.ComHandler;
import se.umu.cs.pvt151.model.Annotation;
import se.umu.cs.pvt151.model.Experiment;
import se.umu.cs.pvt151.model.GeneFile;
import se.umu.cs.pvt151.model.GenomeRelease;
import se.umu.cs.pvt151.model.ProcessStatus;
import android.util.Log;
import junit.framework.TestCase;

public class TestComHandler extends TestCase {
	private String password = "baguette";
	private String serverURL = "http://dumbledore.cs.umu.se:7000/";
//	"http://genomizer.apiary-mock.com/"
	
	
	public void setUp() {
		ComHandler.setServerURL(serverURL);
		System.out.println("hej");
	}
	
	
	public void testShouldLoginValidUser() {
		try {
			assertTrue(ComHandler.login("sdf", password));
		} catch (IOException e) {
			fail("Could not communicate with the server.");
		}
	}
	
	
	public void testShouldRejectLoginWithWrongPassword() {
		try {
			assertFalse(ComHandler.login("yo", "wrongpassword"));
		} catch (IOException e) {
			fail("Could not communicate with the server.");
		}
	}
	
	//TODO är experimentName ett namn på ett experiment?
	// fungerar inte utan databasen
	public void testSearchPackage() {
		HashMap<String, String> searchValues = new HashMap<String, String>();
		searchValues.put("Species", "Human");
		searchValues.put("Sex", "Male");
		try {
			ComHandler.login("John", password);
			ArrayList<Experiment> experiments = ComHandler.search(searchValues);
			// "hesttest" is the first test on the server, for this search.
			assertEquals("hesttest", experiments.get(0).getName());
		} catch (IOException e) {
			fail("Could not communicate with the server.");
			e.printStackTrace();
		} 
	}
	
	
	public void testSearchOnServer() {
		HashMap<String, String> searchValues = new HashMap<String, String>();
		searchValues.put("Sex", "male");
		searchValues.put("Species", "human");
		try {
			ComHandler.login("liveSearchTest", password);
			ArrayList<Experiment> experiments = ComHandler.search(searchValues);
			assertFalse(experiments.isEmpty());
		} catch (IOException e) {
			Log.d("TestLog", e.getMessage());
			e.printStackTrace();
			fail("Could not communicate with the server.");
		} 
	}
	
	
	public void testAnnotationsPackage() {
		try {
			ComHandler.login("John", password);
			ArrayList<Annotation> experiments = ComHandler.getServerAnnotations();
			Log.d("DEBUG", "ANNOTATIONS");
			Log.d("DEBUG", experiments.toString());
			assertEquals("SplatterKatt", experiments.get(0).getName());
		} catch (IOException e) {
			fail("Could not communicate with the server.");
			e.printStackTrace();
		}
		
	}
	
	
	public void testAnnotationsPackageFromServer() {
		try {
			ComHandler.setServerURL(serverURL);
			
			ComHandler.login("annotationFromServer", password);
			ArrayList<Annotation> experiments = ComHandler.getServerAnnotations();
			assertNotNull(experiments);
		} catch (IOException e) {
			fail("Could not communicate with the server.");
			e.printStackTrace();
		}
		
	}
	
	
	public void testAnnotationsDeconstruct() {
		try {
			ComHandler.login("John", "baguette");
			
			ComHandler.getServerAnnotations();
		} catch (IOException e) {
			fail("Could not communicate with the server.");
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
		ComHandler.setServerURL(serverURL);
		
		HashMap<String, String> searchValues = new HashMap<String, String>();
		searchValues.put("Species", "Human");
		
		ArrayList<Experiment> experiments = new ArrayList<Experiment>();
		
		try {
			ComHandler.login("liveSearchTest", password);
			experiments = ComHandler.search(searchValues);
		} catch (IOException e) {
			Log.d("TestLog", e.getMessage());
			e.printStackTrace();
			fail("Could not communicate with the server.");
		} 
		
		List<GeneFile> files = experiments.get(0).getFiles();
		
		ArrayList<String> parameters = new ArrayList<String>();
		parameters.add("param1");
		parameters.add("param2");
		parameters.add("param3");
		parameters.add("param4");
		
		try {
			boolean result = ComHandler.rawToProfile(files.get(0), parameters, "meta", "randoom");
			
			Log.d("RAW", "Code" + result);
			
		} catch (IOException e) {
			e.printStackTrace();
			fail("Could not create profile from raw data.");
		}
	}
	
	
	public void testGetGenomeReleases() {
		ComHandler.setServerURL(serverURL);
		
		ArrayList<GenomeRelease> gr = new ArrayList<GenomeRelease>();
		
		try {
			ComHandler.login("liveSearchTest", password);
			gr = ComHandler.getGenomeReleases();
			
			assertNotNull(gr);
			
		} catch (IOException e) {
			e.printStackTrace();
			fail("IOException!");
		} 
	}
	
	
	public void testGetProcesses() {
		ComHandler.setServerURL(serverURL);
		
		
		ArrayList<ProcessStatus> processes = new ArrayList<ProcessStatus>();
		
		try {
			ComHandler.login("liveSearchTest", password);
			processes = ComHandler.getProcesses();
			
			assertNotNull(processes);
			
		} catch (IOException e) {
			e.printStackTrace();
			fail("IOException!");
		} 
	}
}
