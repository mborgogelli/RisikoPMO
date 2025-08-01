package model.map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import model.utils.MapLoader;

public class TestMapCreator {
	
	private JsonObject jsonMap;
	private MapCreator map;
	
	@BeforeEach
	void loadMap() {
		try {
			this.jsonMap = MapLoader.loadMapFile("risikonew");
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.map = new MapCreatorRisikoClassic();
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
	public void cannotReturnEmptyList() {
	    List<JsonElement> continents = this.map.getValues("continents", this.jsonMap);
	    List<JsonElement> territories = this.map.getValues("territories", continents);
	    List<JsonElement> islandaNeighbours = this.map.getValues("neighbours", territories.get(0));

	    // Qui ci aspettiamo che venga lanciata un'eccezione perché la lista sarà vuota
	    assertThrows(IllegalArgumentException.class, () -> {
	        this.map.getValues("neighbours", islandaNeighbours);
	    }, "List is empty.");
	}

	
	@Test
	public void keyVerify(){
		
		List<JsonElement> json1 = this.map.getValues("continents", this.jsonMap);
		List<JsonElement> continents = this.map.getValues("name", json1);
		List<String> names = this.map.getValues("name", json1, String.class);
		List<JsonElement> json2 = this.map.getValues("territories", json1);
		List<JsonElement> json3 = this.map.getValues("neighbours", json2.get(0));
		json1.stream().forEach(System.out::println);
		System.out.println();
		continents.stream().forEach(System.out::println);
		System.out.println();
		names.stream().forEach(System.out::println);
		System.out.println();
		json2.stream().forEach(System.out::println);
		System.out.println();
		json3.stream().forEach(System.out::println);
		System.out.println();
		}
}