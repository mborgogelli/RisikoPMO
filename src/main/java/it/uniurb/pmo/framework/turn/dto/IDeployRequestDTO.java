package it.uniurb.pmo.framework.turn.dto;

import it.uniurb.pmo.framework.players.ITokenType;
import it.uniurb.pmo.framework.turn.IPlayerDataDTO;

import java.util.List;
import java.util.Map;

public interface IDeployRequestDTO extends IPlayerDataDTO {

    List<String> deployableZones();

    Map<ITokenType, Integer> tokenToDeploy();
}
