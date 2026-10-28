package it.uniurb.pmo.controller.game;

import it.uniurb.pmo.controller.game.dto.CombatCommandRequestDTO;
import it.uniurb.pmo.controller.game.dto.CurrentStateDTO;
import it.uniurb.pmo.controller.game.dto.DeployCommandRequestDTO;
import it.uniurb.pmo.controller.game.dto.MovementCommandRequestDTO;
import it.uniurb.pmo.framework.lobby.GameSessionRegistry;
import it.uniurb.pmo.framework.players.ITokenType;
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

    @PostMapping("/{roomId}/combat")
    public ResponseEntity<?> combat(@PathVariable String roomId, @RequestBody CombatCommandRequestDTO request) {
        ResponseEntity<?> response;
        Optional<IGameCoordinator> coordinator = this.gameSessions.getGameCoordinator(roomId);
        if (coordinator.isEmpty() || coordinator.get().currentState(roomId).isEmpty()) {
            response = ResponseEntity.notFound().build();
        } else if (!this.isValidCombatPayload(request)) {
            response = ResponseEntity.badRequest().body(Map.of("error", "Richiesta di combattimento non valida."));
        } else {
            response = this.submitCombat(roomId, coordinator.get(), request);
        }
        return response;
    }

    @PostMapping("/{roomId}/movement")
    public ResponseEntity<?> movement(@PathVariable String roomId, @RequestBody MovementCommandRequestDTO request) {
        ResponseEntity<?> response;
        Optional<IGameCoordinator> coordinator = this.gameSessions.getGameCoordinator(roomId);
        if (coordinator.isEmpty() || coordinator.get().currentState(roomId).isEmpty()) {
            response = ResponseEntity.notFound().build();
        } else if (!this.isValidMovementPayload(request)) {
            response = ResponseEntity.badRequest().body(Map.of("error", "Richiesta di spostamento non valida."));
        } else {
            response = this.submitMovement(roomId, coordinator.get(), request);
        }
        return response;
    }

    // Gestione della richiesta di Spostamento Strategico
    private boolean isValidMovementPayload(MovementCommandRequestDTO request) {
        boolean valid = request != null
                && request.type() != null
                && request.playerName() != null
                && !request.playerName().isBlank();
        if (valid && "MOVE".equals(request.type())) {
            valid = request.tokenType() != null && !request.tokenType().isBlank()
                    && request.source() != null && !request.source().isBlank()
                    && request.destination() != null && !request.destination().isBlank()
                    && request.tokenAmount() != null && request.tokenAmount() > 0;
        } else if (valid) {
            valid = "PASS".equals(request.type());
        }
        return valid;
    }

    private ResponseEntity<?> submitMovement(String roomId, IGameCoordinator coordinator,
                                             MovementCommandRequestDTO request) {
        ResponseEntity<?> response;
        try {
            if ("PASS".equals(request.type())) {
                coordinator.submitPass(request.playerName());
            } else {
                ITokenType tokenType = coordinator.resolveTokenType(request.tokenType());
                coordinator.submitMovement(request.playerName(), tokenType, request.source(),
                        request.destination(), request.tokenAmount());
            }
            Optional<CurrentStateDTO> updatedState = coordinator.currentState(roomId);
            response = updatedState.<ResponseEntity<?>>map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (IllegalArgumentException ex) {
            response = ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        } catch (IllegalStateException ex) {
            response = ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", ex.getMessage()));
        }
        return response;
    }

    // Gestione della richiesta di Combattimento (Attacco/Difesa)
    private boolean isValidCombatPayload(CombatCommandRequestDTO request) {
        boolean valid = request != null
                && request.type() != null
                && request.playerName() != null
                && !request.playerName().isBlank();
        if (valid && ("ATTACK".equals(request.type()) || "DEFEND".equals(request.type()))) {
            valid = request.tokenType() != null
                    && !request.tokenType().isBlank()
                    && request.tokenAmount() != null
                    && request.tokenAmount() > 0;
            if (valid && "ATTACK".equals(request.type())) {
                valid = request.attackerZone() != null && !request.attackerZone().isBlank()
                        && request.defenderZone() != null && !request.defenderZone().isBlank();
            }
        } else if (valid) {
            valid = "PASS".equals(request.type());
        }
        return valid;
    }

    private ResponseEntity<?> submitCombat(String roomId, IGameCoordinator coordinator,
                                           CombatCommandRequestDTO request) {
        ResponseEntity<?> response;
        try {
            if ("PASS".equals(request.type())) {
                coordinator.submitPass(request.playerName());
            } else {
                ITokenType tokenType = coordinator.resolveTokenType(request.tokenType());
                if ("ATTACK".equals(request.type())) {
                    coordinator.submitAttack(request.playerName(), request.attackerZone(),
                            request.defenderZone(), tokenType, request.tokenAmount());
                } else {
                    coordinator.submitDefend(request.playerName(), tokenType, request.tokenAmount());
                }
            }
            Optional<CurrentStateDTO> updatedState = coordinator.currentState(roomId);
            response = updatedState.<ResponseEntity<?>>map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (IllegalArgumentException ex) {
            response = ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        } catch (IllegalStateException ex) {
            response = ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", ex.getMessage()));
        }
        return response;
    }

    // Gestione della richiesta di Deployment
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
