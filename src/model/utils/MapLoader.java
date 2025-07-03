package model.utils;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public final class MapLoader {
	
	private static final String PATH = "asset/";
	private static final String MAP_SUFFIX= "_map.json";
	
	private MapLoader() {
	}
	
	public static JsonObject loadMapFile(String gameVersion) throws FileNotFoundException {
		
		String map = PATH + gameVersion.toLowerCase() + MAP_SUFFIX;
		FileReader fileReader;
		
		try {
			fileReader = new FileReader(map);
		
		} catch(FileNotFoundException ex) {
			throw new FileNotFoundException("File " + map + " not found.");
		}
		
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		Gson jsonMap = new Gson();
		return jsonMap.fromJson(bufferedReader, JsonObject.class);
	}
}
