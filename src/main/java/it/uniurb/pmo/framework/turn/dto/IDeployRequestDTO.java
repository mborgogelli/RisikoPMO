package it.uniurb.pmo.framework.turn.dto;

import it.uniurb.pmo.framework.players.ITokenType;
import it.uniurb.pmo.framework.turn.IPlayerRequestDTO;

import java.util.List;
import java.util.Map;

public interface IDeployRequestDTO extends IPlayerRequestDTO {

    List<String> deployableZones();

    Map<ITokenType, Integer> tokenDeploy();
}
