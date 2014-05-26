package se.umu.cs.pvt151.model;

import java.util.ArrayList;
import java.util.HashMap;

import android.util.Log;

public class DataStorage {


	//Hashmap which stores arraylists with GeneFile objects
	private static HashMap<String, ArrayList<GeneFile>> fileMap = 
			new HashMap<String, ArrayList<GeneFile>>();
	
	private static ArrayList<GeneFile> rawData = new ArrayList<GeneFile>();
	private static ArrayList<GeneFile> profileData = new ArrayList<GeneFile>();
	private static ArrayList<GeneFile> regionData;


	public static void appendFileList(String key, ArrayList<GeneFile> files) {
		fileMap.put(key, files);
	}
	

	public static ArrayList<GeneFile> getFileList(String key) {
		ArrayList<GeneFile> list = fileMap.get(key);
		if (list == null) {
			return new ArrayList<GeneFile>();
		}
		return list;
	}
	

	public static void setRawDataFiles(ArrayList<GeneFile> raw) {
		rawData = new ArrayList<GeneFile>();
		rawData = raw;
	}
	

	public static ArrayList<GeneFile> getRawDataFiles() {
		return rawData;
	}
	

	public static void setProfileDataFiles(ArrayList<GeneFile> profile) {
		profileData = new ArrayList<GeneFile>();
		profileData = profile;
	}
	

	public static ArrayList<GeneFile> getProfileDataFiles() {
		return profileData;
	}
	

	public static ArrayList<GeneFile> getRegionDataFiles() {
		return regionData;
	}
	

	public static void setRegionDataFiles(ArrayList<GeneFile> regionToConv) {
		regionData = regionToConv;
	}
}
