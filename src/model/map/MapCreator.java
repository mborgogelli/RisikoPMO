package model.map;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
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
    
    /**
	 * Metodo astratto: deve essere implementato per creare una lista di zone a partire da una chiave JSON.
	 * Utilizza la interfaccia Factory per garantire la creazione del tipo corretto di IZone.
	 *
	 * @param key La chiave JSON da cui estrarre le zone
	 * @param jsonMap L'oggetto JSON contenente le informazioni sulle zone
	 * @param factory La factory per creare le istanze di IZone
	 * @return Una lista di IZone create dalla chiave JSON
	 */
    abstract List<IZone> createZone(String key, JsonObject jsonMap, ZoneFactory factory);
    
	/**
     * Inserisce nella mappa tutte le zone di tipo IZone ricavate dalla chiave JSON specificata.
     * Usa la interfaccia Factory per garantire la creazione del tipo corretto.
     */
	List<IZone> insertZoneByKey(List<String> zones, ZoneFactory factory) {
		List<IZone> zoneSet = new ArrayList<IZone>();
		for(String str : zones) {
			IZone zone = factory.createZone(str);
			zoneSet.add(zone);
		}
		return zoneSet;
	}
	
	/**
	 * Ritorna una lista di JsonElement a partire da una chiave dell'oggetto json.
	 * Gestisce sia JsonObject che JsonArray.
	 *
	 * @return Una lista contenente JsonElement
	 **/
	List<JsonElement> getValues(String rootKey, JsonElement jsonMap){
		if (jsonMap == null || jsonMap.isJsonNull() || jsonMap.isJsonPrimitive()) {
			throw new IllegalArgumentException("Parameter is null or JsonNull or JsonPrimitive");
		}
		List<JsonElement> list = extractValuesFromElement(rootKey, jsonMap).collect(Collectors.toList());
		this.checkOutputList(list);
		return list;
	}
	
	/**
	 * Ritorna una lista di JsonElement a partire da una chiave e da una lista di JsonElement.
	 * Gestisce sia liste di JsonObject che di JsonArray.
	 *
	 * @return Una lista contenente JsonElement
	 **/
	List<JsonElement> getValues(String rootKey, List<JsonElement> jsonMap){
		this.checkInputList(jsonMap);
		List<JsonElement> list = new ArrayList<>();
		if (jsonMap.get(0).isJsonArray() || jsonMap.get(0).isJsonObject()) {
			list = this.getValueFromList(rootKey, jsonMap);
		} else {
			throw new IllegalArgumentException("Parameter must contain JsonObject or JsonArray");
		}
		this.checkOutputList(list);
		return list;
	}
	
	/**
	 * Ritorna una lista di valori di tipo T a partire da una chiave dell'oggetto json.
	 * Gestisce sia JsonObject che JsonArray.
	 *
	 * @param rootKey La chiave da verificare
	 * @param jsonMap La lista in cui cercare la chiave (gestisce sia JsonObject che JsonArray)
	 * @return Una lista contenente valori di tipo T
	 **/
	<T> List<T> getValues(String rootKey, List<JsonElement> jsonMap, Class<T> myClass) {
		List<JsonElement> elements = this.getValues(rootKey, jsonMap);
		List<T> result = new ArrayList<>();
		if (elements.get(0).isJsonPrimitive()) {
				result = convertJsonPrimitiveList(elements, myClass);
		} else {
			throw new IllegalArgumentException("Cannot convert elements to " + myClass.getSimpleName());
		}
		return result;
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
		Boolean isValid = true;
		if (!json.has(key) || json.get(key).isJsonNull()) {
			isValid = false;
			throw new IllegalArgumentException(key + " is not a valid key in the JSON object, or its value is null.");
		}
		return isValid;
	}
	
	/**
	 * Controlla la validità di una lista di JsonElement.
	 * Lancia un'eccezione se la lista è null, vuota, contiene elementi di tipi diversi,
	 * o se gli elementi sono JsonPrimitive o JsonNull.
	 *
	 * @param list La lista di JsonElement da controllare
	 * @throws IllegalArgumentException se la lista non è valida
	 */
	private void checkInputList(List<JsonElement> list) {
		this.checkOutputList(list);
		if (list.get(0).isJsonPrimitive()) {
			throw new IllegalArgumentException("Elements of List " + list + " are JsonPrimitive. Cannot use provided key.");
		}
	}
	
	/**
	 * Controlla la validità di una lista di JsonElement.
	 * Lancia un'eccezione se la lista è null, vuota, contiene elementi di tipi diversi,
	 * o se gli elementi sono JsonNull.
	 *
	 * @param list La lista di JsonElement da controllare
	 * @throws IllegalArgumentException se la lista non è valida
	 */
	private void checkOutputList(List<JsonElement> list) {
		if (list == null) {
			throw new IllegalArgumentException("List is null.");
		} else if (list.isEmpty()){
			throw new IllegalArgumentException("List is empty.");
		} else if (!checkSameType(list)){
			throw new IllegalArgumentException("Elements of List are not of the same type.");
		} else if (list.get(0).isJsonNull()) {
			throw new IllegalArgumentException("Elements of List are JsonNull.");
		}
	}
	
	/**
	 * Converte una lista di JsonElement in una lista di tipo T.
	 * 
	 * @param list La lista di JsonElement da convertire
	 * @param myClass La classe di tipo T in cui convertire gli elementi
	 * @return Una lista di tipo T
	 * @throws IllegalArgumentException se gli elementi non possono essere convertiti al tipo specificato
	 */
    private <T> List<T> convertJsonPrimitiveList(List<JsonElement> list, Class<T> myClass) {
    	List<T> result = new ArrayList<>();
    	JsonPrimitive elem = list.get(0).getAsJsonPrimitive();
    	if (myClass == String.class && elem.isString()) {
			result = list.stream().map(e -> myClass.cast(e.getAsString())).collect(Collectors.toList());
		} else if (myClass == Integer.class && elem.isNumber()) {
			result = list.stream().map(e -> myClass.cast(e.getAsInt())).collect(Collectors.toList());
		} else if (myClass == Double.class && elem.isNumber()) {
			result = list.stream().map(e -> myClass.cast(e.getAsDouble())).collect(Collectors.toList());	
		} else if (myClass == Boolean.class) {
			result = list.stream().map(e -> myClass.cast(e.getAsBoolean())).collect(Collectors.toList());
		} else if (myClass == JsonPrimitive.class) {
			result = list.stream().map(e -> myClass.cast(e.getAsJsonPrimitive())).collect(Collectors.toList());
		} else {
			throw new IllegalArgumentException("Elements cannot be cast to " + myClass.getSimpleName());
		}
		return result;
	}
	
	/**
	 * Controlla se tutti gli elementi della lista sono dello stesso tipo.
	 * 
	 * @param jsonElements La lista di JsonElement da controllare
	 * @return true se tutti gli elementi sono dello stesso tipo, false altrimenti
	 */
	private boolean checkSameType(List<JsonElement> jsonElements) {
		Class<?> firstType = jsonElements.get(0).getClass();
		return jsonElements.stream().allMatch(e -> e.getClass().equals(firstType));
	}
	
	/**
	 * Ritorna una lista di JsonElement a partire da una chiave dell'oggetto json
	 * in un data Lista di JsonElement.
	 *
	 * @return Una lista contenente JsonElement
	 **/
	private List<JsonElement> getValueFromList(String rootKey, List<JsonElement> jsonMap) {
	    return jsonMap.stream()
	            .flatMap(element -> extractValuesFromElement(rootKey, element))
	            .collect(Collectors.toList());
	}
	
	/**
	 * Estrae i valori da un JsonElement in base alla chiave specificata.
	 * Gestisce sia JsonObject che JsonArray.
	 *
	 * @param rootKey La chiave da cercare
	 * @param element L'elemento JSON da cui estrarre i valori
	 * @return Uno stream di JsonElement corrispondenti alla chiave
	 */
	private Stream<JsonElement> extractValuesFromElement(String rootKey, JsonElement element) {
		Stream<JsonElement> stream;
		if (element.isJsonObject()) {
	        stream = getValueByKey(rootKey, element.getAsJsonObject()).stream();
	    } else if (element.isJsonArray()) {
	        stream = getValueByKey(rootKey, element.getAsJsonArray()).stream();
	    } else {
	        stream = Stream.empty();
	    }
		return stream;
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
	    return Stream.ofNullable(rootKey)
	            .filter(key -> isValidKey(key, jsonObject))
	            .map(jsonObject::get)
	            .collect(Collectors.toCollection(ArrayList::new));
	}
	
	// Interfaccia factory da passare per la creazione delle istanze di IZone
	@FunctionalInterface
	interface ZoneFactory {
		IZone createZone(String name);
	}
}