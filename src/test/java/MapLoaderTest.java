import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.jupiter.api.Test;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import it.uniurb.pmo.model.utils.MapLoader;

public class MapLoaderTest {
	
	private JsonObject map;
	
	@Test
	void testingMapLoader() throws JsonSyntaxException, IOException {
		this.map = MapLoader.loadMapFile("risiko");
		assertNotNull(map);
	}
	
	@Test
	void testingMapLoader2() throws JsonSyntaxException, IOException {
		this.map = MapLoader.loadMapFile("risikonew");
		assertNotNull(map);
	}
	
	@Test
	void testingEmtpyArgument()throws IllegalArgumentException {
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
														() -> MapLoader.loadMapFile(""));
		assertTrue(exception.getMessage().contains("Game version is required"));
	}
	
	@Test
	void testingFileNotFound()throws FileNotFoundException {
		FileNotFoundException exception = assertThrows(FileNotFoundException.class,
														() -> MapLoader.loadMapFile("risikoantartide"));
		assertTrue(exception.getMessage().contains("File src/main/resources/asset/risikoantartide_map.json not found"));
	}
	
	@Test
	void testingJsonSyntaxException()throws JsonSyntaxException {
		JsonSyntaxException exception = assertThrows(JsonSyntaxException.class,
														() -> MapLoader.loadMapFile("risikotest"));
		assertTrue(exception.getMessage().contains("Json file src/main/resources/asset/risikotest_map.json is invalid."));
	}
	
	@Test
	void testingJsonKeys() throws JsonSyntaxException, IOException {
		this.map = MapLoader.loadMapFile("risiko");
		assertTrue(map.has("continents"));
		this.map = MapLoader.loadMapFile("risikonew");
		assertTrue(map.has("continents"));
	}
	
}
