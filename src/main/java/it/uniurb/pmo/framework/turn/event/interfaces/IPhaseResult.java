package it.uniurb.pmo.framework.turn.event.interfaces;

import it.uniurb.pmo.framework.turn.dto.IGameState;

import java.util.Optional;

/**
 * Esito prodotto da una fase all'avvio o dopo l'elaborazione di un comando.
 */
public interface IPhaseResult<S extends IGameState> {

    /**
     * Indica se la fase è stata completata.
     */
    boolean isCompleted();

    /**
     * Stato opzionale da presentare al client; la fase non crea eventi di gioco.
     */
    Optional<S> state();
}
