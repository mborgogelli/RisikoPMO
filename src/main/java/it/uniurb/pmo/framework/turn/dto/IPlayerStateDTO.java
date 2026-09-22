package it.uniurb.pmo.framework.turn.dto;

import it.uniurb.pmo.framework.players.ITokenType;
import it.uniurb.pmo.framework.players.PlayerTurnStatus;
import it.uniurb.pmo.framework.turn.event.interfaces.IGameState;
import it.uniurb.pmo.framework.utils.EColors;

import java.util.Map;

public interface IPlayerStateDTO extends IGameState {

    @Override
    String playerName();

    @Override
    EColors playerColor();

    PlayerTurnStatus playerTurnStatus();

    Map<String, Map<ITokenType, Integer>> playerZoneStatus();

}
