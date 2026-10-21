package it.uniurb.pmo.framework.turn.phase.request;

import java.util.List;

public interface IAttackRequestDTO extends IGameRequest {

    List<String> possibleTargets();

    List<String> possibleStartingZones(String targetZone);
}
