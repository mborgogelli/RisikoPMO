package model.map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

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
	public void getContinentsFromJson(){
		
		List<String> set = this.map.getListFromJson("continents",this.jsonMap);
		assertTrue(set.contains("europa"));
	    assertTrue(set.contains("america_settentrionale"));
	    assertTrue(set.contains("africa"));
	    assertTrue(set.contains("america_meridionale"));
	    assertTrue(set.contains("oceania"));
	    assertTrue(set.contains("asia"));
	    assertEquals(6, set.size());
	}
	
	@Test
	public void keyVerify(){
		JsonArray array = this.jsonMap.getAsJsonArray("continents");
		Set<JsonElement> set = this.map.getElementsByArray(array);
		
	}
}
