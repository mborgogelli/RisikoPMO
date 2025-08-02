package model.map;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import model.utils.MapLoader;
import model.utils.ZoneTypeClassic;

class MapCreatorRisikoClassic extends MapCreator {
	
	private final static String GAMEVERSION = "risikonew";
	private static MapCreatorRisikoClassic instance;
	
	private Map<Continent, List<IZone>> map;
	private List<IZone> continents;
	private JsonObject jsonObject;
	private List<JsonElement> mainArray;
	
	private MapCreatorRisikoClassic() {
		this.loadMap(GAMEVERSION);
		this.mainArray = super.getValues(ZoneTypeClassic.CONTINENTS.getDescrizione(), this.jsonObject);
	}
	
	static MapCreatorRisikoClassic getInstance() {
		if (instance == null) {
			instance = new MapCreatorRisikoClassic();
		}
		return instance;
	}
	
	@Override
	protected void loadMap(String gameVersion) {
		try {
			this.jsonObject = MapLoader.loadMapFile(gameVersion);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	void createMap() {
		this.createContinents();
		this.insertTerritories();
	}
	
	@Override
	List<IZone> getMap() {
		return this.continents;
	}
	
	JsonObject getJsonObject() {
		return this.jsonObject;
	}
	
	private void createContinents() {
		this.continents = super.createZones("name", this.mainArray, Continent::new);
	}
	
	private List<JsonElement> getTerritoriesFromJson() {
		return super.getValues("territories", this.mainArray);
	}
	
	private void insertTerritories() {
		for(int i = 0; i < this.continents.size(); i++) {
			List<JsonElement> continentTerritories = super.getValues("territories", this.mainArray);
			List<JsonElement> territories = List.of(continentTerritories.get(i));
			List<IZone> zones = (super.createZones("name", territories, Territory::new));
			System.out.println("Continent: " + this.continents.get(i).toString());
			this.continents.get(i).setChildZones(zones);
		}
	}


}
