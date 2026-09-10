package it.uniurb.pmo.framework.turn;

public interface IGameStateObserver {

    void updatePhaseState();

    void updateTurnState();
}
