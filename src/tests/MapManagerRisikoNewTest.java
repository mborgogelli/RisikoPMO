package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.board.IZone;
import model.versions.risikockassic.MapManagerRisikoNew;

/**
 * Classe di test per MapManagerRisikoNew che verifica tutte le funzionalità
 * del gestore della mappa di gioco per la versione classica di Risiko.
 */
class MapManagerRisikoNewTest {

    private MapManagerRisikoNew mapManager;

    /**
     * Metodo di setup eseguito prima di ogni test.
     * Inizializza una nuova istanza del MapManager e configura il gioco.
     */
    @BeforeEach
    void setUp() {
        // Ottiene l'istanza singleton del MapManager
        mapManager = MapManagerRisikoNew.getInstance();
        // Resetta l'istanza per garantire uno stato pulito
        mapManager.resetInstance();
        // Ottiene una nuova istanza e inizializza il gioco
        mapManager = MapManagerRisikoNew.getInstance();
        mapManager.initializeGame();
    }
    
    @Test
    void testGameVersion() {
		// Verifica che la versione del gioco sia RISIKOCLASSIC
		assertEquals("RISIKOCLASSIC", mapManager.getGameVersion().toString());
	}
    
    /**
     * Testa il pattern Singleton verificando che getInstance()
     * restituisca sempre la stessa istanza.
     */
    @Test
    void testGetInstance() {
        MapManagerRisikoNew instance1 = MapManagerRisikoNew.getInstance();
        MapManagerRisikoNew instance2 = MapManagerRisikoNew.getInstance();

        // Verifica che l'istanza non sia null
        assertNotNull(instance1);
        // Verifica che le due chiamate restituiscano la stessa istanza
        assertSame(instance1, instance2);
    }

    /**
     * Testa la funzionalità di reset dell'istanza singleton.
     * Dopo il reset, getInstance() dovrebbe restituire una nuova istanza.
     */
    @Test
    void testResetInstance() {
        MapManagerRisikoNew instance1 = MapManagerRisikoNew.getInstance();
        instance1.resetInstance();
        MapManagerRisikoNew instance2 = MapManagerRisikoNew.getInstance();

        // Verifica che dopo il reset si ottenga una nuova istanza
        assertNotSame(instance1, instance2);
    }

    /**
     * Testa il recupero di tutti i continenti della mappa.
     * Verifica che ci siano esattamente 6 continenti con i nomi corretti.
     */
    @Test
    void testGetAllContinents() {
        List<IZone> continents = mapManager.getAllContinents();

        // Verifica che la lista non sia null e contenga 6 continenti
        assertNotNull(continents);
        assertEquals(6, continents.size());
        
        // Estrae i nomi dei continenti per la verifica
        List<String> continentNames = continents.stream()
            .map(IZone::getName)
            .toList();
        
        // Verifica che tutti i continenti del gioco classico siano presenti
        assertTrue(continentNames.contains("europa"));
        assertTrue(continentNames.contains("africa"));
        assertTrue(continentNames.contains("asia"));
        assertTrue(continentNames.contains("oceania"));
        assertTrue(continentNames.contains("america_settentrionale"));
        assertTrue(continentNames.contains("america_meridionale"));
    }

