package model.utils;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

/**
 * Utility class finalizzata al caricamento di file mappa in formato JSON relativi a diverse versioni di gioco.
 * <p>
 * La classe fornisce metodi statici per:
 * <ul>
 *   <li>Costruire il percorso del file mappa a partire dal nome della versione del gioco</li>
 *   <li>Effettuare la validazione dell'input</li>
 *   <li>Gestire il caricamento, la lettura e il parsing del file nel formato {@link com.google.gson.JsonObject}</li>
 * </ul>
 * <p>
 * */
public final class MapLoader {
	
	private static final String PATH = "asset/";
	private static final String MAP_SUFFIX= "_map.json";
	private static final Gson GSON = new Gson();
	
	/**
	 * Carica e restituisce il contenuto di una mappa di gioco in formato JSON per una specifica versione di gioco.
	 * 
	 * Il metodo costruisce il percorso del file JSON associato alla variante specificata di gioco,
	 * valida l'input, apre e legge il file utilizzando un approccio try-with-resources per garantire
	 * la corretta chiusura delle risorse. Viene poi eseguito il parsing del contenuto JSON tramite la libreria Gson.
	 * In caso di file mancante, JSON malformato o versione di gioco invalida, vengono sollevate eccezioni specifiche.
	 *
	 * @param gameVersion la versione del gioco di cui caricare la mappa (non può essere null o vuota)
	 * @return un oggetto JsonObject che rappresenta il contenuto della mappa caricata
	 * @throws IllegalArgumentException se la versione del gioco è null o vuota
	 * @throws FileNotFoundException se il file della mappa non viene trovato
	 * @throws IOException se si verificano errori di I/O durante la lettura del file
	 * @throws JsonSyntaxException se il file è vuoto, contiene null oppure il JSON è malformato
	 */
	private MapLoader() {
	}
	
	public static JsonObject loadMapFile(String gameVersion) throws IOException {
		
        if (gameVersion == null || gameVersion.trim().isEmpty()) {
            throw new IllegalArgumentException("Game version is required");
        }
		
		String path = buildMapPath(gameVersion);
		
		try (FileReader fileReader = new FileReader(path)){
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			
			JsonObject mapJson = GSON.fromJson(bufferedReader, JsonObject.class);
			if (mapJson == null) {
                throw new JsonSyntaxException("File " + path + " is empty or contains null");
            }
			
			return mapJson; 
		
		} catch (FileNotFoundException ex) {
				throw new FileNotFoundException("File " + path + " not found.");
		} catch (JsonSyntaxException ex){
				throw new JsonSyntaxException("Json file " + path + " is invalid.");
		}
		
	}
	/** Restituisce il percorso del file JSON associato alla variante specificata di gioco.
	 * 
	* @return il percorso del file Json che rappresenta la mappa
	**/
	private static String buildMapPath(String gameVersion) {
		return PATH + gameVersion.toLowerCase() + MAP_SUFFIX;
	}
}
