package it.uniurb.pmo.variants.risikonew.turn.snapshot;

import it.uniurb.pmo.framework.turn.phase.request.IGameRequest;
import it.uniurb.pmo.framework.turn.phase.result.ICombatResultDTO;
import it.uniurb.pmo.framework.turn.snapshot.IPlayerSnapshot;

import java.util.Map;
import java.util.Optional;

/** Snapshot comune con l'eventuale risultato di combattimento dell'evento corrente. */
public record RisikoGameSnapshotDTO(
        int turnNumber,
        Optional<String> phaseId,
        Optional<String> currentPlayerName,
        Map<String, IPlayerSnapshot> players,
        Optional<IGameRequest> pendingAction,
        Optional<String> winnerName,
        Optional<ICombatResultDTO> combatResult
) implements IRisikoGameSnapshot {

    public RisikoGameSnapshotDTO {
        if (turnNumber < 0) {
            throw new IllegalArgumentException("Turn number cannot be negative.");
        }
        players = Map.copyOf(players);
    }
}
