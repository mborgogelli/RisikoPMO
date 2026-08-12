package it.uniurb.pmo.framework.turn;

import it.uniurb.pmo.framework.players.IPlayer;
import it.uniurb.pmo.framework.utils.EnumColors;

public interface IPlayerRequestDTO {

    default String getPlayerName(IPlayer player) {
        return player.getName();
    }

    default EnumColors getPlayerColor(IPlayer player) {
        return player.getColor();
    }
}
