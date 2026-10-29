package it.uniurb.pmo.controller.game;

import it.uniurb.pmo.controller.game.dto.CombatCommandRequestDTO;
import it.uniurb.pmo.controller.game.dto.CurrentStateDTO;
import it.uniurb.pmo.controller.game.dto.DeployCommandRequestDTO;
import it.uniurb.pmo.framework.lobby.GameSessionRegistry;
import it.uniurb.pmo.framework.players.ITokenType;
import it.uniurb.pmo.framework.turn.IGameCoordinator;
import it.uniurb.pmo.framework.turn.event.EGameEventType;
import it.uniurb.pmo.variants.risikonew.utils.ERisikoNewToken;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class GameControllerTest {

    @Test
    @DisplayName("Verifica che venga comunicato il vincitore")
    void getCurrentStateIncludesWinnerName() {
        String roomId = "room-1";
        String winnerName = "Player1";
        GameSessionRegistry gameSessions = new GameSessionRegistry();
        IGameCoordinator coordinator = mock(IGameCoordinator.class);
        when(coordinator.currentState(roomId)).thenReturn(Optional.of(new CurrentStateDTO(
                roomId, EGameEventType.GAME_ENDED, "REINFORCE", winnerName, null,
                Map.of(), winnerName, null, null)));
        gameSessions.register(roomId, coordinator);

        ResponseEntity<CurrentStateDTO> response = new GameController(gameSessions).getCurrentState(roomId);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(winnerName, response.getBody().winnerName());
    }

    @Test
    @DisplayName("Invia il comando di rinforzo e restituisce lo stato aggiornato")
    void deploySubmitsRequestAndReturnsUpdatedState() {
        String roomId = "room-deploy";
        GameSessionRegistry gameSessions = new GameSessionRegistry();
        IGameCoordinator coordinator = mock(IGameCoordinator.class);
        CurrentStateDTO state = emptyState(roomId);
        when(coordinator.currentState(roomId)).thenReturn(Optional.of(state));
        gameSessions.register(roomId, coordinator);
        DeployCommandRequestDTO request = new DeployCommandRequestDTO("Player1", "TANK", Map.of("Alaska", 2));

        ResponseEntity<?> response = new GameController(gameSessions).deploy(roomId, request);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(state, response.getBody());
        verify(coordinator).submitDeploy("Player1", "TANK", Map.of("Alaska", 2));
    }

    @Test
    @DisplayName("Invia un attacco e restituisce lo stato aggiornato")
    void combatAttackSubmitsCommandAndReturnsUpdatedState() {
        String roomId = "room-combat-attack";
        GameSessionRegistry gameSessions = new GameSessionRegistry();
        IGameCoordinator coordinator = mock(IGameCoordinator.class);
        CurrentStateDTO state = emptyState(roomId);
        ITokenType tokenType = ERisikoNewToken.TANK;
        when(coordinator.currentState(roomId)).thenReturn(Optional.of(state));
        when(coordinator.resolveTokenType("TANK")).thenReturn(tokenType);
        gameSessions.register(roomId, coordinator);
        CombatCommandRequestDTO request = new CombatCommandRequestDTO(
                "ATTACK", "Player1", "Alaska", "Northwest Territory", "TANK", 2);

        ResponseEntity<?> response = new GameController(gameSessions).combat(roomId, request);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(state, response.getBody());
        verify(coordinator).submitAttack("Player1", "Alaska", "Northwest Territory", tokenType, 2);
    }

    @Test
    @DisplayName("Invia una difesa e restituisce lo stato aggiornato")
    void combatDefenseSubmitsCommandAndReturnsUpdatedState() {
        String roomId = "room-combat-defend";
        GameSessionRegistry gameSessions = new GameSessionRegistry();
        IGameCoordinator coordinator = mock(IGameCoordinator.class);
        CurrentStateDTO state = emptyState(roomId);
        ITokenType tokenType = ERisikoNewToken.TANK;
        when(coordinator.currentState(roomId)).thenReturn(Optional.of(state));
        when(coordinator.resolveTokenType("TANK")).thenReturn(tokenType);
        gameSessions.register(roomId, coordinator);
        CombatCommandRequestDTO request = new CombatCommandRequestDTO(
                "DEFEND", "Player2", null, null, "TANK", 2);

        ResponseEntity<?> response = new GameController(gameSessions).combat(roomId, request);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(state, response.getBody());
        verify(coordinator).submitDefend("Player2", tokenType, 2);
    }

    @Test
    @DisplayName("Rifiuta un'azione di combattimento sconosciuta")
    void combatRejectsUnknownActionType() {
        String roomId = "room-combat-invalid";
        GameSessionRegistry gameSessions = new GameSessionRegistry();
        IGameCoordinator coordinator = mock(IGameCoordinator.class);
        when(coordinator.currentState(roomId)).thenReturn(Optional.of(emptyState(roomId)));
        gameSessions.register(roomId, coordinator);
        CombatCommandRequestDTO request = new CombatCommandRequestDTO(
                "MOVE", "Player1", null, null, "TANK", 2);

        ResponseEntity<?> response = new GameController(gameSessions).combat(roomId, request);

        assertEquals(400, response.getStatusCode().value());
    }

    @Test
    @DisplayName("Invia il passaggio del turno senza dati dei carri")
    void combatPassSubmitsPassWithoutTokenFields() {
        String roomId = "room-combat-pass";
        GameSessionRegistry gameSessions = new GameSessionRegistry();
        IGameCoordinator coordinator = mock(IGameCoordinator.class);
        CurrentStateDTO state = emptyState(roomId);
        when(coordinator.currentState(roomId)).thenReturn(Optional.of(state));
        gameSessions.register(roomId, coordinator);
        CombatCommandRequestDTO request = new CombatCommandRequestDTO(
                "PASS", "Player1", null, null, null, null);

        ResponseEntity<?> response = new GameController(gameSessions).combat(roomId, request);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(state, response.getBody());
        verify(coordinator).submitPass("Player1");
    }

    @Test
    @DisplayName("Restituisce non trovato se la stanza non ha una partita")
    void deployReturnsNotFoundWhenRoomHasNoGame() {
        ResponseEntity<?> response = new GameController(new GameSessionRegistry()).deploy("missing",
                new DeployCommandRequestDTO("Player1", "TANK", Map.of("Alaska", 2)));

        assertEquals(404, response.getStatusCode().value());
    }

    @Test
    @DisplayName("Restituisce non trovato se lo stato della partita non è disponibile")
    void deployReturnsNotFoundWhenGameStateIsUnavailable() {
        String roomId = "room-no-state";
        GameSessionRegistry gameSessions = new GameSessionRegistry();
        IGameCoordinator coordinator = mock(IGameCoordinator.class);
        when(coordinator.currentState(roomId)).thenReturn(Optional.empty());
        gameSessions.register(roomId, coordinator);

        ResponseEntity<?> response = new GameController(gameSessions).deploy(roomId,
                new DeployCommandRequestDTO("Player1", "TANK", Map.of("Alaska", 2)));

        assertEquals(404, response.getStatusCode().value());
    }

    @Test
    @DisplayName("Rifiuta una richiesta di rinforzo con dati non validi")
    void deployReturnsBadRequestForInvalidPayload() {
        String roomId = "room-invalid";
        GameSessionRegistry gameSessions = new GameSessionRegistry();
        IGameCoordinator coordinator = mock(IGameCoordinator.class);
        when(coordinator.currentState(roomId)).thenReturn(Optional.of(emptyState(roomId)));
        gameSessions.register(roomId, coordinator);

        ResponseEntity<?> response = new GameController(gameSessions).deploy(roomId,
                new DeployCommandRequestDTO("Player1", "TANK", Map.of("Alaska", 0)));

        assertEquals(400, response.getStatusCode().value());
    }

    @Test
    @DisplayName("Restituisce una richiesta non valida se il coordinatore rifiuta il rinforzo")
    void deployReturnsBadRequestWhenCoordinatorRejectsCommand() {
        String roomId = "room-bad-command";
        GameSessionRegistry gameSessions = new GameSessionRegistry();
        IGameCoordinator coordinator = mock(IGameCoordinator.class);
        when(coordinator.currentState(roomId)).thenReturn(Optional.of(emptyState(roomId)));
        doThrow(new IllegalArgumentException("Unknown token type: UNKNOWN")).when(coordinator)
                .submitDeploy(eq("Player1"), eq("UNKNOWN"), anyMap());
        gameSessions.register(roomId, coordinator);

        ResponseEntity<?> response = new GameController(gameSessions).deploy(roomId,
                new DeployCommandRequestDTO("Player1", "UNKNOWN", Map.of("Alaska", 2)));

        assertEquals(400, response.getStatusCode().value());
    }

    @Test
    @DisplayName("Restituisce un conflitto se il coordinatore rifiuta il comando di rinforzo")
    void deployReturnsConflictWhenCoordinatorRejectsCurrentState() {
        String roomId = "room-conflict";
        GameSessionRegistry gameSessions = new GameSessionRegistry();
        IGameCoordinator coordinator = mock(IGameCoordinator.class);
        when(coordinator.currentState(roomId)).thenReturn(Optional.of(emptyState(roomId)));
        doThrow(new IllegalStateException("wrong turn")).when(coordinator)
                .submitDeploy(eq("Player1"), eq("TANK"), anyMap());
        gameSessions.register(roomId, coordinator);

        ResponseEntity<?> response = new GameController(gameSessions).deploy(roomId,
                new DeployCommandRequestDTO("Player1", "TANK", Map.of("Alaska", 2)));

        assertEquals(409, response.getStatusCode().value());
    }

    private CurrentStateDTO emptyState(String roomId) {
        return new CurrentStateDTO(roomId, EGameEventType.PHASE_STARTED, "REINFORCE", "Player1",
                null, Map.of(), null, null, null);
    }

    @Test
    @DisplayName("Restituisce non trovato se il coordinatore non ha uno stato")
    void getCurrentStateReturnsNotFoundWhenCoordinatorHasNoState() {
        String roomId = "room-2";
        GameSessionRegistry gameSessions = new GameSessionRegistry();
        IGameCoordinator coordinator = mock(IGameCoordinator.class);
        when(coordinator.currentState(roomId)).thenReturn(Optional.empty());
        gameSessions.register(roomId, coordinator);

        ResponseEntity<CurrentStateDTO> response = new GameController(gameSessions).getCurrentState(roomId);

        assertEquals(404, response.getStatusCode().value());
    }

}

