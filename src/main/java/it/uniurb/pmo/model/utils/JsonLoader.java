package it.uniurb.pmo.model.utils;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

/**
 * Classe JsonLoader per il caricamento delle mappe di gioco in formato JSON.
 * Questa classe fornisce un metodo statico per caricare una mappa di gioco specificata da una versione del gioco.
 * Utilizza la libreria Gson per il parsing del file JSON e gestisce le eccezioni relative a file mancanti,
 * JSON malformati o versioni di gioco invalide.
 */
public final class JsonLoader {
	
	private static final String PATH = "src/main/resources/asset/";
	private static final String FILE_SUFFIX = ".json";
	private static final Gson GSON = new Gson();

	private JsonLoader() {
	}

	/**
	 * Carica e restituisce il contenuto di una mappa di gioco in formato JSON per una specifica versione di gioco.
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
	public static JsonObject loadJsonFile(String gameVersion) throws IOException {
		
        if (gameVersion == null || gameVersion.trim().isEmpty()) {
            throw new IllegalArgumentException("Game version is required");
        }
		
		String path = buildPath(gameVersion);
		
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
	private static String buildPath(String gameVersion) {
		return PATH + gameVersion.toLowerCase() + FILE_SUFFIX;
	}
}
