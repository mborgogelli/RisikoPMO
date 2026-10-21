package it.uniurb.pmo.variants.risikonew.turn.snapshot;

import it.uniurb.pmo.framework.turn.phase.request.IGameRequest;
import it.uniurb.pmo.framework.turn.phase.result.ICombatResultDTO;
import it.uniurb.pmo.framework.turn.snapshot.IPlayerStateDTO;

import java.util.Map;
import java.util.Optional;

/** Snapshot comune più l'ultimo risultato di combattimento della variante. */
public record RisikoGameSnapshotDTO(
        int turnNumber,
        Optional<String> phaseId,
        Optional<String> currentPlayerName,
        Map<String, IPlayerStateDTO> players,
        Optional<IGameRequest> pendingAction,
        Optional<String> winnerName,
        Optional<ICombatResultDTO> lastCombatResult
) implements IRisikoGameSnapshot {

    public RisikoGameSnapshotDTO {
        if (turnNumber < 0) {
            throw new IllegalArgumentException("Turn number cannot be negative.");
        }
        phaseId = phaseId == null ? Optional.empty() : phaseId;
        currentPlayerName = currentPlayerName == null ? Optional.empty() : currentPlayerName;
        players = Map.copyOf(players);
        pendingAction = pendingAction == null ? Optional.empty() : pendingAction;
        winnerName = winnerName == null ? Optional.empty() : winnerName;
        lastCombatResult = lastCombatResult == null ? Optional.empty() : lastCombatResult;
    }
}
