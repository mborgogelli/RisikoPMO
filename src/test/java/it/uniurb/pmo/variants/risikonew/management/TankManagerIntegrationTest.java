package it.uniurb.pmo.variants.risikonew.management;

import it.uniurb.pmo.framework.players.IPlayer;
import it.uniurb.pmo.framework.players.Player;
import it.uniurb.pmo.variants.risikonew.GameFactoryRisikoNew;
import it.uniurb.pmo.variants.risikonew.management.interfaces.IMapManagerRisikoNew;
import it.uniurb.pmo.variants.risikonew.management.interfaces.ITankManager;
import it.uniurb.pmo.variants.risikonew.utils.RisikoNewTestSetup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TankManagerIntegrationTest extends RisikoNewTestSetup {

    @Test
    @DisplayName("Rifiuta l'inizializzazione con meno di tre giocatori")
    void testInitializeGame_TooFewPlayers() {
        GameFactoryRisikoNew freshFactory = new GameFactoryRisikoNew();
        ITankManager freshTokenManager = freshFactory.getManagers().stream()
            .filter(ITankManager.class::isInstance)
            .map(ITankManager.class::cast)
            .findFirst()
            .orElseThrow();
        IMapManagerRisikoNew freshMapManager = freshFactory.getManagers().stream()
            .filter(IMapManagerRisikoNew.class::isInstance)
            .map(IMapManagerRisikoNew.class::cast)
            .findFirst()
            .orElseThrow();
        List<IPlayer> twoPlayers = List.of(new Player("P1"), new Player("P2"));
        freshMapManager.initializeGame(twoPlayers);

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> freshTokenManager.initializeGame(twoPlayers),
            "Should throw exception for too few players"
        );
        assertEquals("Invalid number of players: 2", exception.getMessage());
    }
    
   @Test
    @DisplayName("Rifiuta una seconda inizializzazione del gestore dei carri")
    void testInitializeGame_AlreadyInitialized() {
        // tokenManager è già inizializzato dal setup
        if (tankManager.isReady()) {
            IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> tankManager.initializeGame(players),
                "Should throw exception when already initialized"
            );
            assertEquals("TankManager is already initialized.", exception.getMessage());
        }
    }
    
    @Test
    @DisplayName("Verifica l'assegnazione e il totale dei carri dei giocatori")
    void testTanksAssignment() {
        int myAvailableTanks = tankManager.getPlayerTank(players.getFirst());
        int myTerritoriesCount = mapManager.getTerritoriesOwnedBy(players.getFirst()).size();
        int totalDeployed = tankManager.getTotalDeployed(players.getFirst());

        assertEquals(30, myAvailableTanks + myTerritoriesCount); // 30 tank totali per 4 giocatori
        assertEquals(myTerritoriesCount, totalDeployed);
    }
}
