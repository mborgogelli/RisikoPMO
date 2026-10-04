package it.uniurb.pmo.controller.game.dto;

import java.util.Map;

public record DeployCommandRequestDTO(String playerName, Map<String, Integer> deployment) {
}
