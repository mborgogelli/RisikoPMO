import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import it.uniurb.pmo.framework.utils.GameVersion;
import it.uniurb.pmo.framework.utils.RiskJsonLoader;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class RiskJsonLoaderTest {

	private final static String JSON_MAP = GameVersion.RISIKONEW.getDescrizione() + "_map";
	private JsonObject map;
	
	@Test
	void testingMapLoader() throws JsonSyntaxException, IOException {
		this.map = RiskJsonLoader.loadJsonFile(JSON_MAP);
		assertNotNull(map);
	}
	
	@Test
	void testingEmtpyArgument()throws IllegalArgumentException {
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
														() -> RiskJsonLoader.loadJsonFile(""));
		assertTrue(exception.getMessage().contains("Game version is required"));
	}
	
	@Test
	void testingFileNotFound() {
		FileNotFoundException exception = assertThrows(FileNotFoundException.class,
														() -> RiskJsonLoader.loadJsonFile("risikoantartide"));
		assertTrue(exception.getMessage().contains("File src/main/resources/asset/risikoantartide.json not found"));
	}
	
	@Test
	void testingJsonSyntaxException()throws JsonSyntaxException {
		JsonSyntaxException exception = assertThrows(JsonSyntaxException.class,
														() -> RiskJsonLoader.loadJsonFile("risikotest_map"));
		assertTrue(exception.getMessage().contains("Json file src/main/resources/asset/risikotest_map.json is invalid."));
	}
	
	@Test
	void testingJsonKeys() throws JsonSyntaxException, IOException {
		this.map = RiskJsonLoader.loadJsonFile(JSON_MAP);
		assertTrue(map.has("continents"));
	}

}
