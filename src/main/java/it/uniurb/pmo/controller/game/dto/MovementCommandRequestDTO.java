package it.uniurb.pmo.controller.game.dto;

public record MovementCommandRequestDTO(
        String type,
        String playerName,
        String tokenType,
        String source,
        String destination,
        Integer tokenAmount) {
}
