package it.uniurb.pmo.framework.turn.dto;

import it.uniurb.pmo.framework.turn.event.interfaces.IGameState;
import it.uniurb.pmo.framework.utils.EColors;

public interface IPlayerStateDTO extends IGameState {

    @Override
    public String playerName();

    @Override
    public EColors playerColor();
}
