package it.uniurb.pmo.variants.risikonew.turn.dto;

import it.uniurb.pmo.framework.players.IPlayer;
import it.uniurb.pmo.framework.turn.dto.IAttackRequestDTO;
import it.uniurb.pmo.framework.turn.dto.IPlayerDataDTO;
import it.uniurb.pmo.framework.utils.EColors;

import java.util.List;
import java.util.Map;

public record AttackRequestRisikoNewDTO(IPlayer player, Map<String, List<String>> possiblePlayerTargets) implements IAttackRequestDTO, IPlayerDataDTO {

    @Override
    public List<String> possibleTargets() {
        return possiblePlayerTargets.keySet().stream().sorted().toList();
    }

    @Override
    public List<String> possibleStartingZones(String targetZone) {
        return possiblePlayerTargets.get(targetZone).stream().toList();
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
