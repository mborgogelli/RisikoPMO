package it.uniurb.pmo.framework.turn.event.interfaces;

import it.uniurb.pmo.framework.turn.dto.IPhasePayload;

import java.util.Optional;

/**
 * Esito prodotto da una fase all'avvio o dopo l'elaborazione di un comando.
 */
public interface IPhaseResult<S extends IPhasePayload> {

    /**
     * Indica se la fase è stata completata.
     */
    boolean isCompleted();

    /**
     * Informazioni opzionali da presentare al client.
     */
    Optional<S> phasePayload();
}
