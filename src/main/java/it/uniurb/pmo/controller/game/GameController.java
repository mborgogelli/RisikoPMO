package it.uniurb.pmo.controller.game;

import it.uniurb.pmo.controller.game.dto.CurrentStateDTO;
import it.uniurb.pmo.controller.game.dto.DeployCommandRequestDTO;
import it.uniurb.pmo.framework.lobby.GameSessionRegistry;
import it.uniurb.pmo.framework.turn.IGameCoordinator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

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
            Optional<CurrentStateDTO> currentState = coordinator.get().currentState(roomId);
            if (currentState.isPresent()) {
                response = ResponseEntity.ok(currentState.get());
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
        } else if (coordinator.get().currentState(roomId).isEmpty()) {
            response = ResponseEntity.notFound().build();
        } else if (!this.isValidDeploymentPayload(request)) {
            response = ResponseEntity.badRequest().body(Map.of("error", "Inserisci almeno un territorio e un numero di carri valido."));
        } else {
            response = this.submitDeployment(roomId, coordinator.get(), request);
        }
        return response;
    }

    private boolean isValidDeploymentPayload(DeployCommandRequestDTO request) {
        boolean valid = request != null
                && request.playerName() != null
                && !request.playerName().isBlank()
                && request.tokenType() != null
                && !request.tokenType().isBlank()
                && request.deployment() != null
                && !request.deployment().isEmpty();
        if (valid) {
            valid = request.deployment().entrySet().stream()
                    .allMatch(entry -> entry.getKey() != null
                            && !entry.getKey().isBlank()
                            && entry.getValue() != null
                            && entry.getValue() > 0);
        }
        return valid;
    }

    private ResponseEntity<?> submitDeployment(String roomId,
                                               IGameCoordinator coordinator,
                                               DeployCommandRequestDTO request) {
        ResponseEntity<?> response;
        try {
            coordinator.submitDeploy(request.playerName(), request.tokenType(), request.deployment());
            Optional<CurrentStateDTO> updatedState = coordinator.currentState(roomId);
            if (updatedState.isPresent()) {
                response = ResponseEntity.ok(updatedState.get());
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
