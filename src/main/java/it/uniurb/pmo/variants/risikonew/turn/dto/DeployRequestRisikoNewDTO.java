package it.uniurb.pmo.variants.risikonew.turn.dto;

import it.uniurb.pmo.framework.players.IPlayer;
import it.uniurb.pmo.framework.players.ITokenType;
import it.uniurb.pmo.framework.turn.dto.IDeployRequestDTO;
import it.uniurb.pmo.framework.turn.dto.IPlayerDataDTO;
import it.uniurb.pmo.framework.utils.EColors;
import it.uniurb.pmo.variants.risikonew.utils.ERisikoNewToken;

import java.util.List;
import java.util.Map;

public record DeployRequestRisikoNewDTO(IPlayer player, List<String> deployableZones, int tanksToDeploy) implements IDeployRequestDTO, IPlayerDataDTO {

    @Override
    public Map<ITokenType, Integer> tokenToDeploy() {
        return Map.of(ERisikoNewToken.TANK, tanksToDeploy);
    }

    @Override
    public String playerName() {
        return player.getName();
    }

    @Override
    public EColors playerColor() {
        return player.getColor();
    }
}
