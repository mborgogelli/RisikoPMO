package it.uniurb.pmo.model.versions.risikockassic.board;

import java.util.List;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import static it.uniurb.pmo.model.versions.risikockassic.board.EnumMap.*;
import it.uniurb.pmo.model.board.BoardCreator;
import it.uniurb.pmo.model.board.IGameBoard;
import it.uniurb.pmo.model.board.IZone;
import it.uniurb.pmo.model.utils.GameVersion;
import it.uniurb.pmo.model.utils.RiskJsonParser;

/**
 * Classe concreta che estende BoardCreator per creare la mappa del gioco RisikoNew.
 * Implementa il pattern Singleton per garantire che esista una sola istanza della mappa.
 * Utilizza un file JSON per caricare la configurazione della mappa, inclusi continenti,
 * territori, valori di territorio per la modalità "TimeAttack" e adiacenze.
 */
public final class BoardCreatorRisikoNew extends BoardCreator {
	
	private static BoardCreatorRisikoNew instance;
	
	private final JsonObject jsonMap;
	private List<IZone> continents;
	private IGameBoard gameBoard;
	
	/**
	 * Costruttore privato per implementare il pattern Singleton.
	 * NOTA: Protected solo per la classe di test TestBoardCreator.
	 * Carica la mappa dal file JSON specificato nella versione del gioco.
	 */
	private BoardCreatorRisikoNew() {
		super(GameVersion.RISIKONEW);
		this.jsonMap = super.getLoadedMap();
	}
	
	/**
	 * Restituisce l'istanza singleton di BoardCreatorRisikoClassic.
	 * Se l'istanza non esiste, la crea.
	 * 
	 * @return Istanza di BoardCreatorRisikoClassic
	 */
	public static BoardCreatorRisikoNew getInstance() {
		if (instance == null) {
			instance = new BoardCreatorRisikoNew();
		}
		return instance;
	}
	
	@Override
	protected IGameBoard createMap() {
		if (this.gameBoard == null) {
			this.initContinents();
			this.initTerritories();
			this.setNeighbours();
			this.setZoneValue(TERRITORIES);
			this.gameBoard = new GameBoardRisikoNew(this.continents);
		}
		return this.gameBoard;
	}
	
	/**
	 * Crea le zone di tipo Continente e le inserisce nella lista continents.
	 * Le zone vengono create a partire dal file JSON caricato.
	 * Imposta anche i valori di armata per ogni continente.
	 * 
	 */
	private void initContinents() {
		List<JsonElement> continents = this.getContinentsAsList();
		this.continents = super.createZones("name", continents, Continent::new);
		this.setZoneValue(CONTINENTS);
	}
	
	/**
	 * Inserisce i territori all'interno dei rispettivi continenti.
	 * I territori sono ottenuti dal file JSON caricato e associati ai continenti.
	 * 
	 */
	private void initTerritories() {
		List<JsonElement> allTerritories = this.getTerritoriesFromJson();
		for(int i = 0; i < this.continents.size(); i++) {
			List<JsonElement> continentTerritories = List.of(allTerritories.get(i));
			List<IZone> zones = this.createTerritories(continentTerritories);
			IZone continent = this.continents.get(i);
			continent.setChildZones(zones);
			this.setContinentinTerritories(zones, continent);

		}
	}

	private void setContinentinTerritories(List<IZone> territories, IZone continent){
		for(IZone territory : territories) {
			territory.setParentZone(continent);
		}
	}

	/**
	 * Restituisce la lista di continenti come JsonElement.
	 * I continenti sono ottenuti dal file JSON caricato.
	 * 
	 * @return Lista di JsonElement che rappresentano i continenti
	 */
	private List<JsonElement> getContinentsAsList() {
		return super.splitJsonArray(super.getValues(CONTINENTS.getDescrizione(), this.jsonMap)
				.getFirst().getAsJsonArray());
	}
	
	/**
	 * Restituisce la lista di territori associati a ciascun continente.
	 * I territori sono ottenuti dal file JSON caricato.
	 * 
	 * @return Lista di JsonElement che rappresentano i territori
	 */
	private List<JsonElement> getTerritoriesFromJson() {
		List<JsonElement> continents = this.getContinentsAsList();
		return super.getValues("territories", continents);
	}
	
