package it.uniurb.pmo.framework.turn;

public interface IGameStateObserver {

    /**
     * Riceve un cambiamento prodotto dalla logica della partita.
     *
     * @param event aggiornamento pubblicato dal TurnManager
     */
    default void onGameEvent(GameEvent event) {
    }
}
