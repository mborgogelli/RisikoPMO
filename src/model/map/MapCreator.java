package model.map;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Spliterator;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

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
		List<String> elements = this.getStringList(key, jsonMap);
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
	
	//TO DO
	protected List<String> getStringList (String key, JsonObject jsonMap) {
		List<String> values = new ArrayList<>();
		if (isValidKey(key, jsonMap)) {
			JsonElement element = jsonMap.get(key);
			if (element.isJsonArray()) {
				JsonArray array = element.getAsJsonArray();
				for (JsonElement item : array) {
					if (!item.isJsonNull() && item.isJsonPrimitive() && item.getAsJsonPrimitive().isString()) {
						values.add(item.getAsString());
					}
				}
			} else if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isString()) {
				values.add(element.getAsString());
			}
		}
		return values;
	}
	
	/**
	 * Verifica se una chiave è valida in un oggetto JSON.
	 * Una chiave è considerata valida se esiste nell'oggetto e il suo valore non è null.
	 * Se la chiave non è valida o il suo valore è null, viene lanciata un'eccezione.
	 *
	 * @param key La chiave da verificare
	 * @param json L'oggetto JSON in cui cercare la chiave
	 * @return true se la chiave è valida
	 * @throws IllegalArgumentException se la chiave non è valida
	 */
	private boolean isValidKey(String key, JsonObject json) {
		if (!json.has(key) || json.get(key).isJsonNull()) {
			throw new IllegalArgumentException(key + " is not a valid key in the JSON object, or its value is null.");
		}
		return true;
	}
	
	/**
	 * Ritorna uno stream di JsonElement a partire da un JsonElement.
	 * Se l'elemento è un JsonArray, ritorna gli elementi al suo interno.
	 *
	 * @return Uno stream di JsonElement
	 **/
	private Stream<JsonElement> expandElement(JsonElement element) {
	    if (element.isJsonArray()) {
	        return StreamSupport.stream(element.getAsJsonArray().spliterator(), false)
	            .filter(e -> !e.isJsonNull())
	            .filter(e -> e.isJsonObject() || e.isJsonPrimitive());
	    } else {
	        return Stream.of(element);
	    }
	}
	
	/**
	 * Ritorna una lista di JsonElement a partire da una chiave dell'oggetto json
	 * in un dato Array di json.
	 *
	 * @return Una lista contenente JsonElement
	 **/
	private List<JsonElement> getValueByKey(String key, JsonArray jsonArray) {
		return StreamSupport.stream(jsonArray.spliterator(),false)
        		             .filter(JsonElement::isJsonObject)
        		             .map(JsonElement::getAsJsonObject)
        		             .filter(obj -> isValidKey(key, obj))
        		             .map(obj -> obj.get(key))
        		             .collect(Collectors.toList());
    }
	
	/**
	 * Ritorna una lista di JsonElement a partire da una chiave dell'oggetto json
	 * in un dato oggetto json.
	 *
	 * @return Una lista contenente JsonElement
	 **/
	private List<JsonElement> getValueByKey(String rootKey, JsonObject jsonObject){
	    return Optional.ofNullable(rootKey)
	            .filter(key -> isValidKey(key, jsonObject))
	            .map(jsonObject::get)
	            .stream()
	            .flatMap(this::expandElement)
	            .collect(Collectors.toList());
	}
	
	/**
	 * Ritorna una lista di JsonElement a partire da una chiave dell'oggetto json.
	 * Gestisce sia JsonObject che JsonArray.
	 *
	 * @return Una lista contenente JsonElement
	 **/
	protected List<JsonElement> getValue(String rootKey, JsonElement jsonMap){
		List<JsonElement> list = new ArrayList<>();
		switch(jsonMap) {
				case JsonObject jsonObject -> this.getValueByKey(rootKey, jsonObject).forEach(list::add);
				case JsonArray jsonArray -> this.getValueByKey(rootKey, jsonArray).forEach(list::add);
		        default -> {
		        	throw new IllegalStateException("Unexpected value: " + jsonMap.getClass());
		        }
		}
		return list;
	}
	
	// Interfaccia factory da passare per la creazione delle istanze di IZone
	@FunctionalInterface
	protected interface ZoneFactory {
		IZone createZone(String name);
	}
}