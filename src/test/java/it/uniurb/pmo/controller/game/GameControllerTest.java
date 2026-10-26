package it.uniurb.pmo.controller.game;

import it.uniurb.pmo.framework.lobby.GameSessionRegistry;
import it.uniurb.pmo.framework.turn.IGameCoordinator;
import it.uniurb.pmo.framework.turn.event.EGameEventType;
import it.uniurb.pmo.framework.turn.event.GameEvent;
import it.uniurb.pmo.framework.turn.snapshot.GameSnapshotDTO;
import it.uniurb.pmo.framework.turn.snapshot.IGameSnapshot;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GameControllerTest {

    @Test
    void getCurrentStateIncludesWinnerName() {
        String roomId = "room-1";
        String winnerName = "Player1";
        GameSessionRegistry gameSessions = new GameSessionRegistry();
        IGameCoordinator coordinator = mock(IGameCoordinator.class);
        IGameSnapshot snapshot = new GameSnapshotDTO(
                3,
                Optional.of("REINFORCE"),
                Optional.of(winnerName),
                Map.of(),
                Optional.empty(),
                Optional.of(winnerName));
        when(coordinator.lastGameEvent()).thenReturn(Optional.of(new GameEvent(EGameEventType.GAME_ENDED, snapshot)));
        gameSessions.register(roomId, coordinator);

        var response = new GameController(gameSessions).getCurrentState(roomId);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(winnerName, response.getBody().winnerName());
    }
}
