package it.uniurb.pmo.variants.risikonew.turn.phase.request;

import it.uniurb.pmo.framework.players.ITokenType;
import it.uniurb.pmo.framework.turn.phase.request.IDeployRequestDTO;
import it.uniurb.pmo.variants.risikonew.utils.ERisikoNewToken;

import java.util.List;
import java.util.Map;

public record DeployRequestRisikoNewDTO(List<String> deployableZones, int tanksToDeploy) implements IDeployRequestDTO {

    @Override
    public Map<ITokenType, Integer> tokenToDeploy() {
        return Map.of(ERisikoNewToken.TANK, tanksToDeploy);
    }

}
