package it.uniurb.pmo.controller.game.dto;

public record CombatCommandRequestDTO(
        String type,
        String playerName,
        String attackerZone,
        String defenderZone,
        String tokenType,
        Integer tokenAmount) {
}
