package it.uniurb.pmo.framework.turn.dto;

import java.util.Map;

public record GameSnapshotDTO(Map<String, IPlayerStateDTO> players) implements IGameState {

    public GameSnapshotDTO {
        players = Map.copyOf(players);
    }
}
