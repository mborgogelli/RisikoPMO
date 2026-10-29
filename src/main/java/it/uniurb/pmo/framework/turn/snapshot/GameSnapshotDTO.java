package it.uniurb.pmo.framework.turn.snapshot;

import it.uniurb.pmo.framework.turn.phase.request.IGameRequest;

import java.util.Map;
import java.util.Optional;

/** Implementazione delle informazioni di snapshot comuni a tutte le varianti. */
public record GameSnapshotDTO(
        int turnNumber,
        Optional<String> phaseId,
        Optional<String> currentPlayerName,
        Map<String, IPlayerSnapshot> players,
        Optional<IGameRequest> pendingAction,
        Optional<String> winnerName
) implements IGameSnapshot {

    public GameSnapshotDTO {
        if (turnNumber < 0) {
            throw new IllegalArgumentException("Turn number cannot be negative.");
        }
        phaseId = phaseId == null ? Optional.empty() : phaseId;
        currentPlayerName = currentPlayerName == null ? Optional.empty() : currentPlayerName;
        players = Map.copyOf(players);
        pendingAction = pendingAction == null ? Optional.empty() : pendingAction;
        winnerName = winnerName == null ? Optional.empty() : winnerName;
    }
}
