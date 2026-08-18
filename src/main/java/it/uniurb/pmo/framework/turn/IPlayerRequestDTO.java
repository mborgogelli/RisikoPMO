package it.uniurb.pmo.framework.turn;

import it.uniurb.pmo.framework.players.IPlayer;
import it.uniurb.pmo.framework.utils.EnumColors;

public interface IPlayerRequestDTO {

    String playerName();

    EnumColors playerColor();
}
