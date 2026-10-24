package it.uniurb.pmo.variants.risikonew.turn.phase.request;

import it.uniurb.pmo.framework.turn.phase.request.IAttackRequestDTO;

import java.util.List;
import java.util.Map;

public record AttackRequestRisikoNewDTO(Map<String, Map<String,Integer>> possiblePlayerTargets) implements IAttackRequestDTO {

    @Override
    public List<String> possibleTargets() {
        return possiblePlayerTargets.keySet().stream().sorted().toList();
    }

    @Override
    public List<String> possibleStartingZones(String targetZone) {
        return possiblePlayerTargets.get(targetZone).keySet().stream().sorted().toList();
    }

    public int maxDeployableTanks(String startingZone) {
        return possiblePlayerTargets.values().stream()
                .filter(map -> map.containsKey(startingZone))
                .mapToInt(map -> map.get(startingZone)).max().orElse(0);
    }
}
