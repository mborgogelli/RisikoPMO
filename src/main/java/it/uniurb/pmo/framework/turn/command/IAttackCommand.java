package it.uniurb.pmo.framework.turn.command;

import it.uniurb.pmo.framework.players.ITokenType;

public interface IAttackCommand <T extends ITokenType> extends IGameCommand {

    T tokenType();

    int attackerTokens();

    String attackerZone();

    String defenderZone();
}
