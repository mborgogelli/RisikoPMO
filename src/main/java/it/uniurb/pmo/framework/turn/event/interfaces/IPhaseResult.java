package it.uniurb.pmo.framework.turn.event.interfaces;

import java.util.Optional;

/**
 * Esito dell'elaborazione di un comando da parte di una fase.
 */
public interface IPhaseResult {

    /**
     * Indica se la fase è stata completata.
     */
    boolean isCompleted();

    /**
     * Evento opzionale prodotto dalla fase, ad esempio una nuova scelta richiesta.
     */
    Optional<? extends IGameState> state();
}
