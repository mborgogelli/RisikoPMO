package it.uniurb.pmo.variants.risikonew.turn.dto;

import it.uniurb.pmo.framework.players.ITokenType;
import it.uniurb.pmo.framework.turn.dto.IDeployRequestDTO;
import it.uniurb.pmo.framework.utils.EColors;
import it.uniurb.pmo.variants.risikonew.utils.ERisikoNewToken;

import java.util.List;
import java.util.Map;

public record DeployRequestRisikoNewDTO(String playerName, EColors playerColor, List<String> deployableZones, int tanksToDeploy) implements IDeployRequestDTO {

    @Override
    public Map<ITokenType, Integer> tokenToDeploy() {
        return Map.of(ERisikoNewToken.TANK, tanksToDeploy);
    }
}
