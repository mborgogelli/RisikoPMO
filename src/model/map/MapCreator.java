package model.map;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

/**
* Classe astratta che offre il comportamento di base per costruire una mappa di gioco a partire da un file JSON.
* Questa classe serve come “scheletro” per tutte le varianti di giochi: fornisce metodi utili per creare e gestire
* la struttura della mappa, lasciando alle sottoclassi il compito di definire come popolare i dettagli specifici
* (ad esempio le adiacenze o altri dati particolari).
* 
*/
abstract class MapCreator {

	 /** Metodo astratto: deve essere implementato per creare la mappa specifica */
    abstract void createMap();
    
    /** Metodo astratto: deve essere implementato per restituire la mappa al MapManager */
    abstract void getMap();
    
    List<IZone> createZone(String key, JsonObject jsonMap, ZoneFactory factory) {
		List<String> elements = this.getListFromJson(key, jsonMap);
		return this.insertZoneByKey(elements, factory);
    }
    
	/**
     * Inserisce nella mappa tutte le zone di tipo IZone ricavate dalla chiave JSON specificata.
     * Usa la interfaccia Factory per garantire la creazione del tipo corretto.
     */
	private List<IZone> insertZoneByKey(List<String> zones, ZoneFactory factory) {
		List<IZone> zoneSet = new ArrayList<IZone>();
		for(String str : zones) {
			IZone zone = factory.createZone(str);
			zoneSet.add(zone);
		}
		return zoneSet;
	}
	
	 /**
     * Ritorna una lista di nomi a partire da una chiave dell'oggetto json.
     *
     * @return Una lista contenente nomi
     **/
	List<String> getListFromJson(String key, JsonObject jsonMap) {
        List<String> list = new ArrayList<>();
        JsonArray array = jsonMap.getAsJsonArray(key);
        for (JsonElement element : array) {
        	if(element.isJsonObject()) {
        		JsonObject obj = element.getAsJsonObject();
        		String name = obj.get("name").getAsString();
            	list.add(name);
        	} else {
        		throw new IllegalStateException(element + " is not a jsonObject");
        	}
        }
        return list;
    }
	
	/*public List<JsonObject> getListByKey(String extKey, String inKey, JsonObject jsonMap) {
	    return Optional.ofNullable(jsonMap)
	        .filter(map -> map.has(extKey))
	        .map(map -> map.get(extKey))
	        .filter(JsonElement::isJsonArray)
	        .map(JsonElement::getAsJsonArray)
	        .map(array -> extractJsonObjects(array, inKey, extKey.equals(inKey)))
	        .orElse(new ArrayList<>());
	}

	private List<JsonObject> extractJsonObjects(JsonArray array, String inKey, boolean directExtraction) {
	    List<JsonObject> list = new ArrayList<>();
	    
	    for (JsonElement element : array) {
	        if (!element.isJsonObject()) {
	            continue; // Skip non-object elements
	        }
	        
	        JsonObject obj = element.getAsJsonObject();
	        
	        if (directExtraction) {
	            list.add(obj);
	        } else {
	            Optional.ofNullable(obj.get(inKey))
	                .filter(JsonElement::isJsonObject)
	                .map(JsonElement::getAsJsonObject)
	                .ifPresent(list::add);
	        }
	    }
	    
	    return list;
	}*/
	List<String> getListByKey(String extKey, String inKey, JsonObject jsonMap) {
        List<String> list = new ArrayList<>();
        JsonArray array = jsonMap.getAsJsonArray(extKey);
        if(extKey.equals(inKey)) {
        	getListFromJson(extKey, jsonMap);
        }else {	
	        for (JsonElement element : array) {
	        	System.out.println(element);
	        	if(element.isJsonArray()) {
	        		JsonArray obj = element.getAsJsonArray();
	        		System.out.println(obj);
	        		//String obj2= obj.get(inKey).getAsString();
	            	//list.add(obj2);
	        	} else {
	        		throw new IllegalStateException(element + " is not a jsonObject");
	        	}
	        }
        }
        return list;
    }
	
	boolean isValidKey(String key, JsonObject json) {
		return json.keySet().contains(key);
	}
	
	
	private String getJsonElementType(JsonElement element) {
	    if (element.isJsonObject()) return "OBJECT";
	    if (element.isJsonArray()) return "ARRAY";
	    if (element.isJsonPrimitive()) return "PRIMITIVE";
	    if (element.isJsonNull()) return "NULL";
	    return "UNKNOWN";
	}
	
	List<JsonElement> getValue(String rootKey, JsonElement jsonMap){
		List<JsonElement> list = new ArrayList<>();
		switch(jsonMap) {
			case JsonObject jsonObject -> {
				if(isValidKey(rootKey, jsonObject))
					list.add(jsonObject.get(rootKey));
			}
			case JsonArray jsonArray -> {
	            for (JsonElement element : jsonArray) {
	                list.addAll(getValue(rootKey, element));
	            }
	        }
	        case JsonPrimitive jsonPrimitive -> {
	            // Handle primitive case
	        }
	        case JsonNull jsonNull -> {
	            // Handle null case
	        }
	        default -> {
	            // Handle any other cases
	        }
		}	
		return list;
	}
	
	// Interfaccia factory da passare per la creazione delle istanze di IZone
	@FunctionalInterface
	interface ZoneFactory {
		IZone createZone(String name);
	}
}

	
