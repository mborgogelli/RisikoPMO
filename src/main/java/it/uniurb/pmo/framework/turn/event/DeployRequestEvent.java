package it.uniurb.pmo.framework.turn.event;

import it.uniurb.pmo.framework.turn.dto.IDeployRequestDTO;
import it.uniurb.pmo.framework.turn.event.interfaces.IGameEvent;

public record DeployRequestEvent(IDeployRequestDTO state) implements IGameEvent<IDeployRequestDTO> {


    @Override
    public EGameEventType getEventType() {
        return EGameEventType.CHOICE_REQUIRED;
    }

    @Override
    public IDeployRequestDTO getState() {
        return state;
    }
}
