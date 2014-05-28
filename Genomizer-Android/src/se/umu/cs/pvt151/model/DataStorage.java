package se.umu.cs.pvt151.model;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class is used to store lists of GeneFiles.
 * An arbitrary number of lists may be stored and accessed
 * through the whole application.
 * 
 * @author Rickard dv12rhm
 *
 */
public class DataStorage {

	//Hashmap which stores arraylists with GeneFile objects
	private static HashMap<String, ArrayList<GeneFile>> fileMap = 
			new HashMap<String, ArrayList<GeneFile>>();

	private static ArrayList<GeneFile> rawData = new ArrayList<GeneFile>();
	private static ArrayList<GeneFile> profileData = new ArrayList<GeneFile>();
	private static ArrayList<GeneFile> regionData;


	/**
	 * Appends a new GeneFile ArrayList to the hashmap with the specified
	 * key parameter as key.
	 * 
	 * @param key
	 * @param files
	 */
	public static void appendFileList(String key, ArrayList<GeneFile> files) {
		fileMap.put(key, files);
	}


	/**
	 * Gets the ArrayList of GeneFiles with the specified key.
	 * 
	 * @param key
	 * @return ArrayList<GeneFile>
	 */
	public static ArrayList<GeneFile> getFileList(String key) {
		ArrayList<GeneFile> list = fileMap.get(key);
		if (list == null) {
			return new ArrayList<GeneFile>();
		}
		return list;
	}


	/**
	 * Sets the raw data files of the FileListFragment.
	 * 
	 * @param raw
	 */
	public static void setRawDataFiles(ArrayList<GeneFile> raw) {
		rawData = raw;
	}


	/**
	 * Gets the raw data files of the FileListFragment.
	 * 
	 * @return raw
	 */
	public static ArrayList<GeneFile> getRawDataFiles() {
		return rawData;
	}


	/**
	 * Sets the profile data files of the FileListFragment.
	 * 
	 * @param profile
	 */
	public static void setProfileDataFiles(ArrayList<GeneFile> profile) {
		profileData = profile;
	}


	/**
	 * Gets the profile data files of the FileListFragment.
	 * 
	 * @return profile
	 */
	public static ArrayList<GeneFile> getProfileDataFiles() {
		return profileData;
	}


	/**
	 * Gets the region data files of the FileListFragment.
	 * 
	 * @return region
	 */
	public static ArrayList<GeneFile> getRegionDataFiles() {
		return regionData;
	}


	/**
	 * Sets the region data files of the FileListFragment.
	 * 
	 * @param regionToConv
	 */
	public static void setRegionDataFiles(ArrayList<GeneFile> regionToConv) {
		regionData = regionToConv;
	}
}
