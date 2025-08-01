package model.map;

import java.io.IOException;
import java.util.List;
import com.google.gson.JsonObject;

import model.utils.MapLoader;
import model.utils.ZoneType;

public class MapCreatorRisikoClassic extends MapCreator {
	
	private List<IZone> continents;
	JsonObject jsonMap;
	
	MapCreatorRisikoClassic(JsonObject jsonObject) {
		this.jsonMap = jsonObject;
	}

	@Override
	void createMap() {
		this.createContinents();
		this.insertTerritories();
	}
	
	@Override
	void getMap() {
		// TODO Auto-generated method stub
		
	}
	private void createContinents() {
		String continents = ZoneType.CONTINENTS.getDescrizione();
	}
	
	private void insertTerritories() {
		String territories = ZoneType.TERRITORIO.getDescrizione();
	}

	@Override
	List<IZone> createZone(String key, JsonObject jsonMap, ZoneFactory factory) {
		// TODO Auto-generated method stub
		return null;
	}



}
