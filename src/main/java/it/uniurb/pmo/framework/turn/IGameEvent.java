package it.uniurb.pmo.framework.turn;

public interface IGameEvent<E extends IGameState> {

    EGameEventType getEventType();

    E getState();

}
