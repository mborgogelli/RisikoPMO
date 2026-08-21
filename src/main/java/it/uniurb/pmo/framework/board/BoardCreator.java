package it.uniurb.pmo.framework.board;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import it.uniurb.pmo.framework.utils.EGameVersion;
import it.uniurb.pmo.framework.utils.RiskJsonLoader;
import it.uniurb.pmo.framework.utils.RiskJsonParser;

/**
* Classe astratta che offre il comportamento di base per costruire una mappa di gioco a partire da un file JSON.
* Questa classe serve come “scheletro” per tutte le varianti di giochi: fornisce metodi utili per creare e gestire
* la struttura della mappa, lasciando alle sottoclassi il compito di definire come popolare i dettagli specifici
* (ad esempio le adiacenze o altri dati particolari).
* 
*/
public abstract class BoardCreator implements IBoardCreator{

	private final static String SUFFIX_MAP = "_map";
	private final JsonObject jsonMap;
	
	/**
	 * Costruttore protetto che carica la mappa dal file JSON in base alla versione del gioco.
	 * 
	 * @param gameVersion La versione del gioco per cui caricare la mappa.
	 */	
	protected BoardCreator(EGameVersion gameVersion) {
		this.jsonMap = this.loadMap(gameVersion);
	}
	
    /** Metodo astratto: deve essere implementato per creare la mappa specifica */
    protected abstract IGameBoard createMap();
    
    /**
	 * Metodo che restituisce la mappa del gioco.
	 * Se la mappa non è stata creata correttamente, lancia un'eccezione.
	 *
	 * @return Un oggetto IGameBoard che rappresenta la mappa del gioco
	 * @throws IllegalStateException se la mappa non è stata creata correttamente
	 */
    @Override
	public IGameBoard getMap() {
		IGameBoard gameBoard = this.createMap();
		if (gameBoard == null) {
			throw new IllegalStateException("Failed to create game board: createMap() returned null");
		}
		return gameBoard;
    }

	// Interfaccia factory da passare per la creazione delle istanze di IZone
	@FunctionalInterface
	public interface ZoneFactory {
		IZone createZone(String name);
	}

	/**
	 * Ritorna un oggetto JsonObject che rappresenta la mappa.
	 *
	 * @return Un oggetto JsonObject che rappresenta la mappa del gioco
	 */
	protected JsonObject getLoadedMap() {
		return this.jsonMap;
	}
	
    /**
	 * Metodo astratto: deve essere implementato per creare una lista di zone a partire da una chiave JSON.
	 * Utilizza la interfaccia Factory per garantire la creazione del tipo corretto di IZone.
	 *
	 * @param rootKey La chiave JSON da cui estrarre le zone
	 * @param jsonMap L'oggetto JSON contenente le informazioni sulle zone
	 * @param factory La factory per creare le istanze di IZone
	 * @return Una lista di IZone create dalla chiave JSON
	 */
    protected List<IZone> createZones(String rootKey, List<JsonElement> jsonMap, ZoneFactory factory) {
	    	List<String> list = RiskJsonParser.getValues(rootKey, jsonMap, String.class);
        return this.insertZoneByKey(list, factory);
	}
	
	/** Caricare la mappa da un file JSON in base alla versione di gioco richiesta.
	 *  Restituisce un oggetto JsonObject che rappresenta la mappa.
	 * 
	 * @param gameVersion La versione del gioco per cui caricare la mappa.
	 * @return Un oggetto JsonObject che rappresenta la mappa del gioco.
     */
	private JsonObject loadMap(EGameVersion gameVersion) {
		JsonObject jsonObject = null;
		try {
			jsonObject = RiskJsonLoader.loadJsonFile(gameVersion.getDescrizione() + SUFFIX_MAP);
		} catch (IOException e) {
			System.err.println("Cannot Load Map: " + e.getMessage());
		}
		return jsonObject;
	}
	
	/**
     * Inserisce nella mappa tutte le zone di tipo IZone ricavate dalla chiave JSON specificata.
     * Usa la interfaccia Factory per garantire la creazione del tipo corretto.
     */
	private List<IZone> insertZoneByKey(List<String> zones, ZoneFactory factory) {
		return zones.stream()
				.map(factory::createZone)
				.collect(Collectors.toList());
	}
}