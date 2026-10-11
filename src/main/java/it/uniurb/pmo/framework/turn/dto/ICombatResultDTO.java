package it.uniurb.pmo.framework.turn.dto;

import java.util.List;

public interface ICombatResultDTO extends IGameState {

    String attackerZone();
    String defenderZone();
    List<Integer> attackerRolls();
    List<Integer> defenderRolls();
    int attackerLosses();
    int defenderLosses();
    boolean territoryConquered();

}
