package tests;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.FileNotFoundException;
import java.lang.reflect.Constructor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.google.gson.JsonObject;

import model.utils.MapLoader;

public class MapLoaderTest {
	
	private JsonObject map;
	
	@Test
	void testingMapLoader() throws FileNotFoundException {
		this.map = MapLoader.loadMapFile("risiko");
		assertNotNull(map);
	}
	
	@Test
	void testingFileNotFound()throws FileNotFoundException {
		FileNotFoundException exception = assertThrows(FileNotFoundException.class,
														() -> MapLoader.loadMapFile("risikoantartide"));
		assertTrue(exception.getMessage().contains("File asset/risikoantartide_map.json not found"));
	}
	
	@Test
	void testingJsonKeys() throws FileNotFoundException {
		this.map = MapLoader.loadMapFile("risiko");
		assertTrue(map.has("continents"));
		assertTrue(map.has("neighbourhood"));
	}
	
}
