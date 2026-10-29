package it.uniurb.pmo.framework.turn.snapshot;

import it.uniurb.pmo.framework.turn.phase.request.IGameRequest;

import java.util.Map;
import java.util.Optional;

/** Stato osservabile e completo della partita, indipendente dalla variante. */
public interface IGameSnapshot {

    int turnNumber();

    Optional<String> phaseId();

    Optional<String> currentPlayerName();

    /**
     * Restituisce lo stato dei giocatori.
     * @return  Stato dei giocatori.
     */
    Map<String, IPlayerSnapshot> players();

    /**
     * Restituisce l'azione pendente.
     * @return  Azione pendente.
     */
    Optional<IGameRequest> pendingAction();

    Optional<String> winnerName();
}
