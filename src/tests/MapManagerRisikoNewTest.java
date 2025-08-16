package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.board.IZone;
import model.versions.risikockassic.MapManagerRisikoNew;

class MapManagerRisikoNewTest {

    private MapManagerRisikoNew mapManager;

    @BeforeEach
    void setUp() {
        mapManager = MapManagerRisikoNew.getInstance();
        mapManager.resetInstance();
        mapManager = MapManagerRisikoNew.getInstance();
        mapManager.initializeGame();
    }

    @Test
    void testGetInstance() {
        MapManagerRisikoNew instance1 = MapManagerRisikoNew.getInstance();
        MapManagerRisikoNew instance2 = MapManagerRisikoNew.getInstance();

        assertNotNull(instance1);
        assertSame(instance1, instance2);
    }

    @Test
    void testResetInstance() {
        MapManagerRisikoNew instance1 = MapManagerRisikoNew.getInstance();
        instance1.resetInstance();
        MapManagerRisikoNew instance2 = MapManagerRisikoNew.getInstance();

        assertNotSame(instance1, instance2);
    }

    @Test
    void testGetAllContinents() {
        List<IZone> continents = mapManager.getAllContinents();

        assertNotNull(continents);
        assertEquals(6, continents.size());
        
        // Verify continent names
        List<String> continentNames = continents.stream()
            .map(IZone::getName)
            .toList();
        
        assertTrue(continentNames.contains("europa"));
        assertTrue(continentNames.contains("africa"));
        assertTrue(continentNames.contains("asia"));
        assertTrue(continentNames.contains("oceania"));
        assertTrue(continentNames.contains("america_settentrionale"));
        assertTrue(continentNames.contains("america_meridionale"));
    }

    @Test
    void testFindTerritoryByName() {
        // Test existing territories
        IZone alaska = mapManager.findTerritoryByName("alaska");
        assertNotNull(alaska);
        assertEquals("alaska", alaska.getName());

        IZone islanda = mapManager.findTerritoryByName("islanda");
        assertNotNull(islanda);
        assertEquals("islanda", islanda.getName());

        IZone cina = mapManager.findTerritoryByName("cina");
        assertNotNull(cina);
        assertEquals("cina", cina.getName());
    }

    @Test
    void testFindTerritoryByNameNotFound() {
        IZone nonExistent = mapManager.findTerritoryByName("territorio_inesistente");
        assertNull(nonExistent);
    }

    @Test
    void testFindContinentOfTerritory() {
        // Test territories from different continents
        Optional<IZone> europaContinent = mapManager.findContinentOfTerritory("islanda");
        assertTrue(europaContinent.isPresent());
        assertEquals("europa", europaContinent.get().getName());

        Optional<IZone> asiaContinent = mapManager.findContinentOfTerritory("cina");
        assertTrue(asiaContinent.isPresent());
        assertEquals("asia", asiaContinent.get().getName());

        Optional<IZone> africaContinent = mapManager.findContinentOfTerritory("egitto");
        assertTrue(africaContinent.isPresent());
        assertEquals("africa", africaContinent.get().getName());

        Optional<IZone> oceaniaContinent = mapManager.findContinentOfTerritory("australia_orientale");
        assertTrue(oceaniaContinent.isPresent());
        assertEquals("oceania", oceaniaContinent.get().getName());
    }

    @Test
    void testGetAdjacentTerritories() {
        // Test Alaska neighbors
        List<String> alaskaNeighbors = mapManager.getAdjacentTerritories("alaska");
        assertNotNull(alaskaNeighbors);
        assertEquals(3, alaskaNeighbors.size());
        assertTrue(alaskaNeighbors.contains("kamchatka"));
        assertTrue(alaskaNeighbors.contains("territori_del_nord_ovest"));
        assertTrue(alaskaNeighbors.contains("alberta"));

        // Test China neighbors (has many neighbors)
        List<String> cinaNeighbors = mapManager.getAdjacentTerritories("cina");
        assertNotNull(cinaNeighbors);
        assertEquals(7, cinaNeighbors.size());
        assertTrue(cinaNeighbors.contains("india"));
        assertTrue(cinaNeighbors.contains("afghanistan"));
        assertTrue(cinaNeighbors.contains("siam"));
        assertTrue(cinaNeighbors.contains("medio_oriente"));
        assertTrue(cinaNeighbors.contains("mongolia"));
        assertTrue(cinaNeighbors.contains("siberia"));
        assertTrue(cinaNeighbors.contains("urali"));

        // Test Iceland neighbors
        List<String> islandaNeighbors = mapManager.getAdjacentTerritories("islanda");
        assertNotNull(islandaNeighbors);
        assertEquals(3, islandaNeighbors.size());
        assertTrue(islandaNeighbors.contains("gran_bretagna"));
        assertTrue(islandaNeighbors.contains("scandinavia"));
        assertTrue(islandaNeighbors.contains("groenlandia"));
    }

