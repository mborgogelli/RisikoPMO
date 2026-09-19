package it.uniurb.pmo.framework.turn.dto;

import it.uniurb.pmo.framework.players.ITokenType;
import it.uniurb.pmo.framework.turn.event.interfaces.IGameState;

import java.util.List;
import java.util.Map;

public interface IDeployRequestDTO extends IGameState {

    List<String> deployableZones();

    Map<ITokenType, Integer> tokenToDeploy();
}
