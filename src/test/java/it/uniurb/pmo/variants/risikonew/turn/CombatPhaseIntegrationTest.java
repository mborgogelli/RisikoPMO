package it.uniurb.pmo.variants.risikonew.turn;

import it.uniurb.pmo.framework.players.IPlayer;
import it.uniurb.pmo.variants.risikonew.turn.gamecoordinator.IGameCoordinatorRisikoNew;
import it.uniurb.pmo.variants.risikonew.turn.phase_combat.CombatPhase;
import it.uniurb.pmo.variants.risikonew.utils.RisikoNewTestSetup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CombatPhaseIntegrationTest extends RisikoNewTestSetup {

    IGameCoordinatorRisikoNew gameCoordinator;

    @BeforeEach
    public void setUp() {
        super.setUpRisikoNew();
        this.gameCoordinator = super.getGameCoordinator();
    }

    @Test
    public void testTargetAcquisition() {
        IPlayer attacker = players.getFirst();

        CombatPhase combatPhase = new CombatPhase(mediator,this.gameCoordinator);
        combatPhase.playPhase(attacker);
    }
}
