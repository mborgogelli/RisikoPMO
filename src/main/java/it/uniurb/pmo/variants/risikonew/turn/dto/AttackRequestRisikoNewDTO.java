package it.uniurb.pmo.variants.risikonew.turn.dto;

import it.uniurb.pmo.framework.turn.dto.IAttackRequestDTO;
import it.uniurb.pmo.framework.utils.EColors;

import java.util.List;
import java.util.Map;

public record AttackRequestRisikoNewDTO(String playerName, EColors playerColor, Map<String, List<String>> possiblePlayerTargets) implements IAttackRequestDTO {

    @Override
    public List<String> possibleTargets() {
        return possiblePlayerTargets.keySet().stream().sorted().toList();
    }

    @Override
    public List<String> possibleStartingZones(String targetZone) {
        return possiblePlayerTargets.get(targetZone).stream().toList();
    }
}
