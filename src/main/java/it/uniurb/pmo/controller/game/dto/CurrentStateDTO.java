package it.uniurb.pmo.controller.game.dto;

import it.uniurb.pmo.framework.turn.event.EGameEventType;
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
        PendingActionDTO pendingAction) {

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

    public record PendingActionDTO(
            String type,
            List<String> deployableTerritories,
            Map<String, Integer> tokensToDeploy) {

        public PendingActionDTO {
            deployableTerritories = List.copyOf(deployableTerritories);
            tokensToDeploy = Map.copyOf(tokensToDeploy);
        }
    }
}
