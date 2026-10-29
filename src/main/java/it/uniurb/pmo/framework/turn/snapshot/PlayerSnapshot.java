package it.uniurb.pmo.framework.turn.snapshot;

import it.uniurb.pmo.framework.players.ITokenType;
import it.uniurb.pmo.framework.players.PlayerTurnStatus;
import it.uniurb.pmo.framework.utils.EColors;
import java.util.Map;

public record PlayerSnapshot(String playerName,
                             EColors playerColor,
                             PlayerTurnStatus playerTurnStatus,
                             Map<String, Map<ITokenType, Integer>> playerZoneStatus,
                             Map<ITokenType, Integer> playerTokens) implements IPlayerSnapshot{

    public PlayerSnapshot {
        playerZoneStatus = playerZoneStatus.entrySet().stream()
                .collect(java.util.stream.Collectors.toUnmodifiableMap(
                        Map.Entry::getKey,
                        entry -> Map.copyOf(entry.getValue())));
        playerTokens = Map.copyOf(playerTokens);
    }
}
