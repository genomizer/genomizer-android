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

		/*for (int i = 0; i < files.size(); i++) {
			Log.d("HEJ", "add files: " + files.get(i).getName());
		}*/
		//ArrayList<GeneFile> currentFiles = fileMap.get(key);
		fileMap.put(key, files);
		Log.d("HEJ", "added file " + files.size());

		/*if (currentFiles == null) {
			fileMap.put(key, files);
		} else {
			for (int i = 0; i < files.size(); i++) {
				GeneFile file = files.get(i);
				currentFiles.add(file);
				Log.d("HEJ", "added file " + files.size());
			}
			fileMap.put(key, currentFiles);
		}

		for (int i = 0; i < fileMap.get(key).size(); i++) {
			Log.d("HEJ", "Efter add files: " + fileMap.get(key).get(i).getName());
		}*/
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
