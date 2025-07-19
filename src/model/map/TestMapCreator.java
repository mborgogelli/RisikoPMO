package model.map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
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
		
		System.out.println(this.map.getListByKey("continents", "name", jsonMap));
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
