package it.uniurb.pmo.framework.turn.dto;

import it.uniurb.pmo.framework.turn.event.interfaces.IGameState;

import java.util.Map;

public record GameSnapshotDTO(Map<String, IPlayerStateDTO> players) implements IGameState {

    public GameSnapshotDTO {
        players = Map.copyOf(players);
    }
}
