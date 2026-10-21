package it.uniurb.pmo.framework.turn.phase.request;

import it.uniurb.pmo.framework.players.ITokenType;

import java.util.List;
import java.util.Map;

public interface IDeployRequestDTO extends IGameRequest {

    List<String> deployableZones();

    Map<ITokenType, Integer> tokenToDeploy();
}
