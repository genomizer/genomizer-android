
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import se.umu.cs.pvt151.com.ComHandler;
import se.umu.cs.pvt151.com.Communicator;
import se.umu.cs.pvt151.com.GenomizerHttpPackage;
import se.umu.cs.pvt151.com.MsgDeconstructor;
import se.umu.cs.pvt151.com.MsgFactory;
import se.umu.cs.pvt151.model.Experiment;
import se.umu.cs.pvt151.model.GeneFile;
import se.umu.cs.pvt151.model.GenomeRelease;
import junit.framework.TestCase;


public class MsgDeconstructorTest extends TestCase {


	public void testCanCreateJSONFromString() {
		JSONObject man = new JSONObject();
		try {
			man.put("head", "hat");
		} catch (JSONException e) {
			fail("Can't put field in JSON");
		}
		String jsonString = man.toString();
		try {
			assertEquals("hat", new JSONObject(jsonString).get("head"));
		} catch (JSONException e) {
			fail("Can't convert from and to JSON properly");
		}
	}


	public void testDeconstructSearchResults() {
		try {
			Communicator.initCommunicator("http://genomizer.apiary-mock.com/");

			HashMap<String, String> annotations = new HashMap<String, String>();
			annotations.put("Species", "Human");

			JSONObject msg = MsgFactory.createRegularPackage();
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


	public void testDeconstructGenomeReleases() {
		try {
			Communicator.initCommunicator("http://genomizer.apiary-mock.com/");

			JSONObject msg = MsgFactory.createRegularPackage();
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



}
