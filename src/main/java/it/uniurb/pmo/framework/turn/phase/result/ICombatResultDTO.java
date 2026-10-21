package it.uniurb.pmo.framework.turn.phase.result;

import it.uniurb.pmo.framework.turn.phase.IPhasePayload;

import java.util.List;
import java.util.Map;

public interface ICombatResultDTO extends IPhasePayload {

    String attackerZone();
    String defenderZone();
    List<Integer> attackerRolls();
    List<Integer> defenderRolls();
    int attackerLosses();
    int defenderLosses();
    boolean territoryConquered();
    Map<String, List<String>> possiblePlayerTargets();

    default List<String> possibleTargets() {
        return this.possiblePlayerTargets().keySet().stream().sorted().toList();
    }

    default List<String> possibleStartingZones(String targetZone) {
        return this.possiblePlayerTargets().getOrDefault(targetZone, List.of());
    }

}
