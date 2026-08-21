package it.uniurb.pmo.variants.risikonew.turn.phase_initialplacement;

import it.uniurb.pmo.framework.players.ITokenType;
import it.uniurb.pmo.framework.turn.dto.IDeployRequestDTO;
import it.uniurb.pmo.framework.utils.EColors;
import it.uniurb.pmo.variants.risikonew.utils.ERisikoNewToken;
import java.util.List;
import java.util.Map;

public record DeployRequestRisikoNewDTO(String playerName, EColors playerColor, List<String> deployableZones, Map<ITokenType, Integer> tokenToDeploy) implements IDeployRequestDTO {

    public DeployRequestRisikoNewDTO(String playerName, EColors playerColor, List<String> deployableZones, int tokensToDeploy) {
        this(playerName, playerColor,deployableZones, Map.of(ERisikoNewToken.TANK, tokensToDeploy));
    }
}
