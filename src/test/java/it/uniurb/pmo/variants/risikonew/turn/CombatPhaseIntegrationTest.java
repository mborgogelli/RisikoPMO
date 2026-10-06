package it.uniurb.pmo.variants.risikonew.turn;

import it.uniurb.pmo.variants.risikonew.utils.RisikoNewTestSetup;
import org.junit.jupiter.api.BeforeEach;


public class CombatPhaseIntegrationTest extends RisikoNewTestSetup {

    @BeforeEach
    public void setUp() {
        super.setUpRisikoNew();
    }
/*
    @Test
    @DisplayName("Verifica che i dati nel DTO di richiesta siano coerenti con quelli del model.")
    public void testAttackRequestContainsCorrectData() {
        IPlayer attacker = players.getFirst();
        IGameCoordinatorRisikoNew phaseCoordinator = spy(super.gameCoordinator);
        List<String> ownedZones = mediator.getZonesOwnedBy(attacker);

        this.assignAllTanksToZones(attacker);

        List<String> possibleTargetsByModel = this.getAllPossibleTargets(ownedZones);
        CombatPhase combatPhase = new CombatPhase(mediator);
        combatPhase.playPhase(attacker);

        ArgumentCaptor<AttackRequestRisikoNewDTO> captor = ArgumentCaptor.forClass(AttackRequestRisikoNewDTO.class);
        verify(phaseCoordinator).sendAttackRequest(captor.capture());

        AttackRequestRisikoNewDTO request = captor.getValue();

        assertEquals(attacker.getName(), request.player().getName());
        assertEquals(attacker.getColor(), request.player().getColor());
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
*/
}
