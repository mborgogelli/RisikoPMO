package it.uniurb.pmo.variants.risikonew.turn.phase_initialplacement;

import it.uniurb.pmo.framework.players.ITokenType;
import it.uniurb.pmo.framework.turn.dto.IDeployResponseDTO;
import it.uniurb.pmo.variants.risikonew.utils.ERisikoNewToken;

import java.util.Map;
import java.util.stream.Collectors;

public record DeployResponseRisikoNewDTO(Map<String, Integer> deployment) implements IDeployResponseDTO {

    @Override
    public Map<String, Map<ITokenType, Integer>> deployedTokenByZone() {
        return deployment.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> Map.of(ERisikoNewToken.TANK, e.getValue())
                ));
    }
}

