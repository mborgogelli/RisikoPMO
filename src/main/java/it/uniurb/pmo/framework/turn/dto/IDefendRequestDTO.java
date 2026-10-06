package it.uniurb.pmo.framework.turn.dto;

import it.uniurb.pmo.framework.players.ITokenType;

public interface IDefendRequestDTO extends IGameState{

    String defenderName();

    ITokenType defenderTokenType();

    int maxDefenderTokens();
}
