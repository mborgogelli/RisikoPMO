package it.uniurb.pmo.framework.turn.dto;

import java.util.List;

public interface IAttackRequestDTO extends IGameState {

    List<String> possibleTargets();

    List<String> possibleStartingZones(String targetZone);
}
