package model.map;

import java.util.ArrayList;
import java.util.List;
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
	
	private boolean isValidKey(String key, JsonObject json) {
		return (json.has(key) && !json.get(key).isJsonNull());
	}
	
	List<String> getStringList (String key, JsonObject jsonMap) {
		List<String> values = new ArrayList<>();
		if (isValidKey(key, jsonMap)) {
			JsonElement element = jsonMap.get(key);
			if (element.isJsonArray()) {
				JsonArray array = element.getAsJsonArray();
				for (JsonElement item : array) {
					if (!item.isJsonNull() && item.isJsonPrimitive()) {
						values.add(item.getAsString());
					}
				}
			} else if (element.isJsonPrimitive()) {
				values.add(element.getAsString());
			}
		}
		return values;
	}
	
	
	/**
	 * Ritorna una lista di JsonElement a partire da un array json.
	 *
	 * @return Una lista contenente JsonElement
	 **/
	private List<JsonElement> getElements(JsonArray jsonArray) {
		List<JsonElement> list = new ArrayList<>();
		for (JsonElement element : jsonArray) {
			if(element.isJsonObject() && !element.isJsonNull()) {
				JsonObject obj = element.getAsJsonObject();
				list.add(obj);
			} else if(element.isJsonPrimitive()) {
				list.add(element.getAsJsonPrimitive());
			} else {
				throw new IllegalStateException(element + " is not a jsonObject or jsonPrimitive");
			}
		}
		return list;
	}
	
	/**
	 * Ritorna una lista di JsonElement a partire da una chiave dell'oggetto json
	 * in un dato Array di json.
	 *
	 * @return Una lista contenente JsonElement
	 **/
	private List<JsonElement> getValueByKey(String key, JsonArray jsonArray) {
        List<JsonElement> list = new ArrayList<>();
		for (JsonElement element : jsonArray) {
        	if(element.isJsonObject() && isValidKey(key, element.getAsJsonObject())) {
				JsonObject obj = element.getAsJsonObject();
				list.add(obj.get(key));
        	} else {
        		throw new IllegalStateException(element + " is not a jsonObject");
        	}
        }
        return list;
    }
	
	
	private List<JsonElement> getValueByKey(String rootKey, JsonObject jsonObject){
		List<JsonElement> list = new ArrayList<>();
		if(isValidKey(rootKey, jsonObject)) {
			if(jsonObject.get(rootKey).isJsonArray()) {
				JsonArray array = jsonObject.get(rootKey).getAsJsonArray();
				list = this.getElements(array);
			}else {
				list.add(jsonObject.get(rootKey));
			}
		}else {
			throw new IllegalStateException("Key " + rootKey + " not found or is null in the provided JsonObject.");
		}
		return list;
		
	}
	
	List<JsonElement> getValue(String rootKey, JsonElement jsonMap){
		List<JsonElement> list = new ArrayList<>();
		switch(jsonMap) {
				case JsonObject jsonObject -> this.getValueByKey(rootKey, jsonObject).forEach(list::add);
				case JsonArray jsonArray -> this.getValueByKey(rootKey, jsonArray).forEach(list::add);
				case JsonPrimitive jsonPrimitive -> list.add(jsonPrimitive.getAsJsonPrimitive());
		        default -> {
		        	throw new IllegalStateException("Unexpected value: " + jsonMap.getClass());
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