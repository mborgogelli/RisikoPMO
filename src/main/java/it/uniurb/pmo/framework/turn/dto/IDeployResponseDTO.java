package it.uniurb.pmo.framework.turn.dto;

import it.uniurb.pmo.framework.players.ITokenType;

import java.util.Map;

public interface IDeployResponseDTO {

    Map<String, Map<ITokenType, Integer>> deployedTokenByZone();
}
