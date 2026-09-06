package it.uniurb.pmo.variants.risikonew.turn;

import it.uniurb.pmo.framework.players.IPlayer;
import it.uniurb.pmo.variants.risikonew.turn.gamecoordinator.IGameCoordinatorRisikoNew;
import it.uniurb.pmo.variants.risikonew.turn.phase_combat.CombatPhase;
import it.uniurb.pmo.variants.risikonew.utils.RisikoNewTestSetup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;

import static org.mockito.Mockito.spy;

public class CombatPhaseIntegrationTest extends RisikoNewTestSetup {

    @BeforeEach
    public void setUp() {
        super.setUpRisikoNew();
    }

    @Test
    public void testTargetAcquisition() {
        IPlayer attacker = players.getFirst();
        IGameCoordinatorRisikoNew phaseCoordinator = spy(super.gameCoordinator);
        List<String> playerTerritories = mediator.getZonesOwnedBy(attacker);

        while(mediator.getPlayerTank(attacker) > 0) {
            mediator.deployTank(attacker,playerTerritories.get(new Random().nextInt(playerTerritories.size())),1);
        }

        playerTerritories.stream().filter(territory -> this.mediator.getZoneTank(territory) > 1).forEach(territory -> System.out.println(territory + "->" + this.mediator.getNeighboursOf(territory)));

        CombatPhase combatPhase = new CombatPhase(mediator,phaseCoordinator);
        combatPhase.playPhase(attacker);
    }
}
