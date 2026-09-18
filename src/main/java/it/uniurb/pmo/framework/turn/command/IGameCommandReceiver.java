package it.uniurb.pmo.framework.turn.command;

import it.uniurb.pmo.framework.turn.IPhaseResult;

/**
 * Punto di ingresso dei comandi diretti alla logica di una partita.
 *
 * @param <C> tipo di comando accettato dalla partita o dalla fase corrente
 */
public interface IGameCommandReceiver {

    /**
     * Riceve un comando gia' associato alla partita destinataria.
     * La sua validazione e la sua applicazione sono responsabilita'
     * dell'implementazione.
     *
     * @param command comando ricevuto dall'esterno
     */
    IPhaseResult handleCommand(IGameCommand command);

    boolean isValidCommand(IGameCommand command);
}
