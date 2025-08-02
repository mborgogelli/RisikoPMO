package model.map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.stream.StreamSupport;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import model.utils.ZoneTypeClassic;

public class TestMapCreator {
	
	private JsonObject jsonMap;
	private MapCreatorRisikoClassic map;
	
	@BeforeEach
	void loadMap() {
		this.map = MapCreatorRisikoClassic.getInstance();
		this.jsonMap = this.map.getJsonObject();
	}
	
	@Test
	public void getContinentsNameFromJson(){
		List<JsonElement> continents = this.map.getValues("continents", this.jsonMap);
		List<String> list = this.map.getValues("name", continents, String.class);
		
		assertTrue(list.contains("europa"));
	    assertTrue(list.contains("america_settentrionale"));
	    assertTrue(list.contains("africa"));
	    assertTrue(list.contains("america_meridionale"));
	    assertTrue(list.contains("oceania"));
	    assertTrue(list.contains("asia"));
	    assertEquals(6, list.size());
	}
	
	@Test
	public void createContinents() {
		List<JsonElement> continents = this.map.getValues(ZoneTypeClassic.CONTINENTS.getDescrizione(), this.jsonMap);
		List<IZone> list = this.map.createZones("name", continents, Continent::new);
		
		assertEquals(6, list.size());
		assertTrue(list.stream().allMatch(zone -> zone instanceof Continent));
	}
	
	@Test
	public void createTerritories() {
		this.map.createMap();
		List<IZone> continents = this.map.getMap();
		System.out.println(continents.get(0).getChildZones().toString());
	}
	
	@Test
	public void getContinentsArmyFromJson(){
		List<JsonElement> continents = this.map.getValues("continents", this.jsonMap);
		List<Integer> list = this.map.getValues("armate", continents, Integer.class);
		
		List<Integer> expected = List.of(
				2, 2, 3, 5, 5, 7);
			
		assertEquals(expected.size(), list.size());
		assertTrue(list.containsAll(expected));
	}

	@Test
	public void getEuropaTerritoriesFromJson(){
		List<JsonElement> continents = this.map.getValues("continents", this.jsonMap);
		JsonArray continentsArray = continents.get(0).getAsJsonArray();
		
		JsonElement europa = StreamSupport.stream(continentsArray.spliterator(),false)
			.filter(c -> c.getAsJsonObject().get("name").getAsString().equals("europa"))
			.findFirst().orElseThrow(() -> new IllegalArgumentException("Europa continent not found"));
		
		List<JsonElement> territories = this.map.getValues("territories", europa);
		List<String> europaTerritories = this.map.getValues("name", territories, String.class);
		
		List<String> expected = List.of(
			"islanda",
			"gran_bretagna",
			"scandinavia",
			"ucraina",
			"europa_settentrionale",
			"europa_occidentale",
			"europa_meridionale"
		);
		
		assertEquals(expected.size(), europaTerritories.size());
		assertTrue(europaTerritories.containsAll(expected));
	}
	
	@Test
	public void cannotReturnEmptyList() {
	    List<JsonElement> continents = this.map.getValues("continents", this.jsonMap);
	    List<JsonElement> territories = this.map.getValues("territories", continents);
	    List<JsonElement> islandaNeighbours = this.map.getValues("neighbours", territories.get(0));

	    assertThrows(IllegalArgumentException.class, () -> {
	        this.map.getValues("neighbours", islandaNeighbours);
	    }, "List is empty.");
	}

}