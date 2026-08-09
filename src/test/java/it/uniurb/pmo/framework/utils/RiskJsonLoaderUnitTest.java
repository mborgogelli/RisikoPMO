package it.uniurb.pmo.framework.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class RiskJsonLoaderUnitTest {

	private final static String JSON_MAP = GameVersion.RISIKONEW.getDescrizione() + "_map";
	private JsonObject map;
	
	@Test
	@DisplayName("Unit: Map Loader")
	void testingMapLoader() throws JsonSyntaxException, IOException {
		this.map = RiskJsonLoader.loadJsonFile(JSON_MAP);
		assertNotNull(map);
	}
	
	@Test
	@DisplayName("Unit: Empty Argument")
	void testingEmptyArgument()throws IllegalArgumentException {
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
														() -> RiskJsonLoader.loadJsonFile(""));
		assertTrue(exception.getMessage().contains("Game version is required"));
	}
	
	@Test
	@DisplayName("Unit: File Not Found")
	void testingFileNotFound() {
		FileNotFoundException exception = assertThrows(FileNotFoundException.class,
														() -> RiskJsonLoader.loadJsonFile("risikoantartide"));
		assertTrue(exception.getMessage().contains("File src/main/resources/asset/risikoantartide.json not found"));
	}
	
	@Test
	@DisplayName("Unit: Json Syntax Exception")
	void testingJsonSyntaxException()throws JsonSyntaxException {
		JsonSyntaxException exception = assertThrows(JsonSyntaxException.class,
														() -> RiskJsonLoader.loadJsonFile("risikotest_map"));
		assertTrue(exception.getMessage().contains("Json file src/main/resources/asset/risikotest_map.json is invalid."));
	}
	
	@Test
	@DisplayName("Unit: Json Keys")
	void testingJsonKeys() throws JsonSyntaxException, IOException {
		this.map = RiskJsonLoader.loadJsonFile(JSON_MAP);
		assertTrue(map.has("continents"));
	}

}
