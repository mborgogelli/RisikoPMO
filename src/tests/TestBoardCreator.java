package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.List;
import java.util.stream.StreamSupport;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import model.board.IZone;
import model.board.risikoclassic.BoardCreatorRisikoClassic;
import model.board.risikoclassic.Continent;
import model.board.risikoclassic.Territory;

public class TestBoardCreator extends BoardCreatorRisikoClassic {
	
	private JsonObject jsonMap;
	
	@BeforeEach
	void loadMap() {
		this.jsonMap = super.getLoadedJson();
	}
	
	@Test
	public void getContinentsNameFromJson(){
		List<JsonElement> continents = super.getValues("continents", this.jsonMap);
		List<String> list = super.getValues("name", continents, String.class);
		
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
		super.createMap();
		List<IZone> continents = super.getMap().getZones();
		
		assertEquals(6, continents.size());
		assertTrue(continents.stream().allMatch(zone -> zone instanceof Continent));
	}
	
	@Test
	public void createTerritories() {
		super.createMap();
		List<IZone> continents = super.getMap().getZones();
		
		assertEquals(7, continents.get(0).getChildZones().size());
		assertTrue(continents.get(0).getChildZones().stream().allMatch(zone -> zone instanceof Territory));
	}
	
	@Test
	public void getArmyFromContinent() {
		super.createMap();
		List<IZone> continents = super.getMap().getZones();
		
		assertEquals(5, continents.get(0).getValue());
		assertEquals(3, continents.get(1).getValue());
		assertEquals(7, continents.get(2).getValue());
		assertEquals(2, continents.get(3).getValue());
		assertEquals(5, continents.get(4).getValue());
		assertEquals(2, continents.get(5).getValue());
	}
	
	@Test
	public void getContinentsArmyFromJson(){
		List<JsonElement> continents = super.getValues("continents", this.jsonMap);
		List<Integer> list = super.getValues("army", continents, Integer.class);
		
		List<Integer> expected = List.of(
				2, 2, 3, 5, 5, 7);
			
		assertEquals(expected.size(), list.size());
		assertTrue(list.containsAll(expected));
	}
	
	@Test
	public void getEuropaTerritoriesFromJson(){
		List<JsonElement> continents = super.getValues("continents", this.jsonMap);
		JsonArray continentsArray = continents.get(0).getAsJsonArray();
		
		JsonElement europa = StreamSupport.stream(continentsArray.spliterator(),false)
			.filter(c -> c.getAsJsonObject().get("name").getAsString().equals("europa"))
			.findFirst().orElseThrow(() -> new IllegalArgumentException("Europa continent not found"));
		
		List<JsonElement> territories = super.getValues("territories", europa);
		List<String> europaTerritories = super.getValues("name", territories, String.class);
		
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
	    List<JsonElement> continents = super.getValues("continents", this.jsonMap);
	    List<JsonElement> territories = super.getValues("territories", continents);
	    List<JsonElement> islandaNeighbours = super.getValues("neighbours", territories.get(0));

	    assertThrows(IllegalArgumentException.class, () -> {
	        super.getValues("neighbours", islandaNeighbours);
	    }, "List is empty.");
	}

}