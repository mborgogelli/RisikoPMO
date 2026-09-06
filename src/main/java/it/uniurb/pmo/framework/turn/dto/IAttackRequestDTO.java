package it.uniurb.pmo.framework.turn.dto;

import it.uniurb.pmo.framework.turn.IPlayerDataDTO;

import java.util.List;

public interface IAttackRequestDTO extends IPlayerDataDTO {

    List<String> possibleTargets();

    List<String> possibleStartingZones(String targetZone);
}
