package model.board.risikoclassic;

import java.util.List;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import model.board.BoardCreator;
import model.board.IGameBoard;
import model.board.IZone;
import model.utils.GameVersion;
import model.utils.IEnumRisiko;

import static model.utils.EnumRisikoClassic.*;

public class BoardCreatorRisikoClassic extends BoardCreator {
	
	private static BoardCreatorRisikoClassic instance;
	
	private JsonObject jsonObject;
	private List<IZone> continents;
	private List<JsonElement> jsonMap;
	
	/**
	 * Costruttore privato per implementare il pattern Singleton.
	 * protected solo per la classe TestBoardCreator.
	 * Carica la mappa dal file JSON specificato nella versione del gioco.
	 */
	protected BoardCreatorRisikoClassic() {
		this.jsonObject = super.loadMap(GameVersion.RISIKOCLASSIC);
		this.jsonMap = this.getContinentsAsList();
		this.createMap();
	}
	
	public static BoardCreatorRisikoClassic getInstance() {
		if (instance == null) {
			instance = new BoardCreatorRisikoClassic();
		}
		return instance;
	}
	
	@Override
	public IGameBoard getMap() {
		return new GameBoardRisikoClassic(this.continents);
	}
	
	@Override
	protected void createMap() {
		this.createContinents();
		this.insertTerritories();
	}
	
	/**
	 * Restituisce l'oggetto JsonObject caricato.
	 * Questo oggetto contiene i dati della mappa in formato JSON.
	 * 
	 * @return JsonObject che rappresenta la mappa caricata
	 */
	protected JsonObject getLoadedJson() {
		return this.jsonObject;
	}
	
	/**
	 * Restituisce la lista di continenti come JsonElement.
	 * I continenti sono ottenuti dal file JSON caricato.
	 * 
	 * @return Lista di JsonElement che rappresentano i continenti
	 */
	private List<JsonElement> getContinentsAsList() {
		return super.splitJsonArray(super.getValues(CONTINENTS.getDescrizione(), this.jsonObject)
				.get(0).getAsJsonArray());
	}
	
	/**
	 * Crea le zone di tipo Continente e le inserisce nella lista continents.
	 * Le zone vengono create a partire dal file JSON caricato.
	 * Imposta anche i valori di armata per ogni continente.
	 * 
	 */
	private void createContinents() {
		this.continents = super.createZones("name", this.jsonMap, Continent::new);
		this.setArmyValues(CONTINENTS);
	}
	
	private List<IZone> createTerritories(List<JsonElement> continentTerritories) {
		List<IZone> zones = (super.createZones("name", continentTerritories, Territory::new));
		this.setArmyValues(TERRITORIES);
		return zones;
	}
	
	/**
	 * Restituisce la lista di territori associati a ciascun continente.
	 * I territori sono ottenuti dal file JSON caricato.
	 * 
	 * @return Lista di JsonElement che rappresentano i territori
	 */
	private List<JsonElement> getTerritoriesFromJson() {
		return super.getValues("territories", this.jsonMap);
	}
	
	/**
	 * Imposta i valori di armata per ogni continente.
	 * I valori sono ottenuti dal file JSON caricato.
	 * 
	 */
	private void setArmyValues(IEnumRisiko zoneType) {
		List<Integer> armyValues = super.getValues("army", this.jsonMap, Integer.class);
		for(int i = 0; i < this.continents.size(); i++) {
			this.continents.get(i).setValue(armyValues.get(i));
		}
	}
	
	/**
	 * Inserisce i territori all'interno dei rispettivi continenti.
	 * I territori sono ottenuti dal file JSON caricato e associati ai continenti.
	 * 
	 */
	private void insertTerritories() {
		List<JsonElement> allTerritories = this.getTerritoriesFromJson();
		for(int i = 0; i < this.continents.size(); i++) {
			List<JsonElement> continentTerritories = List.of(allTerritories.get(i));
			List<IZone> zones = this.createTerritories(continentTerritories);
			this.continents.get(i).setChildZones(zones);
		}
	}




}