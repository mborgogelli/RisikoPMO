package it.uniurb.pmo.framework.turn;

/**
 * Punto di ingresso dei comandi diretti alla logica di una partita.
 *
 * @param <T> tipo di comando accettato dalla partita o dalla fase corrente
 */
public interface IGameCommandReceiver<T> {

    /**
     * Riceve un comando gia' associato alla partita destinataria.
     * La sua validazione e la sua applicazione sono responsabilita'
     * dell'implementazione.
     *
     * @param command comando ricevuto dall'esterno
     */
    void receiveCommand(T command);
}
