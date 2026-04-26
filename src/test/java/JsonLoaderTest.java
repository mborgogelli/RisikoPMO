import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.FileNotFoundException;
import java.io.IOException;

import it.uniurb.pmo.model.utils.JsonLoader;
import org.junit.jupiter.api.Test;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

public class JsonLoaderTest {
	
	private JsonObject map;
	
	@Test
	void testingMapLoader() throws JsonSyntaxException, IOException {
		this.map = JsonLoader.loadJsonFile("risikonew");
		assertNotNull(map);
	}
	
	@Test
	void testingEmtpyArgument()throws IllegalArgumentException {
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
														() -> JsonLoader.loadJsonFile(""));
		assertTrue(exception.getMessage().contains("Game version is required"));
	}
	
	@Test
	void testingFileNotFound() {
		FileNotFoundException exception = assertThrows(FileNotFoundException.class,
														() -> JsonLoader.loadJsonFile("risikoantartide"));
		assertTrue(exception.getMessage().contains("File src/main/resources/asset/risikoantartide.json not found"));
	}
	
	@Test
	void testingJsonSyntaxException()throws JsonSyntaxException {
		JsonSyntaxException exception = assertThrows(JsonSyntaxException.class,
														() -> JsonLoader.loadJsonFile("risikotest_map"));
		assertTrue(exception.getMessage().contains("Json file src/main/resources/asset/risikotest_map.json is invalid."));
	}
	
	@Test
	void testingJsonKeys() throws JsonSyntaxException, IOException {
		this.map = JsonLoader.loadJsonFile("risikonew");
		assertTrue(map.has("continents"));
	}

}
