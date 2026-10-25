package it.uniurb.pmo.framework.turn.command;

import it.uniurb.pmo.framework.players.ITokenType;

public interface IMoveCommand extends  IGameCommand{

    String source();
    String destination();
    ITokenType tokenType();
    int tokensAmount();
}
