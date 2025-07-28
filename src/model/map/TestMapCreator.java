package model.map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
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
	
	/*@Test
	public void getContinentsFromJson(){
		
		List<String> list = this.map.getStringList("continents",this.jsonMap);
		assertTrue(list.contains("europa"));
	    assertTrue(list.contains("america_settentrionale"));
	    assertTrue(list.contains("africa"));
	    assertTrue(list.contains("america_meridionale"));
	    assertTrue(list.contains("oceania"));
	    assertTrue(list.contains("asia"));
	    assertEquals(6, list.size());
	}*/
	
	@Test
	public void keyVerify(){
		
		List<JsonElement> json = this.map.getValue("continents", this.jsonMap);
		List<JsonElement> json2 = this.map.getValue("territories", json.get(0).getAsJsonObject());
		List<JsonElement> json3 = this.map.getValue("neighbours", json2.get(1).getAsJsonObject());
		JsonObject obj = this.jsonMap.getAsJsonArray("continents").get(0).getAsJsonObject().getAsJsonArray("territories").get(0).getAsJsonObject();
		List<JsonElement> jsonTest = this.map.getValue("neighbours", obj);
		
		System.out.println(jsonTest);
		//json2.stream().forEach(element -> this.map.getValue("neighbours", element).forEach(System.out::println));
		//System.out.println(json3);
		/*for(JsonElement elem : json3) {
			if(elem.isJsonObject()) {
				JsonObject obj = elem.getAsJsonObject();
				String name = obj.get("name").getAsString();
				System.out.println(name);
			}
		}*/
	}
}
