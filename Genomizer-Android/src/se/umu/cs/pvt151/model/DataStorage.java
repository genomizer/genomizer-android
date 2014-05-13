package se.umu.cs.pvt151.model;

import java.util.ArrayList;
import java.util.HashMap;

public class DataStorage {


	//Hashmap which stores arraylists with GeneFile objects
	private static HashMap<String, ArrayList<GeneFile>> fileMap = 
			new HashMap<String, ArrayList<GeneFile>>();
	private static ArrayList<GeneFile> rawData = new ArrayList<GeneFile>();
	private static ArrayList<GeneFile> profileData = new ArrayList<GeneFile>();


	public static void appendFileList(String key, ArrayList<GeneFile> files) {
		ArrayList<GeneFile> currentFiles = fileMap.get(key);

		if (currentFiles == null) {
			fileMap.put(key, files);
		} else {
			currentFiles.addAll(files);
			fileMap.put(key, currentFiles);
		}
	}

	public static ArrayList<GeneFile> getFileList(String key) {
		ArrayList<GeneFile> list = fileMap.get(key);
		if (list == null) {
			return new ArrayList<GeneFile>();
		}
		return list;
	}

	public static void setRawDataFiles(ArrayList<GeneFile> raw) {
		rawData = raw;
	}

	public static ArrayList<GeneFile> getRawDataFiles() {
		return rawData;
	}

	public static void setProfileDataFiles(ArrayList<GeneFile> profile) {
		profileData = profile;
	}

	public static ArrayList<GeneFile> getProfileDataFiles() {
		return profileData;
	}

}
