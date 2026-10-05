package it.uniurb.pmo.controller.game.dto;

import java.util.Map;

public record DeployCommandRequestDTO(String playerName, String tokenType, Map<String, Integer> deployment) {
}
