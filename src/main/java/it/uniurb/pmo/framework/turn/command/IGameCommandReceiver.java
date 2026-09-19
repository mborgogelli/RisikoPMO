package it.uniurb.pmo.framework.turn.command;

import it.uniurb.pmo.framework.turn.event.interfaces.IGameEvent;
import it.uniurb.pmo.framework.turn.event.interfaces.IGameState;

/**
 * Punto di ingresso dei comandi diretti alla logica di una partita.
 *
 */
public interface IGameCommandReceiver {

    /**
     * Riceve un comando gia' associato alla partita destinataria.
     * La sua validazione e la sua applicazione sono responsabilita'
     * dell'implementazione.
     *
     * @param command comando ricevuto dall'esterno
     */
    IGameEvent<? extends IGameState> handleCommand(IGameCommand command);

    boolean isValidCommand(IGameCommand command);
}
