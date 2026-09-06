package it.uniurb.pmo.variants.risikonew.turn.dto;

import it.uniurb.pmo.framework.players.ITokenType;
import it.uniurb.pmo.framework.turn.dto.IDeployChoiceDTO;
import it.uniurb.pmo.variants.risikonew.utils.ERisikoNewToken;

import java.util.Map;
import java.util.stream.Collectors;

public record DeployChoiceRisikoNewDTO(Map<String, Integer> deployment) implements IDeployChoiceDTO {

    @Override
    public Map<String, Map<ITokenType, Integer>> deployedTokenByZone() {
        return deployment.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> Map.of(ERisikoNewToken.TANK, e.getValue())
                ));
    }
}