    @Test
    void testCanAttackTerritory() {
        // Test adjacent territories - should be able to attack
        assertTrue(mapManager.canAttackTerritory("alaska", "alberta"));
        assertTrue(mapManager.canAttackTerritory("alaska", "kamchatka"));
        assertTrue(mapManager.canAttackTerritory("cina", "india"));
        assertTrue(mapManager.canAttackTerritory("islanda", "gran_bretagna"));

        // Test non-adjacent territories - should not be able to attack
        assertFalse(mapManager.canAttackTerritory("alaska", "brasile"));
        assertFalse(mapManager.canAttackTerritory("islanda", "australia_orientale"));
        assertFalse(mapManager.canAttackTerritory("egitto", "groenlandia"));
    }

    @Test
    void testCanMoveArmiesBetween() {
        // Test adjacent territories - should be able to move armies
        assertTrue(mapManager.canMoveArmiesBetween("alaska", "alberta"));
        assertTrue(mapManager.canMoveArmiesBetween("cina", "mongolia"));
        assertTrue(mapManager.canMoveArmiesBetween("brasile", "venezuela"));

        // Test non-adjacent territories - should not be able to move armies
        assertFalse(mapManager.canMoveArmiesBetween("alaska", "egitto"));
        assertFalse(mapManager.canMoveArmiesBetween("islanda", "giappone"));
        assertFalse(mapManager.canMoveArmiesBetween("australia_orientale", "argentina"));
    }
/*
    @Test
    void testGetContinentArmyBonus() {
        // Test continent army bonuses from JSON
        assertEquals(5, mapManager.getContinentArmyBonus("europa"));
        assertEquals(3, mapManager.getContinentArmyBonus("africa"));
        assertEquals(7, mapManager.getContinentArmyBonus("asia"));
        assertEquals(2, mapManager.getContinentArmyBonus("oceania"));
        assertEquals(5, mapManager.getContinentArmyBonus("america_settentrionale"));
        assertEquals(2, mapManager.getContinentArmyBonus("america_meridionale"));
    }*/

    @Test
    void testGetTerritoryValue() {
        // Test specific territory values from JSON
        assertEquals(3, mapManager.getTerritoryValue("alaska"));
        assertEquals(3, mapManager.getTerritoryValue("islanda"));
        assertEquals(7, mapManager.getTerritoryValue("cina"));
        assertEquals(6, mapManager.getTerritoryValue("ucraina"));
        assertEquals(4, mapManager.getTerritoryValue("egitto"));
        assertEquals(2, mapManager.getTerritoryValue("giappone"));
        assertEquals(2, mapManager.getTerritoryValue("venezuela"));
        assertEquals(2, mapManager.getTerritoryValue("australia_orientale"));
    }

    @Test
    void testSpecificTerritoryConnections() {
        // Test intercontinental connections
        assertTrue(mapManager.canAttackTerritory("kamchatka", "alaska"));
        assertTrue(mapManager.canAttackTerritory("groenlandia", "islanda"));
        assertTrue(mapManager.canAttackTerritory("brasile", "africa_settentrionale"));
        assertTrue(mapManager.canAttackTerritory("siam", "indonesia"));

        // Verify these are bidirectional
        assertTrue(mapManager.canAttackTerritory("alaska", "kamchatka"));
        assertTrue(mapManager.canAttackTerritory("islanda", "groenlandia"));
        assertTrue(mapManager.canAttackTerritory("africa_settentrionale", "brasile"));
        assertTrue(mapManager.canAttackTerritory("indonesia", "siam"));
    }

    @Test
    void testEdgeCasesTerritories() {
        // Test territories with few neighbors
        List<String> madagascarNeighbors = mapManager.getAdjacentTerritories("madagascar");
        assertEquals(2, madagascarNeighbors.size());
        assertTrue(madagascarNeighbors.contains("africa_orientale"));
        assertTrue(madagascarNeighbors.contains("africa_meridionale"));

        List<String> giapponeNeighbors = mapManager.getAdjacentTerritories("giappone");
        assertEquals(2, giapponeNeighbors.size());
        assertTrue(giapponeNeighbors.contains("kamchatka"));
        assertTrue(giapponeNeighbors.contains("mongolia"));

        List<String> australiaOrientaleNeighbors = mapManager.getAdjacentTerritories("australia_orientale");
        assertEquals(2, australiaOrientaleNeighbors.size());
        assertTrue(australiaOrientaleNeighbors.contains("australia_occidentale"));
        assertTrue(australiaOrientaleNeighbors.contains("nuova_guinea"));
    }
}
