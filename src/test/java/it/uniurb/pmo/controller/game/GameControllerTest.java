package it.uniurb.pmo.controller.game;

import it.uniurb.pmo.framework.lobby.GameSessionRegistry;
import it.uniurb.pmo.controller.game.dto.CurrentStateDTO;
import it.uniurb.pmo.controller.game.dto.DeployCommandRequestDTO;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.doThrow;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.eq;

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
        when(coordinator.currentState(roomId)).thenReturn(Optional.of(new it.uniurb.pmo.controller.game.dto.CurrentStateDTO(
                roomId, EGameEventType.GAME_ENDED, "REINFORCE", winnerName, null,
                Map.of(), winnerName, null, null)));
        gameSessions.register(roomId, coordinator);

        var response = new GameController(gameSessions).getCurrentState(roomId);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(winnerName, response.getBody().winnerName());
    }
    @Test
    void deploySubmitsRequestAndReturnsUpdatedState() {
        String roomId = "room-deploy";
        GameSessionRegistry gameSessions = new GameSessionRegistry();
        IGameCoordinator coordinator = mock(IGameCoordinator.class);
        CurrentStateDTO state = emptyState(roomId);
        when(coordinator.currentState(roomId)).thenReturn(Optional.of(state));
        gameSessions.register(roomId, coordinator);
        DeployCommandRequestDTO request = new DeployCommandRequestDTO("Player1", "TANK", Map.of("Alaska", 2));

        var response = new GameController(gameSessions).deploy(roomId, request);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(state, response.getBody());
        verify(coordinator).submitDeploy("Player1", "TANK", Map.of("Alaska", 2));
    }

    @Test
    void deployReturnsNotFoundWhenRoomHasNoGame() {
        var response = new GameController(new GameSessionRegistry()).deploy("missing",
                new DeployCommandRequestDTO("Player1", "TANK", Map.of("Alaska", 2)));

        assertEquals(404, response.getStatusCode().value());
    }

    @Test
    void deployReturnsNotFoundWhenGameStateIsUnavailable() {
        String roomId = "room-no-state";
        GameSessionRegistry gameSessions = new GameSessionRegistry();
        IGameCoordinator coordinator = mock(IGameCoordinator.class);
        when(coordinator.currentState(roomId)).thenReturn(Optional.empty());
        gameSessions.register(roomId, coordinator);

        var response = new GameController(gameSessions).deploy(roomId,
                new DeployCommandRequestDTO("Player1", "TANK", Map.of("Alaska", 2)));

        assertEquals(404, response.getStatusCode().value());
    }

    @Test
    void deployReturnsBadRequestForInvalidPayload() {
        String roomId = "room-invalid";
        GameSessionRegistry gameSessions = new GameSessionRegistry();
        IGameCoordinator coordinator = mock(IGameCoordinator.class);
        when(coordinator.currentState(roomId)).thenReturn(Optional.of(emptyState(roomId)));
        gameSessions.register(roomId, coordinator);

        var response = new GameController(gameSessions).deploy(roomId,
                new DeployCommandRequestDTO("Player1", "TANK", Map.of("Alaska", 0)));

        assertEquals(400, response.getStatusCode().value());
    }

    @Test
    void deployReturnsBadRequestWhenCoordinatorRejectsCommand() {
        String roomId = "room-bad-command";
        GameSessionRegistry gameSessions = new GameSessionRegistry();
        IGameCoordinator coordinator = mock(IGameCoordinator.class);
        when(coordinator.currentState(roomId)).thenReturn(Optional.of(emptyState(roomId)));
        doThrow(new IllegalArgumentException("Unknown token type: UNKNOWN")).when(coordinator)
                .submitDeploy(eq("Player1"), eq("UNKNOWN"), anyMap());
        gameSessions.register(roomId, coordinator);

        var response = new GameController(gameSessions).deploy(roomId,
                new DeployCommandRequestDTO("Player1", "UNKNOWN", Map.of("Alaska", 2)));

        assertEquals(400, response.getStatusCode().value());
    }

    @Test
    void deployReturnsConflictWhenCoordinatorRejectsCurrentState() {
        String roomId = "room-conflict";
        GameSessionRegistry gameSessions = new GameSessionRegistry();
        IGameCoordinator coordinator = mock(IGameCoordinator.class);
        when(coordinator.currentState(roomId)).thenReturn(Optional.of(emptyState(roomId)));
        doThrow(new IllegalStateException("wrong turn")).when(coordinator)
                .submitDeploy(eq("Player1"), eq("TANK"), anyMap());
        gameSessions.register(roomId, coordinator);

        var response = new GameController(gameSessions).deploy(roomId,
                new DeployCommandRequestDTO("Player1", "TANK", Map.of("Alaska", 2)));

        assertEquals(409, response.getStatusCode().value());
    }

    private CurrentStateDTO emptyState(String roomId) {
        return new CurrentStateDTO(roomId, EGameEventType.PHASE_STARTED, "REINFORCE", "Player1",
                null, Map.of(), null, null, null);
    }

    @Test
    void getCurrentStateReturnsNotFoundWhenCoordinatorHasNoState() {
        String roomId = "room-2";
        GameSessionRegistry gameSessions = new GameSessionRegistry();
        IGameCoordinator coordinator = mock(IGameCoordinator.class);
        when(coordinator.currentState(roomId)).thenReturn(Optional.empty());
        gameSessions.register(roomId, coordinator);

        var response = new GameController(gameSessions).getCurrentState(roomId);

        assertEquals(404, response.getStatusCode().value());
    }

}

