package it.uniurb.pmo.variants.risikonew.turn.phase_initialplacement;

import it.uniurb.pmo.framework.players.IPlayer;
import it.uniurb.pmo.framework.players.ITokenType;
import it.uniurb.pmo.framework.turn.dto.IDeployRequestDTO;
import it.uniurb.pmo.framework.utils.EnumColors;
import it.uniurb.pmo.variants.risikonew.utils.ERisikoNewToken;
import java.util.List;
import java.util.Map;

public record InitialDeployRequestDTO(String playerName, EnumColors playerColor, List<String> deployableZones, Map<ITokenType, Integer> tokenDeploy) implements IDeployRequestDTO {

    public InitialDeployRequestDTO(String playerName, EnumColors playerColor, List<String> deployableZones, int tokensToDeploy) {
        this(playerName, playerColor,deployableZones, Map.of(ERisikoNewToken.TANK, tokensToDeploy));
    }
}
