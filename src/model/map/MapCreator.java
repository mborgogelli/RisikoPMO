package model.map;

import java.util.ArrayList;
/**
* Classe astratta che offre il comportamento di base per costruire una mappa di gioco a partire da un file JSON.
* <p>
* Questa classe serve come “scheletro” per tutte le varianti di giochi: fornisce metodi utili per creare e gestire
* la struttura della mappa, lasciando alle sottoclassi il compito di definire come popolare i dettagli specifici
* (ad esempio le adiacenze o altri dati particolari).
* <p>
*/
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
abstract class MapCreator {

	
	MapCreator() {
	}
	
	 /** Metodo astratto: deve essere implementato per creare la mappa specifica */
    abstract void createMap();
    
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
        List<String> set = new ArrayList<>();
        JsonArray array = jsonMap.getAsJsonArray(key);
        for (JsonElement element : array) {
        	if(element.isJsonObject()) {
        		JsonObject obj = element.getAsJsonObject();
        		String name = obj.get("name").getAsString();
            	set.add(name);
        	} else {
        		throw new IllegalStateException(element + " is not a jsonObject");
        	}
        }
        return set;
    }
	
	Set<JsonElement> getElementsByArray(JsonArray array){
		Set<JsonElement> jsonSet = new HashSet<>();
		for(JsonElement elem : array) {
			jsonSet.add(elem);
		}
		return jsonSet;
	}
	
	// Interfaccia factory da passare per la creazione delle istanze di IZone
	@FunctionalInterface
	interface ZoneFactory {
		IZone createZone(String name);
	}
}

	