    /**
     * Testa la ricerca di territori esistenti per nome.
     * Verifica che i territori vengano trovati correttamente.
     */
    @Test
    void testFindTerritoryByName() {
        // Testa territori esistenti di diversi continenti
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

    /**
     * Testa la ricerca di un territorio inesistente.
     * Dovrebbe restituire null quando il territorio non esiste.
     */
    @Test
    void testFindTerritoryByNameNotFound() {
        IZone nonExistent = mapManager.findTerritoryByName("territorio_inesistente");
        assertNull(nonExistent);
    }

    /**
     * Testa la ricerca del continente di appartenenza per diversi territori.
     * Verifica che ogni territorio sia correttamente associato al suo continente.
     */
    @Test
    void testFindContinentOfTerritory() {
        // Testa territori di diversi continenti
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

    /**
     * Testa il recupero dei territori adiacenti per vari territori chiave.
     * Verifica che le connessioni della mappa siano corrette.
     */
    @Test
    void testGetAdjacentTerritories() {
        // Testa i vicini dell'Alaska (connessione intercontinentale importante)
        List<String> alaskaNeighbors = mapManager.getAdjacentTerritories("alaska");
        assertNotNull(alaskaNeighbors);
        assertEquals(3, alaskaNeighbors.size());
        assertTrue(alaskaNeighbors.contains("kamchatka"));
        assertTrue(alaskaNeighbors.contains("territori_del_nord_ovest"));
        assertTrue(alaskaNeighbors.contains("alberta"));

        // Testa i vicini della Cina (territorio con molte connessioni)
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

        // Testa i vicini dell'Islanda (territorio insulare)
        List<String> islandaNeighbors = mapManager.getAdjacentTerritories("islanda");
        assertNotNull(islandaNeighbors);
        assertEquals(3, islandaNeighbors.size());
        assertTrue(islandaNeighbors.contains("gran_bretagna"));
        assertTrue(islandaNeighbors.contains("scandinavia"));
        assertTrue(islandaNeighbors.contains("groenlandia"));
    }

    /**
     * Testa la possibilità di raggiungere territori adiacenti e non adiacenti.
     */
    @Test
    void testCanReachTerritory() {
        // Testa territori adiacenti - dovrebbe essere possibile attaccare
        assertTrue(mapManager.canMoveBetween("alaska", "alberta"));
        assertTrue(mapManager.canMoveBetween("alaska", "kamchatka"));
        assertTrue(mapManager.canMoveBetween("cina", "india"));
        assertTrue(mapManager.canMoveBetween("islanda", "gran_bretagna"));

        // Testa territori non adiacenti - non dovrebbe essere possibile attaccare
        assertFalse(mapManager.canMoveBetween("alaska", "brasile"));
        assertFalse(mapManager.canMoveBetween("islanda", "australia_orientale"));
        assertFalse(mapManager.canMoveBetween("egitto", "groenlandia"));
    }


    /**
     * Testa i bonus in armate per il controllo completo dei continenti.
     * Ogni continente ha un valore specifico nel gioco classico.
     */
    @Test
    void testGetContinentArmyBonus() {
        assertEquals(5, mapManager.getContinentArmyBonus("europa"));
        assertEquals(3, mapManager.getContinentArmyBonus("africa"));
        assertEquals(7, mapManager.getContinentArmyBonus("asia"));
        assertEquals(2, mapManager.getContinentArmyBonus("oceania"));
        assertEquals(5, mapManager.getContinentArmyBonus("america_settentrionale"));
        assertEquals(2, mapManager.getContinentArmyBonus("america_meridionale"));
    }

    /**
     * Testa i valori strategici dei singoli territori.
     * Alcuni territori hanno valori più alti per la loro posizione strategica.
     */
    @Test
    void testGetTerritoryValues() {
        assertEquals(3, mapManager.getTerritoryValue("alaska"));
        assertEquals(3, mapManager.getTerritoryValue("islanda"));
        assertEquals(7, mapManager.getTerritoryValue("cina"));
        assertEquals(6, mapManager.getTerritoryValue("ucraina"));
        assertEquals(4, mapManager.getTerritoryValue("egitto"));
        assertEquals(2, mapManager.getTerritoryValue("giappone"));
        assertEquals(2, mapManager.getTerritoryValue("venezuela"));
        assertEquals(2, mapManager.getTerritoryValue("australia_orientale"));
    }

    /**
     * Testa specifiche connessioni intercontinentali cruciali nel gioco.
     * Verifica che le connessioni strategiche tra continenti funzionino correttamente.
     */
    @Test
    void testSpecificTerritoryConnections() {
        // Testa connessioni intercontinentali importanti
        assertTrue(mapManager.canMoveBetween("kamchatka", "alaska"));
        assertTrue(mapManager.canMoveBetween("groenlandia", "islanda"));
        assertTrue(mapManager.canMoveBetween("brasile", "africa_settentrionale"));
        assertTrue(mapManager.canMoveBetween("siam", "indonesia"));

        // Verifica che queste connessioni siano bidirezionali
        assertTrue(mapManager.canMoveBetween("alaska", "kamchatka"));
        assertTrue(mapManager.canMoveBetween("islanda", "groenlandia"));
        assertTrue(mapManager.canMoveBetween("africa_settentrionale", "brasile"));
        assertTrue(mapManager.canMoveBetween("indonesia", "siam"));
    }

    /**
     * Testa territori con poche connessioni.
     * Verifica territori insulari o isolati.
     */
    @Test
    void testEdgeCasesTerritories() {
        // Testa il Madagascar (territorio isolato con solo 2 connessioni)
        List<String> madagascarNeighbors = mapManager.getAdjacentTerritories("madagascar");
        assertEquals(2, madagascarNeighbors.size());
        assertTrue(madagascarNeighbors.contains("africa_orientale"));
        assertTrue(madagascarNeighbors.contains("africa_meridionale"));

        // Testa il Giappone (territorio insulare)
        List<String> giapponeNeighbors = mapManager.getAdjacentTerritories("giappone");
        assertEquals(2, giapponeNeighbors.size());
        assertTrue(giapponeNeighbors.contains("kamchatka"));
        assertTrue(giapponeNeighbors.contains("mongolia"));

        // Testa l'Australia Orientale (territorio insulare dell'Oceania)
        List<String> australiaOrientaleNeighbors = mapManager.getAdjacentTerritories("australia_orientale");
        assertEquals(2, australiaOrientaleNeighbors.size());
        assertTrue(australiaOrientaleNeighbors.contains("australia_occidentale"));
        assertTrue(australiaOrientaleNeighbors.contains("nuova_guinea"));
    }
}
