package tests;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.FileNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.google.gson.JsonObject;

import model.utils.MapLoader;

public class MapLoaderTest {
	
	private MapLoader mapLoader;
	private JsonObject map;
	
	@BeforeEach
	public void initMapLoader() {
		this.mapLoader = new MapLoader();
		
	}
	
	@Test
	void testingMapLoader() throws FileNotFoundException {
		this.map = mapLoader.loadMapFile("risiko");
		assertNotNull(map);
	}
	
	@Test
	void testingFileNotFound()throws FileNotFoundException {
		FileNotFoundException exception = assertThrows(FileNotFoundException.class,
														() -> this.mapLoader.loadMapFile("risikoantartide"));
		assertTrue(exception.getMessage().contains("File asset/risikoantartide_map.json non trovato"));
	}
	
	@Test
	void testingJsonKeys() throws FileNotFoundException {
		this.map = mapLoader.loadMapFile("risiko");
		assertTrue(map.has("continents"));
		assertTrue(map.has("territories"));
	
	}
	

}
