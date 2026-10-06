package it.uniurb.pmo.framework.turn.event;

import it.uniurb.pmo.framework.turn.dto.IAttackRequestDTO;
import it.uniurb.pmo.framework.turn.event.interfaces.IGameEvent;

public record AttackRequestEvent(IAttackRequestDTO attackRequestDTO) implements IGameEvent<IAttackRequestDTO> {

    @Override
    public EGameEventType getEventType() {
        return EGameEventType.CHOICE_REQUIRED;
    }

    @Override
    public IAttackRequestDTO getState() {
        return attackRequestDTO;
    }
}
