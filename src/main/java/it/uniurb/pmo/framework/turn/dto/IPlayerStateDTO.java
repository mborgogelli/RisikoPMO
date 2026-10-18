package it.uniurb.pmo.framework.turn.dto;

import it.uniurb.pmo.framework.players.ITokenType;
import it.uniurb.pmo.framework.players.PlayerTurnStatus;
import java.util.Map;

public interface IPlayerStateDTO extends IPlayerIdentityDTO {

    PlayerTurnStatus playerTurnStatus();

    Map<String, Map<ITokenType, Integer>> playerZoneStatus();

    Map<ITokenType, Integer> playerTokens();
}
