package it.uniurb.pmo.variants.risikonew.turn;

import it.uniurb.pmo.framework.board.IZone;
import it.uniurb.pmo.framework.management.interfaces.IMapManager;
import it.uniurb.pmo.framework.players.IPlayer;
import it.uniurb.pmo.framework.players.Player;
import it.uniurb.pmo.framework.turn.dto.*;
import it.uniurb.pmo.variants.risikonew.GameFactoryRisikoNew;
import it.uniurb.pmo.variants.risikonew.management.MediatorRisikoNew;
import it.uniurb.pmo.variants.risikonew.management.interfaces.IMapManagerRisikoNew;
import it.uniurb.pmo.variants.risikonew.management.interfaces.IMediatorRisikoNew;
import it.uniurb.pmo.variants.risikonew.management.interfaces.ITankManager;
import it.uniurb.pmo.variants.risikonew.turn.gamecoordinator.GameCoordinatorRisikoNew;
import it.uniurb.pmo.variants.risikonew.turn.gamecoordinator.IGameCoordinatorRisikoNew;
import it.uniurb.pmo.variants.risikonew.turn.phase_initialplacement.InitialDeployRequestDTO;
import it.uniurb.pmo.variants.risikonew.turn.phase_initialplacement.InitialDeployResponseDTO;
import it.uniurb.pmo.variants.risikonew.turn.phase_initialplacement.InitialPlacementPhase;
import it.uniurb.pmo.variants.risikonew.utils.RisikoNewTestSetup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class InitialPlacementPhaseIntegrationTest extends RisikoNewTestSetup {

    private List<IPlayer> players;
    private IMapManagerRisikoNew mapManager;
    private ITankManager tankManager;
    private IMediatorRisikoNew mediator;
    private IGameCoordinatorRisikoNew gameCoordinator;

    @BeforeEach
    public void setUp() {
        super.setUpRisikoNew();
        this.initManagers();
    }

    private void initManagers() {
        this.players = super.getPlayers();
        this.mapManager = resolveManager(IMapManagerRisikoNew.class);
        this.tankManager = resolveManager(ITankManager.class);
        this.mediator = super.getMediator();
        this.gameCoordinator = super.getGameCoordinator();
    }

    @Test
    @DisplayName("Integration: Deploy tanks until none are left")
    void testPlayPhaseDeploysAllTanks() {

        InitialPlacementPhase phase = new InitialPlacementPhase(this.mediator, this.gameCoordinator);

        while (haveRemainingTanks(mediator)) {
            for (IPlayer player : this.players) {
                if (mediator.getPlayerTank(player) > 0) {
                    phase.playPhase(player);
                    System.out.println(player.getName() + ": " + mediator.getPlayerTank(player) + " tanks remaining");
                }
            }
        }

        for (IPlayer player : this.players) {
            assertTrue(this.mediator.getPlayerTank(player) == 0);
            assertTrue(this.players.size() == 4 && this.tankManager.getTotalDeployed(player) == 30);
        }

    }

   @Test
    @DisplayName("Integration: Deploy less than 3 tanks when fewer available")
    void testPlayPhasePlayerDeployLessThan3Tanks() {

        IPlayer player = players.getFirst();
        int remainingBefore = mediator.getPlayerTank(player);

        // Il giocatore ha esattamente 2 tanks residui da assegnare
        int targetRemaining = 2;
        if (remainingBefore > targetRemaining) {
            tankManager.removeTank(player, remainingBefore - targetRemaining);
        } else if (remainingBefore < targetRemaining) {
            tankManager.assignTank(player, targetRemaining - remainingBefore);
        }

        int toDeploy = Math.min(3, mediator.getPlayerTank(player));

        List<String> ownedZones = mediator.getTerritoriesOwnedBy(player);
        // Il giocatore deve assegnare i tanks rimanenti alla zona con il nome "più piccolo"
        String expectedZone = ownedZones.stream().min(String::compareTo).orElseThrow();
        // Recupera il numero di tanks nella zona prima del deploy
        int zoneBefore = mediator.getZoneTank(expectedZone);

        InitialPlacementPhase phase = new InitialPlacementPhase(mediator, new GameCoordinatorRisikoNew());
        phase.playPhase(player);

        // Verifica che siano stati assegnati meno di 3 tanks
        assertTrue(toDeploy < 3);
        // Verifica che il numero di tanks nella zona sia aumentato del numero di tanks assegnati
        assertEquals(zoneBefore + toDeploy, mediator.getZoneTank(expectedZone));
        // Verifica che il numero di tanks del giocatore sia diminuito del numero di tanks assegnati
        assertEquals(targetRemaining - toDeploy, mediator.getPlayerTank(player));
    }

    @Test
    @DisplayName("Should throw RuntimeException when invalid sum is provided")
    public void testDeployInvalidSumThrows() {
        IPlayer player = this.players.getFirst();

        List<String> ownedZones = mediator.getZonesOwnedBy(player);
        String deployZone = ownedZones.stream().min(String::compareTo).orElseThrow();

        Map<String,Integer> response = Map.of(deployZone, 2);
        InitialPlacementPhase phase = new InitialPlacementPhase(mediator, new CoordinatorStub(response));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> phase.playPhase(player));
        assertEquals("You must deploy 3 tanks.", ex.getMessage());
    }

    @Test
    @DisplayName("Should throw RuntimeException when no tanks available")
    public void testThrowExceptionWhenNoTanks() {
        IPlayer player = this.players.get(2);
        int currentTanks = mediator.getPlayerTank(player);
        tankManager.removeTank(player, currentTanks);

        InitialPlacementPhase phase = new InitialPlacementPhase(mediator, new GameCoordinatorRisikoNew());

        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> phase.playPhase(player));

        assertEquals("Not enough tanks to deploy.", exception.getMessage());
    }

    @Test
    @DisplayName("Should deploy exactly MAX_DEPLOYABLE when exactly available")
    public void testDeployExactlyMaxDeployable() {
        IPlayer player = this.players.get(3);
        int currentTanks = mediator.getPlayerTank(player);
        if (currentTanks > 3) {
            tankManager.removeTank(player, currentTanks - 3);
        } else if (currentTanks < 3) {
            tankManager.assignTank(player, 3 - currentTanks);
        }

        List<String> ownedZones = mediator.getZonesOwnedBy(player);
        String deployZone = ownedZones.stream().min(String::compareTo).orElseThrow();
        int zonesTanksBefore = mediator.getZoneTank(deployZone);

        InitialPlacementPhase phase = new InitialPlacementPhase(mediator, new GameCoordinatorRisikoNew());
        phase.playPhase(player);

        int tanksAfter = mediator.getPlayerTank(player);
        int zonesTanksAfter = mediator.getZoneTank(deployZone);

        assertEquals(0, tanksAfter);
        assertEquals(zonesTanksBefore + 3, zonesTanksAfter);
    }

    private boolean haveRemainingTanks(IMediatorRisikoNew mediator) {
        return this.players.stream().anyMatch(p -> mediator.getPlayerTank(p) > 0);
    }

    /**
         * Inner class stub for IGameCoordinatorRisikoNew.
         */
        private record CoordinatorStub(Map<String, Integer> response) implements IGameCoordinatorRisikoNew {

        @Override
        public InitialDeployResponseDTO sendInitialPlacementRequest(InitialDeployRequestDTO request) {
            return new InitialDeployResponseDTO(response);
        }

        @Override
        public DeployResponseDTO sendDeployRequest(IDeployRequestDTO request) {
            return null;
        }

        @Override
        public AttackChoiceDTO sendAttackRequest(AttackRequestDTO request) {
            return null;
        }

        @Override
        public FortifyChoiceDTO sendFortifyRequest(FortifyRequestDTO request) {
            return null;
        }
    }
}
