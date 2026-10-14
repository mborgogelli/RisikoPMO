package it.uniurb.pmo.framework.turn.dto;

import java.util.List;
import java.util.Map;

public interface ICombatResultDTO extends IAttackRequestDTO {

    String attackerZone();
    String defenderZone();
    List<Integer> attackerRolls();
    List<Integer> defenderRolls();
    int attackerLosses();
    int defenderLosses();
    boolean territoryConquered();
    Map<String, List<String>> possiblePlayerTargets();

    @Override
    default List<String> possibleTargets() {
        return this.possiblePlayerTargets().keySet().stream().sorted().toList();
    }

    @Override
    default List<String> possibleStartingZones(String targetZone) {
        return this.possiblePlayerTargets().getOrDefault(targetZone, List.of());
    }

}
