package it.uniurb.pmo.framework.turn.phase.request;

import it.uniurb.pmo.framework.players.ITokenType;

public interface IDefendRequestDTO extends IGameRequest {

    String defenderName();

    ITokenType defenderTokenType();

    int maxDefenderTokens();
}
