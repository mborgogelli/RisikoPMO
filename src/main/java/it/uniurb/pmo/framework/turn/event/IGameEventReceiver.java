package it.uniurb.pmo.framework.turn.event;

public interface IGameEventReceiver {

    /**
     * Riceve un cambiamento prodotto dalla logica della partita.
     *
     * @param event aggiornamento pubblicato dal TurnManager
     */
    default void onGameEvent(IGameEvent<? extends IGameState> event) {
    }
}
