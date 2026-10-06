package it.uniurb.pmo.framework.turn.command;

import it.uniurb.pmo.framework.players.ITokenType;

public interface IDefendCommand <T extends ITokenType> extends IGameCommand{

    T getTokenType();

    int defenderTokens();
}
