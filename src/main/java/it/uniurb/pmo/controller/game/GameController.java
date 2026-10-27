package it.uniurb.pmo.controller.game;

import it.uniurb.pmo.controller.game.dto.CurrentStateDTO;
import it.uniurb.pmo.controller.game.dto.DeployCommandRequestDTO;
import it.uniurb.pmo.framework.lobby.GameSessionRegistry;
import it.uniurb.pmo.framework.turn.IGameCoordinator;
import it.uniurb.pmo.framework.turn.command.IDeployCommand;
import it.uniurb.pmo.framework.turn.event.interfaces.IGameEvent;
import it.uniurb.pmo.framework.turn.phase.request.IDeployRequestDTO;
import it.uniurb.pmo.variants.risikonew.turn.command.DeployCommandRisikoNew;
import it.uniurb.pmo.variants.risikonew.utils.ERisikoNewToken;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Objects;
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
        } else {
            Optional<IGameEvent> currentEvent = coordinator.get().lastGameEvent();
            if (currentEvent.isEmpty()) {
                response = ResponseEntity.notFound().build();
            } else if (!(currentEvent.get().getSnapshot().pendingAction().orElse(null) instanceof IDeployRequestDTO)) {
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

    private boolean isCurrentPlayer(IGameEvent event, DeployCommandRequestDTO request) {
        boolean matchesCurrentPlayer = false;
        if (request != null) {
            matchesCurrentPlayer = event.getSnapshot().currentPlayerName()
                    .map(name -> Objects.equals(name, request.playerName()))
                    .orElse(false);
        }
        return matchesCurrentPlayer;
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
            coordinator.submitCommand(this.toDeployCommand(request));
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

    private IDeployCommand<ERisikoNewToken> toDeployCommand(DeployCommandRequestDTO request) {
        return new DeployCommandRisikoNew(this.toRisikoNewToken(request.tokenType()), request.deployment());
    }

    private ERisikoNewToken toRisikoNewToken(String tokenName) {
        return java.util.Arrays.stream(ERisikoNewToken.values())
                .filter(token -> token.name().equals(tokenName) || token.getName().equals(tokenName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown token type: " + tokenName));
    }
}
