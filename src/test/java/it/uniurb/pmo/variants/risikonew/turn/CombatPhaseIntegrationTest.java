package it.uniurb.pmo.variants.risikonew.turn;

import it.uniurb.pmo.framework.players.IPlayer;
import it.uniurb.pmo.variants.risikonew.turn.dto.AttackRequestRisikoNewDTO;
import it.uniurb.pmo.variants.risikonew.turn.gamecoordinator.IGameCoordinatorRisikoNew;
import it.uniurb.pmo.variants.risikonew.turn.phase_combat.CombatPhase;
import it.uniurb.pmo.variants.risikonew.utils.RisikoNewTestSetup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;


public class CombatPhaseIntegrationTest extends RisikoNewTestSetup {

    @BeforeEach
    public void setUp() {
        super.setUpRisikoNew();
    }

    @Test
    @DisplayName("Verifica che i dati nel DTO di richiesta siano coerenti con quelli del model.")
    public void testAttackRequestContainsCorrectData() {
        IPlayer attacker = players.getFirst();
        IGameCoordinatorRisikoNew phaseCoordinator = spy(super.gameCoordinator);
        List<String> ownedZones = mediator.getZonesOwnedBy(attacker);

        this.assignAllTanksToZones(attacker);

        List<String> possibleTargetsByModel = this.getAllPossibleTargets(ownedZones);
        CombatPhase combatPhase = new CombatPhase(mediator,phaseCoordinator);
        combatPhase.playPhase(attacker);

        ArgumentCaptor<AttackRequestRisikoNewDTO> captor = ArgumentCaptor.forClass(AttackRequestRisikoNewDTO.class);
        verify(phaseCoordinator).sendAttackRequest(captor.capture());

        AttackRequestRisikoNewDTO request = captor.getValue();

        assertEquals(attacker.getName(), request.playerName());
        assertEquals(attacker.getColor(), request.playerColor());
        assertTrue(possibleTargetsByModel.size() == request.possibleTargets().size() &&
                   possibleTargetsByModel.containsAll(request.possibleTargets()));
        for(String target : possibleTargetsByModel){
            List<String> possibleStartingZonesByModel = this.getPossibleStartingZones(target, ownedZones);
            assertEquals(possibleStartingZonesByModel, request.possibleStartingZones(target));
        }
    }

    private void assignAllTanksToZones(IPlayer player) {
        List<String> playerTerritories = mediator.getZonesOwnedBy(player);
        while(mediator.getPlayerTank(player) > 0) {
            mediator.deployTank(player,this.getRandomZone(playerTerritories),1);
        }
    }

    private List<String> getAllPossibleTargets(List<String> ownedZones) {
        return ownedZones.stream()
                .filter(territory -> this.mediator.getZoneTank(territory) > 1)
                .flatMap(territory -> this.mediator.getNeighboursOf(territory).stream())
                .distinct()
                .filter(territory -> !ownedZones.contains(territory))
                .toList();
    }

    private List<String> getPossibleStartingZones(String target, List<String> ownedZones) {
        return this.mediator.getNeighboursOf(target).stream()
                .filter(neighbour -> ownedZones.contains(neighbour))
                .filter(territory -> this.mediator.getZoneTank(territory) > 1)
                .toList();
    }

    private String getRandomZone(List<String> ownedZones) {
        return ownedZones.get(new Random().nextInt(ownedZones.size()));
    }

}
