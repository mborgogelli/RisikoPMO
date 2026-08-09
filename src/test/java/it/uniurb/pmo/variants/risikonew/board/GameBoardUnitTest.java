package it.uniurb.pmo.variants.risikonew.board;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import it.uniurb.pmo.framework.board.IZone;
import it.uniurb.pmo.framework.utils.GameVersion;

class GameBoardUnitTest {

	private GameBoardRisikoNew gameBoard;

	@BeforeEach
	void setUp() {
		this.gameBoard = new GameBoardRisikoNew(buildContinents());
	}

	private List<IZone> buildContinents() {
		Continent europa = new Continent("europa");
		Territory islanda = new Territory("islanda");
		Territory granBretagna = new Territory("gran_bretagna");
		islanda.setNeighbours(List.of("gran_bretagna", "scandinavia"));
		granBretagna.setNeighbours(List.of("islanda"));
		islanda.setValue(3);
		granBretagna.setValue(4);
		europa.setChildZones(List.of(islanda, granBretagna));

		Continent asia = new Continent("asia");
		Territory kamchatka = new Territory("kamchatka");
		Territory urali = new Territory("urali");
		kamchatka.setNeighbours(List.of("urali"));
		urali.setNeighbours(List.of("kamchatka", "islanda"));
		kamchatka.setValue(5);
		urali.setValue(2);
		asia.setChildZones(List.of(kamchatka, urali));

		return List.of(europa, asia);
	}

	@Test
	@DisplayName("getRootZones restituisce le continenti passati al costruttore")
	void getRootZonesReturnsContinents() {
		List<IZone> rootZones = this.gameBoard.getRootZones();

		assertNotNull(rootZones);
		assertEquals(2, rootZones.size());
		assertEquals("europa", rootZones.get(0).getName());
		assertEquals("asia", rootZones.get(1).getName());
	}

	@Test
	@DisplayName("getGameVersion restituisce RISIKONEW")
	void getGameVersionReturnsRisikoNew() {
		assertEquals(GameVersion.RISIKONEW, this.gameBoard.getGameVersion());
	}

	@Test
	@DisplayName("findZoneByName trova territori e continenti ignorando il case")
	void findZoneByNameIsCaseInsensitive() {
		IZone islanda = this.gameBoard.findZoneByName("ISLANDA");
		IZone europa = this.gameBoard.findZoneByName("euROPa");

		assertNotNull(islanda);
		assertNotNull(europa);
		assertEquals("islanda", islanda.getName());
		assertEquals("europa", europa.getName());
		assertNull(this.gameBoard.findZoneByName("non_esiste"));
	}

	@Test
	@DisplayName("getNeighbours restituisce i vicini del territorio")
	void getNeighboursReturnsTerritoryNeighbours() {
		List<String> neighbours = this.gameBoard.getNeighbours("islanda");

		assertNotNull(neighbours);
		assertEquals(2, neighbours.size());
		assertTrue(neighbours.contains("gran_bretagna"));
		assertTrue(neighbours.contains("scandinavia"));
		assertNull(this.gameBoard.getNeighbours("non_esiste"));
	}

	@Test
	@DisplayName("whereIsZone restituisce il continente contenitore del territorio")
	void whereIsZoneReturnsParentContinent() {
		Optional<IZone> parentZone = this.gameBoard.whereIsZone("kamchatka");

		assertTrue(parentZone.isPresent());
		assertEquals("asia", parentZone.get().getName());
		assertTrue(this.gameBoard.whereIsZone("non_esiste").isEmpty());
	}

	@Test
	@DisplayName("canReach verifica la raggiungibilità tra territori")
	void canReachChecksNeighbourhood() {
		assertTrue(this.gameBoard.canReach("gran_bretagna", "islanda"));
		assertTrue(this.gameBoard.canReach("islanda", "gran_bretagna"));
		assertFalse(this.gameBoard.canReach("kamchatka", "islanda"));
		assertFalse(this.gameBoard.canReach("non_esiste", "islanda"));
		assertFalse(this.gameBoard.canReach("gran_bretagna", "non_esiste"));
	}

	@Test
	@DisplayName("getValue restituisce il valore assegnato alla zona")
	void getValueReturnsZoneValue() {
		assertEquals(3, this.gameBoard.getValue("islanda"));
		assertEquals(4, this.gameBoard.getValue("gran_bretagna"));
		assertEquals(5, this.gameBoard.getValue("kamchatka"));
	}
}



