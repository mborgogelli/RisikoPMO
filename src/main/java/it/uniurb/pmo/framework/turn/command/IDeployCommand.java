package it.uniurb.pmo.framework.turn.command;

import it.uniurb.pmo.framework.players.ITokenType;

import java.util.Map;

public interface IDeployCommand<T extends ITokenType> extends IGameCommand {

    T tokenType();

    Map<String, Integer> deployment();
}
