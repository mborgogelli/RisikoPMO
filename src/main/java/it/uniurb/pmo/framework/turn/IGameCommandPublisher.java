package it.uniurb.pmo.framework.turn;

public interface IGameCommandPublisher {

    void setCommandReceiver(IGameCommandReceiver receiver);

    void removeCommandReceiver(IGameCommandReceiver receiver);
}
