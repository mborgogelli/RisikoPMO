package model.board.risikoclassic;

import java.io.IOException;
import java.util.List;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import model.board.BoardCreator;
import model.board.IZone;
import model.utils.MapLoader;
import model.utils.ZoneTypeClassic;

import static model.utils.ZoneTypeClassic.*;

public class BoardCreatorRisikoClassic extends BoardCreator {
	
	private final static String GAMEVERSION = "risikonew";
	private static BoardCreatorRisikoClassic instance;
	
	private List<IZone> continents;
	private JsonObject jsonObject;
	private List<JsonElement> jsonMap;
	
	protected BoardCreatorRisikoClassic() {
		this.loadMap(GAMEVERSION);
		this.jsonMap = super.splitJsonArray(super.getValues(CONTINENTS.getDescrizione(), this.jsonObject)
							.get(0).getAsJsonArray());
		this.createMap();
	}
	
	public static BoardCreatorRisikoClassic getInstance() {
		if (instance == null) {
			instance = new BoardCreatorRisikoClassic();
		}
		return instance;
	}
	
	@Override
	protected void loadMap(String gameVersion) {
		try {
			this.jsonObject = MapLoader.loadMapFile(GAMEVERSION);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected void createMap() {
		this.createContinents();
		this.insertTerritories();
	}
	
	@Override
	protected List<IZone> getMap() {
		return this.continents;
	}
	
	/**
	 * Restituisce l'oggetto JSON che rappresenta la mappa.
	 * 
	 * @return JsonObject contenente i dati della mappa
	 */
	protected JsonObject getLoadedJson() {
		return this.jsonObject;
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
	private void setArmyValues(ZoneTypeClassic zoneType) {
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
