package it.uniurb.pmo.variants.risikonew.turn.phase.request;

import it.uniurb.pmo.framework.turn.phase.request.IAttackRequestDTO;

import java.util.List;
import java.util.Map;

public record AttackRequestRisikoNewDTO(Map<String, List<String>> possiblePlayerTargets) implements IAttackRequestDTO {

    @Override
    public List<String> possibleTargets() {
        return possiblePlayerTargets.keySet().stream().sorted().toList();
    }

    @Override
    public List<String> possibleStartingZones(String targetZone) {
        return possiblePlayerTargets.get(targetZone).stream().toList();
    }

}
