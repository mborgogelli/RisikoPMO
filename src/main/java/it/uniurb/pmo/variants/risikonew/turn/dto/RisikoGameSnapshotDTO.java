package it.uniurb.pmo.variants.risikonew.turn.dto;

import it.uniurb.pmo.framework.turn.dto.ICombatResultDTO;
import it.uniurb.pmo.framework.turn.dto.IPlayerStateDTO;
import it.uniurb.pmo.framework.turn.dto.IGameActionDTO;

import java.util.Map;
import java.util.Optional;

/** Snapshot comune più l'ultimo risultato di combattimento della variante. */
public record RisikoGameSnapshotDTO(
        int turnNumber,
        Optional<String> phaseId,
        Optional<String> currentPlayerName,
        Map<String, IPlayerStateDTO> players,
        Optional<IGameActionDTO> pendingAction,
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
