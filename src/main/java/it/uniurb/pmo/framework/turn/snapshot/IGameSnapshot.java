package it.uniurb.pmo.framework.turn.snapshot;

import it.uniurb.pmo.framework.turn.phase.request.IGameRequest;

import java.util.Map;
import java.util.Optional;

/** Stato osservabile e completo della partita, indipendente dalla variante. */
public interface IGameSnapshot {

    int turnNumber();

    Optional<String> phaseId();

    Optional<String> currentPlayerName();

    Map<String, IPlayerStateDTO> players();

    Optional<IGameRequest> pendingAction();

    Optional<String> winnerName();
}
