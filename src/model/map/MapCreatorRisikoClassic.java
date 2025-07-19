package model.map;

import java.util.List;

import com.google.gson.JsonObject;

import model.utils.ZoneType;

public class MapCreatorRisikoClassic extends MapCreator {
	
	private List<IZone> continents;
	JsonObject jsonMap;
	
	MapCreatorRisikoClassic() {
		this.jsonMap = new JsonObject();
	}

	@Override
	void createMap() {
		this.createContinents();
		this.insertTerritories();
	}
	
	private void createContinents() {
		String continents = ZoneType.CONTINENTS.getDescrizione();
		super.createZone(continents,this.jsonMap,Continent::new);
	}
	
	private void insertTerritories() {
		String territories = ZoneType.TERRITORIO.getDescrizione();
		super.createZone(territories, this.jsonMap, Territory::new);
	}

	@Override
	void getMap() {
		// TODO Auto-generated method stub
		
	}

}
