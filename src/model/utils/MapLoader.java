package model.utils;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class MapLoader {
	
	private final String path = "asset/";
	private final String mapSuffix = "_map.json";
	
	public JsonObject loadMapFile(String gameVersion) throws FileNotFoundException {
		
		String map = this.path + gameVersion + this.mapSuffix;
		FileReader fileReader;
		
		try {
			fileReader = new FileReader(map);
		
		} catch(FileNotFoundException ex) {
			throw new FileNotFoundException("File " + map + " non trovato.");
		}
		
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		Gson jsonMap = new Gson();
		return jsonMap.fromJson(bufferedReader, JsonObject.class);
	}
}
