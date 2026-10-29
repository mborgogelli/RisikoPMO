package it.uniurb.pmo.variants.risikonew.turn;

import it.uniurb.pmo.framework.players.IPlayer;
import it.uniurb.pmo.framework.turn.command.PassCommand;
import it.uniurb.pmo.framework.turn.phase.request.IGameRequest;
import it.uniurb.pmo.framework.turn.phase.result.IPhaseResult;
import it.uniurb.pmo.variants.risikonew.management.interfaces.IMediatorRisikoNew;
import it.uniurb.pmo.variants.risikonew.turn.command.AttackCommandRisikoNew;
import it.uniurb.pmo.variants.risikonew.turn.phase.PhaseCombat;
import it.uniurb.pmo.variants.risikonew.turn.phase.request.AttackRequestRisikoNewDTO;
import it.uniurb.pmo.variants.risikonew.utils.RisikoNewTestSetup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;


public class CombatPhaseIntegrationTest extends RisikoNewTestSetup {

    private IPlayer attacker;
    private IPhaseResult<IGameRequest> result;
    private PhaseCombat phaseCombat;

    @BeforeEach
    public void setUp() {
        super.setUpRisikoNew();
        this.attacker = players.getFirst();
        IMediatorRisikoNew phaseMediator = spy(this.mediator);

        //Costruzione dei territori posseduti dall'attaccante e dei loro vicini
        doReturn(List.of("topolinia", "paperopoli", "centinarola"))
                .when(phaseMediator).getTerritoriesOwnedBy(attacker);

        doReturn(List.of("gondor","contea","valinor","paperopoli"))
                .when(phaseMediator).getNeighboursOf("topolinia");
        doReturn(List.of("centinarola","topolinia","valinor"))
                .when(phaseMediator).getNeighboursOf("paperopoli");
        doReturn(List.of("paperopoli","contea"))
                .when(phaseMediator).getNeighboursOf("centinarola");

        doReturn(List.of("topolinia")).when(phaseMediator).getNeighboursOf("gondor");
        doReturn(List.of("topolinia", "centinarola")).when(phaseMediator).getNeighboursOf("contea");
        doReturn(List.of("topolinia")).when(phaseMediator).getNeighboursOf("valinor");

        doReturn(1).when(phaseMediator).getZoneTank("paperopoli");
        doReturn(3).when(phaseMediator).getZoneTank("topolinia");
        doReturn(2).when(phaseMediator).getZoneTank("centinarola");

        this.phaseCombat = new PhaseCombat(phaseMediator);
        this.result = phaseCombat.playPhase(attacker);
    }

    @Test
    @DisplayName("Calcola i territori attaccabili e i limiti di carri per zona")
    public void testPossibleTargets() {

        assertTrue(result.phasePayload().isPresent());

        AttackRequestRisikoNewDTO attackRequest = (AttackRequestRisikoNewDTO) result.phasePayload().get();
        List<String> targets = attackRequest.possibleTargets();

        assertEquals(3, targets.size());
        assertTrue(targets.containsAll(List.of("gondor", "contea", "valinor")));
        assertFalse(attackRequest.possibleStartingZones("valinor").contains("paperopoli"));
        assertEquals(2, attackRequest.maxDeployableTanks("topolinia"));
        assertEquals(1, attackRequest.maxDeployableTanks("centinarola"));
    }

    @Test
    @DisplayName("Rifiuta un attacco che supera i carri disponibili")
    void testAttackCommand() {

        AttackCommandRisikoNew attackCommand = new AttackCommandRisikoNew(attacker, "topolinia", "gondor", 3);
        assertFalse(phaseCombat.isValidCommand(attackCommand));
    }

    @Test
    @DisplayName("Accetta il comando per terminare la fase di combattimento")
    void testPassCommand() {
        assertTrue(phaseCombat.isValidCommand(new PassCommand()));
    }

}
