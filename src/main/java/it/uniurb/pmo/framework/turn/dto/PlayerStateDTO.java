package it.uniurb.pmo.framework.turn.dto;

import it.uniurb.pmo.framework.players.IPlayer;
import it.uniurb.pmo.framework.players.ITokenType;
import it.uniurb.pmo.framework.players.PlayerTurnStatus;
import it.uniurb.pmo.framework.utils.EColors;
import java.util.Map;

public record PlayerStateDTO(IPlayer player,
                             Map<String, Map<ITokenType, Integer>> playerZoneStatus,
                             Map<ITokenType, Integer> playerTokens) implements IPlayerStateDTO{

    @Override
    public String playerName() {
            return player.getName();
    }

    @Override
    public EColors playerColor() {
        return player.getColor();
    }

    @Override
    public PlayerTurnStatus playerTurnStatus() {
        return player.getTurnStatus();
    }

}
