package model.map;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class MapCreator {

	private final Map<Continent,Set<Territory>> gameMap;
	private final JsonObject jsonMap;
	
	public MapCreator(JsonObject jsonMap) {
		this.jsonMap = jsonMap;
		this.gameMap = new HashMap<Continent, Set<Territory>>();
	}
	
	public List<String> getContinentsFromJson() {
        List<String> continents = new LinkedList<>();
        JsonArray array = this.jsonMap.getAsJsonArray("continents");
        for (JsonElement element : array) {
        	if(element.isJsonObject()) {
        		JsonObject obj = element.getAsJsonObject();
        		String continentName = obj.get("name").getAsString();
            	continents.add(continentName);
        	} else {
        		throw new IllegalStateException(element + " is not a jsonObject");
        	}
        }
        return continents;
    }

}
