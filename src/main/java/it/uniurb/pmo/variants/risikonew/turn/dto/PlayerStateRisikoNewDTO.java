package it.uniurb.pmo.variants.risikonew.turn.dto;

import it.uniurb.pmo.framework.players.IPlayer;
import it.uniurb.pmo.framework.players.ITokenType;
import it.uniurb.pmo.framework.players.PlayerTurnStatus;
import it.uniurb.pmo.framework.turn.dto.IPlayerStateDTO;
import it.uniurb.pmo.framework.utils.EColors;
import it.uniurb.pmo.variants.risikonew.utils.ERisikoNewToken;

import java.util.Map;
import java.util.stream.Collectors;

public record PlayerStateRisikoNewDTO(IPlayer player, Map<String, Integer> playerTerritories) implements IPlayerStateDTO {

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
        return player.getPlayerTurnStatus();
    }

    @Override
    public Map<String, Map<ITokenType, Integer>> playerZoneStatus() {
        return playerTerritories.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> Map.of(ERisikoNewToken.TANK, e.getValue())));
    }
}
