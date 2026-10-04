package it.uniurb.pmo.controller.game;

import it.uniurb.pmo.controller.game.dto.CurrentStateDTO;
import it.uniurb.pmo.controller.game.dto.DeployCommandRequestDTO;
import it.uniurb.pmo.framework.lobby.GameSessionRegistry;
import it.uniurb.pmo.framework.players.ITokenType;
import it.uniurb.pmo.framework.turn.IGameCoordinator;
import it.uniurb.pmo.framework.turn.IPhaseType;
import it.uniurb.pmo.framework.turn.command.DeployCommand;
import it.uniurb.pmo.framework.turn.dto.GameSnapshotDTO;
import it.uniurb.pmo.framework.turn.dto.IDeployRequestDTO;
import it.uniurb.pmo.framework.turn.dto.IGameState;
import it.uniurb.pmo.framework.turn.dto.IPlayerDataDTO;
import it.uniurb.pmo.framework.turn.dto.IPlayerStateDTO;
import it.uniurb.pmo.framework.turn.event.EGameEventType;
import it.uniurb.pmo.framework.turn.event.interfaces.IGameEvent;
import it.uniurb.pmo.framework.utils.EColors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/game")
public class GameController {

    private final GameSessionRegistry gameSessions;

    public GameController(GameSessionRegistry gameSessions) {
        this.gameSessions = gameSessions;
    }

    @GetMapping("/{roomId}/state")
    public ResponseEntity<CurrentStateDTO> getCurrentState(@PathVariable String roomId) {
        Optional<IGameCoordinator> coordinator = this.gameSessions.getGameCoordinator(roomId);
        ResponseEntity<CurrentStateDTO> response;
        if (coordinator.isPresent()) {
            Optional<IGameEvent<? extends IGameState>> event = coordinator.get().getLastGameEvent();
            if (event.isPresent()) {
                response = ResponseEntity.ok(this.toCurrentState(roomId, coordinator.get(), event.get()));
            } else {
                response = ResponseEntity.notFound().build();
            }
        } else {
            response = ResponseEntity.notFound().build();
        }
        return response;
    }

    @PostMapping("/{roomId}/deploy")
    public ResponseEntity<?> deploy(@PathVariable String roomId, @RequestBody DeployCommandRequestDTO request) {
        ResponseEntity<?> response;
        Optional<IGameCoordinator> coordinator = this.gameSessions.getGameCoordinator(roomId);
        if (coordinator.isEmpty()) {
            response = ResponseEntity.notFound().build();
        } else {
            Optional<IGameEvent<? extends IGameState>> currentEvent = coordinator.get().getLastGameEvent();
            if (currentEvent.isEmpty()) {
                response = ResponseEntity.notFound().build();
            } else if (!(currentEvent.get().getState() instanceof IDeployRequestDTO)) {
                response = ResponseEntity.badRequest().body(Map.of("error", "La fase corrente non richiede un piazzamento."));
            } else if (!this.isCurrentPlayer(currentEvent.get(), request)) {
                response = ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(Map.of("error", "Non è il turno del giocatore indicato."));
            } else if (!this.isValidDeploymentPayload(request)) {
                response = ResponseEntity.badRequest().body(Map.of("error", "Inserisci almeno un territorio e un numero di carri valido."));
            } else {
                response = this.submitDeployment(roomId, coordinator.get(), request);
            }
        }
        return response;
    }

    private CurrentStateDTO toCurrentState(String roomId,
                                           IGameCoordinator coordinator,
                                           IGameEvent<? extends IGameState> event) {
        GameSnapshotDTO snapshot = coordinator.getLatestGameSnapshot().orElse(new GameSnapshotDTO(Map.of()));
        Map<String, CurrentStateDTO.PlayerStateDTO> players = snapshot.players().entrySet().stream()
                .collect(Collectors.toUnmodifiableMap(Map.Entry::getKey, entry -> this.toPlayerState(entry.getValue())));

        String currentPlayerName = null;
        EColors currentPlayerColor = null;
        if (event.getState() instanceof IPlayerDataDTO playerData) {
            currentPlayerName = playerData.playerName();
            currentPlayerColor = playerData.playerColor();
        }

        String phaseId = event.getPhaseType().map(IPhaseType::code).orElse(null);
        CurrentStateDTO.PendingActionDTO pendingAction = null;
        if (event.getState() instanceof IDeployRequestDTO deployRequest) {
            pendingAction = new CurrentStateDTO.PendingActionDTO(
                    "DEPLOY",
                    deployRequest.deployableZones(),
                    this.toTokenMap(deployRequest.tokenToDeploy()));
        }

        EGameEventType eventType = event.getEventType();
        return new CurrentStateDTO(roomId, eventType, phaseId, currentPlayerName, currentPlayerColor, players, pendingAction);
    }

    private CurrentStateDTO.PlayerStateDTO toPlayerState(IPlayerStateDTO playerState) {
        Map<String, Map<String, Integer>> deployedTokensByTerritory = playerState.playerZoneStatus().entrySet().stream()
                .collect(Collectors.toUnmodifiableMap(
                        Map.Entry::getKey,
                        entry -> this.toTokenMap(entry.getValue())));
        return new CurrentStateDTO.PlayerStateDTO(
                playerState.playerColor(),
                playerState.playerTurnStatus().name(),
                deployedTokensByTerritory,
                this.toTokenMap(playerState.playerTokens()));
    }

    private Map<String, Integer> toTokenMap(Map<ITokenType, Integer> tokens) {
        return tokens.entrySet().stream()
                .collect(Collectors.toUnmodifiableMap(entry -> entry.getKey().getName(), Map.Entry::getValue));
    }

    private boolean isCurrentPlayer(IGameEvent<? extends IGameState> event, DeployCommandRequestDTO request) {
        boolean currentPlayer = false;
        if (event.getState() instanceof IPlayerDataDTO playerData) {
            currentPlayer = request != null && Objects.equals(playerData.playerName(), request.playerName());
        }
        return currentPlayer;
    }

    private boolean isValidDeploymentPayload(DeployCommandRequestDTO request) {
        boolean valid = request != null
                && request.playerName() != null
                && !request.playerName().isBlank()
                && request.deployment() != null
                && !request.deployment().isEmpty();
        if (valid) {
            valid = request.deployment().entrySet().stream()
                    .allMatch(entry -> entry.getKey() != null && entry.getValue() != null && entry.getValue() > 0);
        }
        return valid;
    }

    private ResponseEntity<?> submitDeployment(String roomId,
                                               IGameCoordinator coordinator,
                                               DeployCommandRequestDTO request) {
        ResponseEntity<?> response;
        try {
            coordinator.submitCommand(new DeployCommand(request.deployment()));
            Optional<IGameEvent<? extends IGameState>> updatedEvent = coordinator.getLastGameEvent();
            if (updatedEvent.isPresent()) {
                response = ResponseEntity.ok(this.toCurrentState(roomId, coordinator, updatedEvent.get()));
            } else {
                response = ResponseEntity.notFound().build();
            }
        } catch (IllegalArgumentException ex) {
            response = ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        } catch (IllegalStateException ex) {
            response = ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", ex.getMessage()));
        }
        return response;
    }
}
