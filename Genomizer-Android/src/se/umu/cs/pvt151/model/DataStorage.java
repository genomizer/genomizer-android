package se.umu.cs.pvt151.model;

import java.util.ArrayList;
import java.util.HashMap;

public class DataStorage {
	
	
	//Hashmap which stores arraylists with GeneFile objects
	private static HashMap<String, ArrayList<GeneFile>> fileMap = 
			new HashMap<String, ArrayList<GeneFile>>();
	
	
	public static void appendFileList(String key, ArrayList<GeneFile> files) {
		fileMap.put(key, files);
	}
	
	public static ArrayList<GeneFile> getFileList(String key) {
		return fileMap.get(key);
	}

}
