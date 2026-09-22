package it.uniurb.pmo.framework.turn.event;

import it.uniurb.pmo.framework.turn.dto.IPlayerStateDTO;
import it.uniurb.pmo.framework.turn.event.interfaces.IGameEvent;

public record PlayerStateEvent(EGameEventType event, IPlayerStateDTO playerState) implements IGameEvent<IPlayerStateDTO> {
    @Override
    public EGameEventType getEventType() {
        return event;
    }

    @Override
    public IPlayerStateDTO getState() {
        return playerState;
    }
}
