package it.uniurb.pmo.framework.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class RiskJsonLoaderUnitTest {

	private final static String JSON_MAP = EGameVersion.RISIKONEW.getDescrizione() + "_map";
	private JsonObject map;
	
	@Test
	@DisplayName("Carica correttamente la mappa JSON di Risiko New")
	void testingMapLoader() throws JsonSyntaxException, IOException {
		this.map = RiskJsonLoader.loadJsonFile(JSON_MAP);
		assertNotNull(map);
	}
	
	@Test
	@DisplayName("Rifiuta una versione di gioco vuota")
	void testingEmptyArgument()throws IllegalArgumentException {
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
														() -> RiskJsonLoader.loadJsonFile(""));
		assertTrue(exception.getMessage().contains("Game version is required"));
	}
	
	@Test
	@DisplayName("Segnala l'assenza del file della mappa richiesto")
	void testingFileNotFound() {
		FileNotFoundException exception = assertThrows(FileNotFoundException.class,
														() -> RiskJsonLoader.loadJsonFile("risikoantartide"));
		assertTrue(exception.getMessage().contains("File src/main/resources/asset/risikoantartide.json not found"));
	}
	
	@Test
	@DisplayName("Segnala un errore di sintassi nel file JSON")
	void testingJsonSyntaxException()throws JsonSyntaxException {
		JsonSyntaxException exception = assertThrows(JsonSyntaxException.class,
														() -> RiskJsonLoader.loadJsonFile("risikotest_map"));
		assertTrue(exception.getMessage().contains("Json file src/main/resources/asset/risikotest_map.json is invalid."));
	}
	
	@Test
	@DisplayName("Verifica che la mappa caricata contenga la chiave continenti")
	void testingJsonKeys() throws JsonSyntaxException, IOException {
		this.map = RiskJsonLoader.loadJsonFile(JSON_MAP);
		assertTrue(map.has("continents"));
	}

}
