package it.uniurb.pmo.variants.risikonew.turn.phase.result;

import it.uniurb.pmo.framework.turn.phase.result.ICombatResultDTO;

import java.util.List;
import java.util.Map;

public record CombatResultRisikoNewDTO(String attackerTerritory,
                                       String defenderTerritory,
                                       List<Integer> attackerRolls,
                                       List<Integer> defenderRolls,
                                       int attackerLosses,
                                       int defenderLosses,
                                       boolean territoryConquered,
                                       Map<String, List<String>> possiblePlayerTargets
) implements ICombatResultDTO {

    // Per validazione
    public CombatResultRisikoNewDTO {
        if (attackerTerritory == null || attackerTerritory.isBlank()) {
            throw new IllegalArgumentException("Il territorio attaccante non può essere nullo o vuoto.");
        }
        if (defenderTerritory == null || defenderTerritory.isBlank()) {
            throw new IllegalArgumentException("Il territorio difensore non può essere nullo o vuoto.");
        }
        if (attackerRolls == null || defenderRolls == null) {
            throw new IllegalArgumentException("Le liste dei risultati dei dadi non possono essere nulle.");
        }
        if (attackerLosses < 0 || defenderLosses < 0) {
            throw new IllegalArgumentException("Il numero di armate perse non può essere negativo.");
        }

        attackerRolls = List.copyOf(attackerRolls);
        defenderRolls = List.copyOf(defenderRolls);
        possiblePlayerTargets = possiblePlayerTargets.entrySet().stream()
                .collect(java.util.stream.Collectors.toUnmodifiableMap(
                        Map.Entry::getKey,
                        entry -> List.copyOf(entry.getValue())));
    }

    @Override
    public String attackerZone() {
        return attackerTerritory;
    }

    @Override
    public String defenderZone() {
        return defenderTerritory;
    }

}
