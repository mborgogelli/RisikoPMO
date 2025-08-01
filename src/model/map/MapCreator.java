package model.map;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
    
    List<IZone> createZone(String key, JsonObject jsonMap, ZoneFactory factory) {
		return null;
		//return this.insertZoneByKey(elementsString, factory);
    }
    
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
	 * Restituisce una lista generica di JsonElement (JsonObject, JsonArray o JsonPrimitive) a seconda del tipo degli elementi nella lista.
	 *
	 * @param rootKey la chiave da cercare
	 * @param jsonMap la lista di JsonElement
	 * @return una lista di elementi del tipo corretto
	 */
	<T> List<T> getValues(String rootKey, List<JsonElement> jsonMap, Class<T> myClass) {
		checkInputList(jsonMap);
		List<JsonElement> elements = this.getValueFromList(rootKey, jsonMap);
		List<T> result = new ArrayList<>();
		for (JsonElement elem : elements) {
			String type = this.checkType(elem);
			switch (type) {
				case "JSONPRIMITIVE" -> {
					if (myClass == String.class && elem.getAsJsonPrimitive().isString()) {
						result.add(myClass.cast(elem.getAsString()));
					} else if (myClass == Integer.class && elem.getAsJsonPrimitive().isNumber()) {
						result.add(myClass.cast(elem.getAsInt()));
					} else if (myClass == Boolean.class) {
						result.add(myClass.cast(elem.getAsJsonPrimitive()));
					} else if (myClass == JsonPrimitive.class) {
						result.add(myClass.cast(elem.getAsJsonPrimitive()));
					} else {
						throw new IllegalArgumentException("Element " + elem + " cannot be cast to " + myClass.getSimpleName());
					}
				}
				case "JSONOBJECT" -> {
					if (myClass == JsonObject.class) {
						result.add(myClass.cast(elem.getAsJsonObject()));
					} else {
						throw new IllegalArgumentException("Element " + elem + " cannot be cast to " + myClass.getSimpleName());
					}
				}
				case "JSONARRAY" -> {
					if (myClass == JsonArray.class) {
						result.add(myClass.cast(elem.getAsJsonArray()));
					} else {
						throw new IllegalArgumentException("Element " + elem + " cannot be cast to " + myClass.getSimpleName());
					}
				}
				default -> throw new IllegalArgumentException("Element " + elem + " is of unknown type");
			}
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
	
	/** * Controlla il tipo di un JsonElement e ritorna una stringa rappresentativa del tipo.
	 * 
	 * @param elem Il JsonElement da controllare
	 * @return Una stringa che rappresenta il tipo del JsonElement
	 * @throws IllegalArgumentException se l'elemento non è valido
	 */
	private String checkType(JsonElement elem) {
		String str;
		switch(elem) {
			case JsonObject jsonObject -> str = "JSONOBJECT";
			case JsonArray jsonArray -> str = "JSONARRAY";
			case JsonPrimitive jsonPrimitive -> str = "JSONPRIMITIVE";
			default -> {
				throw new IllegalArgumentException("Parameter " + elem + " is not valid");
			}
		}
		return str;
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
	
	/**
	 * Ritorna una lista di stringhe a partire da una Lista di JsonElements.
	 * Gestisce sia JsonObject che JsonArray.
	 *
	 * @return Una lista contenente stringhe
	 **/
	private List<String> getStringList (List<JsonElement> jsonElements) {
		return jsonElements.stream()
				.filter(JsonElement::isJsonPrimitive)
				.filter(e -> e.getAsJsonPrimitive().isString())
				.map(JsonElement::getAsString)
				.collect(Collectors.toList());
	}
	
	/**
	 * Ritorna una lista di interi a partire da una Lista di JsonElements.
	 * Gestisce sia JsonObject che JsonArray.
	 *
	 * @return Una lista contenente stringhe
	 **/
	private List<Integer> getIntList (List<JsonElement> jsonElements) {
		return jsonElements.stream()
				.filter(JsonElement::isJsonPrimitive)
				.filter(e -> e.getAsJsonPrimitive().isNumber())
				.map(e -> e.getAsInt())
				.collect(Collectors.toList());
	}
	
	// Interfaccia factory da passare per la creazione delle istanze di IZone
	@FunctionalInterface
	interface ZoneFactory {
		IZone createZone(String name);
	}
}