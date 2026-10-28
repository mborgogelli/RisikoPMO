package it.uniurb.pmo.controller.game.dto;

import it.uniurb.pmo.framework.turn.event.EGameEventType;
import it.uniurb.pmo.framework.turn.phase.result.ICombatResultDTO;
import it.uniurb.pmo.framework.utils.EColors;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Stato della partita proiettato per il client web.
 */
public record CurrentStateDTO(
        String roomId,
        EGameEventType eventType,
        String phaseId,
        String currentPlayerName,
        EColors currentPlayerColor,
        Map<String, PlayerStateDTO> players,
        String winnerName,
        PendingActionDTO pendingAction,
        ICombatResultDTO combatResult) {

    public CurrentStateDTO {
        players = Map.copyOf(players);
    }

    public record PlayerStateDTO(
            EColors color,
            String turnStatus,
            Map<String, Map<String, Integer>> deployedTokensByTerritory,
            Map<String, Integer> availableTokens) {

        public PlayerStateDTO {
            deployedTokensByTerritory = deployedTokensByTerritory.entrySet().stream()
                    .collect(Collectors.toUnmodifiableMap(
                            Map.Entry::getKey,
                            entry -> Map.copyOf(entry.getValue())));
            availableTokens = Map.copyOf(availableTokens);
        }
    }

    // TODO Valutare un record separato per PendingActionDTO
    public record PendingActionDTO(
            String type,
            List<String> deployableTerritories,
            Map<String, Integer> tokensToDeploy,
            List<String> possibleTargets,
            Map<String, List<String>> possibleStartingZones,
            String defenderName,
            String defenderTokenType,
            Integer maxDefenderTokens,
            Map<String, List<String>> possibleMoves,
            Map<String, Map<String, Integer>> moveableTokens) {

        public PendingActionDTO(String type,
                                List<String> deployableTerritories,
                                Map<String, Integer> tokensToDeploy,
                                List<String> possibleTargets,
                                Map<String, List<String>> possibleStartingZones) {
            this(type, deployableTerritories, tokensToDeploy, possibleTargets,
                    possibleStartingZones, null, null, null, Map.of(), Map.of());
        }

        public PendingActionDTO(String type,
                                List<String> deployableTerritories,
                                Map<String, Integer> tokensToDeploy,
                                List<String> possibleTargets,
                                Map<String, List<String>> possibleStartingZones,
                                String defenderName,
                                String defenderTokenType,
                                Integer maxDefenderTokens) {
            this(type, deployableTerritories, tokensToDeploy, possibleTargets,
                    possibleStartingZones, defenderName, defenderTokenType,
                    maxDefenderTokens, Map.of(), Map.of());
        }

        public PendingActionDTO {
            deployableTerritories = List.copyOf(deployableTerritories);
            tokensToDeploy = Map.copyOf(tokensToDeploy);
            possibleTargets = List.copyOf(possibleTargets);
            possibleStartingZones = possibleStartingZones.entrySet().stream()
                    .collect(Collectors.toUnmodifiableMap(Map.Entry::getKey, entry -> List.copyOf(entry.getValue())));
            possibleMoves = possibleMoves.entrySet().stream()
                    .collect(Collectors.toUnmodifiableMap(Map.Entry::getKey, entry -> List.copyOf(entry.getValue())));
            moveableTokens = moveableTokens.entrySet().stream()
                    .collect(Collectors.toUnmodifiableMap(Map.Entry::getKey, entry -> Map.copyOf(entry.getValue())));
        }
    }
}
