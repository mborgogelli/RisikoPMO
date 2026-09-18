package it.uniurb.pmo.framework.turn.command;

public interface IGameCommandPublisher {

    void setCommandReceiver(IGameCommandReceiver receiver);

    void removeCommandReceiver(IGameCommandReceiver receiver);

    void submitCommand(IGameCommand command);
}
