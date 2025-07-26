package model.map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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
	public void getContinentsFromJson(){
		
		List<String> list = this.map.getListFromJson("continents",this.jsonMap);
		assertTrue(list.contains("europa"));
	    assertTrue(list.contains("america_settentrionale"));
	    assertTrue(list.contains("africa"));
	    assertTrue(list.contains("america_meridionale"));
	    assertTrue(list.contains("oceania"));
	    assertTrue(list.contains("asia"));
	    assertEquals(6, list.size());
	}
	
	@Test
	public void keyVerify(){
		
		List<JsonElement> json = this.map.getValue("continents", this.jsonMap);
		List<JsonElement> json2 = this.map.getValueByKey("name", json.get(0).getAsJsonObject());
		//List<JsonElement> json3 = this.map.getValueByKey("neighbours", json2.get(0).getAsJsonObject());
		json2.stream().forEach(System.out::println);
		/*for(JsonElement elem : json) {
			JsonObject obj = elem.getAsJsonObject();
			String name = obj.get("name").getAsString();
			System.out.println(name);
		}*/
		
		//List<String> list = json.stream().map(j -> j.getAsJsonObject().get("name").getAsString()).collect(Collectors.toList());
		//List<JsonElement> json3 = this.map.getValue("territories", json.get(0));
		//System.out.println(this.map.getValue("neighbours", json3.get(0)));
		//System.out.println(this.map.getListByKey("continents", "name", jsonMap));
		//System.out.println(JsonParser.parseString(this.jsonMap.toString()));
		/*JsonArray array = this.jsonMap.getAsJsonArray("continents");
		for(JsonElement elem : array) {
			if(elem.isJsonObject()) {
				JsonObject jo = elem.getAsJsonObject();
				System.out.print(jo.get("name") + ": ");
				List<String> list = this.map.getListFromJson("territories", jo);
				System.out.println(list);
			}
		}*/
	}
}
