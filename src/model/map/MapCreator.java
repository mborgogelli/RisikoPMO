package model.map;

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

	private final JsonObject jsonMap;
	
	MapCreator(JsonObject jsonMap) {
		this.jsonMap = jsonMap;
	}
	
	 /** Metodo astratto: deve essere implementato per creare la mappa specifica */
    abstract void createMap();
    
	/**
     * Inserisce nella mappa tutte le zone di tipo IZone ricavate dalla chiave JSON specificata.
     * Usa la interfaccia Factory per garantire la creazione del tipo corretto.
     */
	Set<IZone> insertZoneByKey(String key, ZoneFactory factory) {
		List<String> element = this.getListFromJson(key);
		Set<IZone> zones = new HashSet<IZone>();
		for(String str : element) {
			IZone zone = factory.createZone(str);
			zones.add(zone);
		}
		return zones;
	}
	
	 /**
     * Ritorna una lista di nomi a partire da una chiave dell'oggetto json.
     *
     * @return Una lista contenente nomi
     **/
	List<String> getListFromJson(String key) {
        List<String> list = new LinkedList<>();
        JsonArray array = this.jsonMap.getAsJsonArray(key);
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
	
	// Interfaccia factory da passare per la creazione delle istanze di IZone
	@FunctionalInterface
	interface ZoneFactory {
		IZone createZone(String name);
	}
}

	
