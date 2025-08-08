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
	
	private JsonObject jsonMap;
	private List<IZone> continents;
	
	/**
	 * Costruttore privato per implementare il pattern Singleton.
	 * NOTA: Protected solo per la classe di test TestBoardCreator.
	 * Carica la mappa dal file JSON specificato nella versione del gioco.
	 */
	protected BoardCreatorRisikoClassic() {
		super(GameVersion.RISIKOCLASSIC);
		this.jsonMap = super.getLoadedMap();
	}
	
	/**
	 * Restituisce l'istanza singleton di BoardCreatorRisikoClassic.
	 * Se l'istanza non esiste, la crea.
	 * 
	 * @return Istanza di BoardCreatorRisikoClassic
	 */
	public static BoardCreatorRisikoClassic getInstance() {
		if (instance == null) {
			instance = new BoardCreatorRisikoClassic();
		}
		return instance;
	}
	
	@Override
	protected IGameBoard createMap() {
		this.createContinents();
		this.insertTerritories();
		return new GameBoardRisikoClassic(this.continents);
	}
	
	/**
	 * Restituisce la lista di continenti come JsonElement.
	 * I continenti sono ottenuti dal file JSON caricato.
	 * 
	 * @return Lista di JsonElement che rappresentano i continenti
	 */
	private List<JsonElement> getContinentsAsList() {
		return super.splitJsonArray(super.getValues(CONTINENTS.getDescrizione(), this.jsonMap)
				.get(0).getAsJsonArray());
	}
	
	/**
	 * Crea le zone di tipo Continente e le inserisce nella lista continents.
	 * Le zone vengono create a partire dal file JSON caricato.
	 * Imposta anche i valori di armata per ogni continente.
	 * 
	 */
	private void createContinents() {
		List<JsonElement> continents = this.getContinentsAsList();
		this.continents = super.createZones("name", continents, Continent::new);
		this.setArmyValues(CONTINENTS);
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
		List<IZone> zones = (super.createZones("name", continentTerritories, Territory::new));
		this.setArmyValues(TERRITORIES);
		return zones;
	}
	
	/**
	 * Imposta i valori di armata per i continenti o i territori a seconda del tipo di zona.
	 * Se il tipo di zona è CONTINENTS, imposta i valori per i continenti.
	 * Se il tipo di zona è TERRITORIES, imposta i valori per i territori all'interno dei continenti.
	 * 
	 * @param zoneType Tipo di zona (CONTINENTS o TERRITORIES)
	 */
	private void setArmyValues(IEnumRisiko zoneType) {
		if (zoneType == CONTINENTS) {
			List<JsonElement> continents = this.getContinentsAsList();
			List<Integer> armyValues = super.getValues("army", continents, Integer.class);
			for(int i = 0; i < this.continents.size(); i++) {
				this.continents.get(i).setValue(armyValues.get(i));
			}
		} else if(zoneType == TERRITORIES) {
			List<JsonElement> allTerritories = this.getTerritoriesFromJson();
			for(int i = 0; i < this.continents.size(); i++) {
				List<IZone> territories = this.continents.get(i).getChildZones();
				List<JsonElement> continentTerritories = List.of(allTerritories.get(i));
				List<Integer> armyValues = super.getValues("army", continentTerritories, Integer.class);
				for(int j = 0; j < territories.size(); j++) {
					territories.get(j).setValue(armyValues.get(j));
				}
			}
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