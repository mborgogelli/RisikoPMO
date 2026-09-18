package it.uniurb.pmo.framework.turn.event;

/**
 * Interfaccia per gli aggiornamenti sullo stato della partita.
 */
public interface IGameEventPublisher {

    /**
     * Registra un osservatore degli aggiornamenti della partita.
     *
     * @param observer osservatore da registrare
     */
    void addObserver(IGameEventReceiver observer);

    /**
     * Rimuove un osservatore precedentemente registrato.
     *
     * @param observer osservatore da rimuovere
     */
    void removeObserver(IGameEventReceiver observer);
}
