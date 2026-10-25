package it.uniurb.pmo.framework.turn.phase.request;

import it.uniurb.pmo.framework.players.ITokenType;

import java.util.List;
import java.util.Map;

public interface IMovementRequest extends IGameRequest{

    Map<String,List<String>> destinations();

    Map<String, Map<ITokenType,Integer>> moveableTokens();
}