	/**
	 * Crea le zone di tipo Territory a partire dai territori di un continente.
	 * Le zone vengono create utilizzando il metodo createZones della classe padre.
	 * 
	 * @param continentTerritories Lista di JsonElement che rappresentano i territori di un continente
	 * @return Lista di IZone che rappresentano i territori creati
	 */
	private List<IZone> createTerritories(List<JsonElement> continentTerritories) {
        return (super.createZones("name", continentTerritories, Territory::new));
	}
	
	/**
	 * Imposta i valori di armata per i continenti o i territori.
	 * 
	 * @param zoneType Tipo di zona (CONTINENTS o TERRITORIES)
	 */
	private void setZoneValue(EnumMap zoneType) {
	    if (zoneType == CONTINENTS) {
	        setArmyBonusForContinents();
	    } else if (zoneType == TERRITORIES) {
	        setPointsForTerritories();
	    }
	}

	/**
	 * Imposta i valori di armata per tutti i continenti.
	 */
	private void setArmyBonusForContinents() {
	    List<JsonElement> continents = this.getContinentsAsList();
	    List<Integer> armyValues = super.getValues("armybonus", continents, Integer.class);

		// Al continente i-esimo corrisponde il bonus i-esimo
	    for (int i = 0; i < this.continents.size(); i++) {
	        this.continents.get(i).setValue(armyValues.get(i));
	    }
	}

	/**
	 * Imposta i punti per la modalità Time Attack per tutti i territori di ogni continente.
	 */
	private void setPointsForTerritories() {
	    List<JsonElement> allTerritories = this.getTerritoriesFromJson();
	    
	    for (int continentIndex = 0; continentIndex < this.continents.size(); continentIndex++) {
	        setPointsForContinentTerritories(allTerritories, continentIndex);
	    }
	}

	/**
	 * Imposta i valori per la modalità di gioco "TimeAttack" per i territori di un continente specifico.
	 * 
	 * @param allTerritories Lista di tutti i territori dal JSON
	 * @param continentIndex Indice del continente da processare
	 */
	private void setPointsForContinentTerritories(List<JsonElement> allTerritories, int continentIndex) {
	    List<IZone> territories = this.continents.get(continentIndex).getChildZones();

		// Estrae i territori del continente i-esimo dal JSON
	    List<JsonElement> continentTerritories = List.of(allTerritories.get(continentIndex));

		// Estrae il valore per la modalità "TimeAttack" per ogni territorio del continente
	    List<Integer> armyValues = super.getValues("points", continentTerritories, Integer.class);

		// Assegna all' i-esimo territorio l' i-esimo valore per la modalità "TimeAttack"
	    for (int territoryIndex = 0; territoryIndex < territories.size(); territoryIndex++) {
	        territories.get(territoryIndex).setValue(armyValues.get(territoryIndex));
	    }
	}

	/**
	 * Imposta le adiacenze per tutti i territori della mappa.
	 * Per ogni territorio, recupera la lista dei suoi vicini dal JSON e li assegna.
	 */
	private void setNeighbours() {
	    List<JsonElement> allTerritories = this.getTerritoriesFromJson();
	    
	    for (int continentIndex = 0; continentIndex < this.continents.size(); continentIndex++) {
	        setNeighboursForContinent(allTerritories, continentIndex);
	    }
	}

	/**
	 * Imposta le adiacenze per tutti i territori di un continente specifico.
	 * 
	 * @param allTerritories Lista di tutti i territori dal JSON
	 * @param continentIndex Indice del continente da processare
	 */
	private void setNeighboursForContinent(List<JsonElement> allTerritories, int continentIndex) {
	    List<IZone> territories = this.continents.get(continentIndex).getChildZones();
	    List<JsonElement> continentTerritories = List.of(allTerritories.get(continentIndex));
	    List<List<String>> neighboursList = getNeighboursListForContinent(continentTerritories);
	    
	    for (int territoryIndex = 0; territoryIndex < territories.size(); territoryIndex++) {
	        territories.get(territoryIndex).setNeighbours(neighboursList.get(territoryIndex));
	    }
	}

	/**
	 * Estrae la lista dei vicini per tutti i territori di un continente dal JSON.
	 * 
	 * @param continentTerritories Territori del continente in formato JSON
	 * @return Lista di liste contenenti i nomi dei territori vicini
	 */
	private List<List<String>> getNeighboursListForContinent(List<JsonElement> continentTerritories) {
	    return super.getValues("neighbours", continentTerritories).stream()
	        .map(JsonElement::getAsJsonArray)
	        .map(super::splitJsonArray)
	        .map(neighbours -> super.convertJsonPrimitiveList(neighbours, String.class))
	        .toList();
	}
}