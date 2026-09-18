package it.uniurb.pmo.framework.turn;

/**
 * Un evento di gioco è caratterizzato da un tipo di evento e uno stato di gioco.
 * Lo stato di gioco è il risultato dell'evento.
 * @param <E>
 */
public interface IGameEvent<E extends IGameState> {

    EGameEventType getEventType();

    E getState();

}
