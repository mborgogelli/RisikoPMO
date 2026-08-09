package it.uniurb.pmo.variants.risikonew.board;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.stream.StreamSupport;

import it.uniurb.pmo.framework.board.BoardCreator;
import it.uniurb.pmo.framework.utils.GameVersion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import it.uniurb.pmo.framework.board.IGameBoard;
import it.uniurb.pmo.framework.board.IZone;
import it.uniurb.pmo.framework.utils.RiskJsonParser;

class BoardCreatorIntegrationTest {

	private JsonObject jsonMap;

	private JsonObject getLoadedMap() {
		return new BoardCreatorTestSupport().getLoadedMap();
	}

	private List<JsonElement> getValues(String key, JsonElement element) {
		return RiskJsonParser.getValues(key, element);
	}

	private List<JsonElement> getValues(String key, List<JsonElement> elements) {
		return RiskJsonParser.getValues(key, elements);
	}

	private <T> List<T> getValues(String key, List<JsonElement> elements, Class<T> type) {
		return RiskJsonParser.getValues(key, elements, type);
	}

	@BeforeEach
	public void setUp() {
		this.jsonMap = getLoadedMap();
	}

	@Test
	@DisplayName("Integration: Get continents name from json")
	public void getContinentsNameFromJson(){
		List<JsonElement> continents = getValues("continents", this.jsonMap);
		List<String> list = getValues("name", continents, String.class);

		assertTrue(list.contains("europa"));
	    assertTrue(list.contains("america_settentrionale"));
	    assertTrue(list.contains("africa"));
	    assertTrue(list.contains("america_meridionale"));
	    assertTrue(list.contains("oceania"));
	    assertTrue(list.contains("asia"));
	    assertEquals(6, list.size());

	}

	@Test
	@DisplayName("Integration: Continents creation")
	public void createContinents() {
		List<IZone> continents = BoardCreatorRisikoNew.getInstance().getMap().getRootZones();

		assertEquals(6, continents.size());
		assertTrue(continents.stream().allMatch(zone -> zone instanceof Continent));
	}

	@Test
	@DisplayName("Integration: Territories creation")
	public void createTerritories() {
		List<IZone> continents = BoardCreatorRisikoNew.getInstance().getMap().getRootZones();

		assertEquals(7, continents.getFirst().getChildZones().size());
		assertTrue(continents.getFirst().getChildZones().stream().allMatch(zone -> zone instanceof Territory));
	}

	@Test
	@DisplayName("Integration: Get army bonus from continent")
	public void getArmyFromContinent() {
		List<IZone> continents = BoardCreatorRisikoNew.getInstance().getMap().getRootZones();

		assertEquals(5, continents.get(0).getValue());
		assertEquals(3, continents.get(1).getValue());
		assertEquals(7, continents.get(2).getValue());
		assertEquals(2, continents.get(3).getValue());
		assertEquals(5, continents.get(4).getValue());
		assertEquals(2, continents.get(5).getValue());
	}

	@Test
	@DisplayName("Integration: Get continents bonus from json")
	public void getContinentsArmyFromJson(){
		List<JsonElement> continents = getValues("continents", this.jsonMap);
		List<Integer> list = getValues("armybonus", continents, Integer.class);

		List<Integer> expected = List.of(
				2, 2, 3, 5, 5, 7);

		assertEquals(expected.size(), list.size());
		assertTrue(list.containsAll(expected));
	}

	@Test
	@DisplayName("Integration: Get Europe bonus points from json")
	public void testEuropaTerritoriesBonusPoints() {
	    List<IZone> continents = BoardCreatorRisikoNew.getInstance().getMap().getRootZones();
	    IZone europa = continents.stream()
						        .filter(zone -> zone instanceof Continent && zone.getName().equals("europa"))
						        .findFirst()
						        .orElseThrow(() -> new IllegalArgumentException("Europa continent not found"));

	    List<IZone> europaTerritories = europa.getChildZones();

	    List<Integer> expectedBonusValues = List.of(3, 4, 4, 6, 5, 4, 6);

	    List<Integer> actualBonusValues = europaTerritories.stream()
												        .map(IZone::getValue)
												        .toList();

	    assertEquals(expectedBonusValues, actualBonusValues);
	}

	@Test
	@DisplayName("Integration: Get Europe territories from json")
	public void getEuropaTerritoriesFromJson(){
		List<JsonElement> continents = getValues("continents", this.jsonMap);
		JsonArray continentsArray = continents.getFirst().getAsJsonArray();

		JsonElement europa = StreamSupport.stream(continentsArray.spliterator(),false)
			.filter(c -> c.getAsJsonObject().get("name").getAsString().equals("europa"))
			.findFirst().orElseThrow(() -> new IllegalArgumentException("Europa continent not found"));

		List<JsonElement> territories = getValues("territories", europa);
		List<String> europaTerritories = getValues("name", territories, String.class);

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
	@DisplayName("Integration: Cannot return empty list")
	public void cannotReturnEmptyList() {
	    List<JsonElement> continents = getValues("continents", this.jsonMap);
	    List<JsonElement> territories = getValues("territories", continents);
	    List<JsonElement> islandaNeighbours = getValues("neighbours", territories.getFirst());

	    assertThrows(IllegalArgumentException.class,
	        () -> getValues("neighbours", islandaNeighbours));
	}

	@Test
	@DisplayName("Integration: Check neighbours")
	public void testSetNeighbours() {
	    IGameBoard gameBoard = BoardCreatorRisikoNew.getInstance().getMap();

	    List<String> islandaNeighbours = gameBoard.getNeighbours("islanda");

	    assertNotNull(islandaNeighbours);
	    assertTrue(islandaNeighbours.contains("gran_bretagna"));
	    assertTrue(islandaNeighbours.contains("scandinavia"));
	    assertEquals(3, islandaNeighbours.size());

	    List<String> ucrainaNeighbours = gameBoard.getNeighbours("ucraina");
	    assertNotNull(ucrainaNeighbours);
	    assertTrue(ucrainaNeighbours.contains("scandinavia"));
	    assertTrue(ucrainaNeighbours.contains("europa_settentrionale"));
	    assertTrue(ucrainaNeighbours.contains("europa_meridionale"));
	    assertTrue(ucrainaNeighbours.contains("afghanistan"));
	    assertTrue(ucrainaNeighbours.contains("medio_oriente"));
	    assertTrue(ucrainaNeighbours.contains("urali"));
	    assertEquals(6, ucrainaNeighbours.size());
	}

	/**
	 * Inner class for testing purposes
	 */
	private static class BoardCreatorTestSupport extends BoardCreator {

		public BoardCreatorTestSupport() {
			super(GameVersion.RISIKONEW);
		}

		@Override
		protected IGameBoard createMap() {
			return null;
		}

		@Override
		public JsonObject getLoadedMap() {
			return super.getLoadedMap();
		}
	}


}
