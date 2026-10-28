package it.uniurb.pmo.framework.utils;

import it.uniurb.pmo.controller.game.dto.CurrentStateDTO;
import it.uniurb.pmo.framework.players.ITokenType;
import it.uniurb.pmo.framework.turn.event.interfaces.IGameEvent;
import it.uniurb.pmo.framework.turn.phase.request.*;
import it.uniurb.pmo.framework.turn.phase.result.ICombatResultDTO;
import it.uniurb.pmo.framework.turn.snapshot.IGameSnapshot;
import it.uniurb.pmo.framework.turn.snapshot.IPlayerStateDTO;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Classe Adapter che converte gli eventi e gli snapshot del framework nello stato esposto alla view.
 * I dati specifici di una variante vengono forniti dal coordinator.
 */
public class CurrentStateMapper {

    public CurrentStateDTO toCurrentState(String roomId,
                                          IGameEvent event,
                                          ICombatResultDTO combatResult) {
        IGameSnapshot snapshot = event.getSnapshot();
        Map<String, CurrentStateDTO.PlayerStateDTO> players = snapshot.players().entrySet().stream()
                .collect(Collectors.toUnmodifiableMap(Map.Entry::getKey,
                        entry -> this.toPlayerState(entry.getValue())));

        IPlayerStateDTO currentPlayer = snapshot.currentPlayerName()
                .map(snapshot.players()::get)
                .orElse(null);
        String currentPlayerName = currentPlayer == null ? null : currentPlayer.playerName();
        EColors currentPlayerColor = currentPlayer == null ? null : currentPlayer.playerColor();

        CurrentStateDTO.PendingActionDTO pendingAction = this.toPendingAction(snapshot, combatResult);
        return new CurrentStateDTO(roomId, event.getEventType(), snapshot.phaseId().orElse(null),
                currentPlayerName, currentPlayerColor, players, snapshot.winnerName().orElse(null),
                pendingAction, combatResult);
    }

    private CurrentStateDTO.PendingActionDTO toPendingAction(IGameSnapshot snapshot, ICombatResultDTO combatResult) {
        CurrentStateDTO.PendingActionDTO pendingAction = null;
        IGameRequest action = snapshot.pendingAction().orElse(null);
        if (action instanceof IDeployRequestDTO deployRequest) {
            pendingAction = new CurrentStateDTO.PendingActionDTO(
                    "DEPLOY",
                    deployRequest.deployableZones(),
                    this.toTokenMap(deployRequest.tokenToDeploy()),
                    List.of(),
                    Map.of());
        } else if (action instanceof IAttackRequestDTO attackRequest
                && !attackRequest.possibleTargets().isEmpty()) {
            Map<String, List<String>> startingZones = attackRequest.possibleTargets().stream()
                    .collect(Collectors.toUnmodifiableMap(
                            target -> target,
                            attackRequest::possibleStartingZones));
            pendingAction = new CurrentStateDTO.PendingActionDTO(
                    "ATTACK", List.of(), Map.of(), attackRequest.possibleTargets(), startingZones);
        } else if (action instanceof IDefendRequestDTO defendRequest) {
            pendingAction = new CurrentStateDTO.PendingActionDTO(
                    "DEFEND", List.of(), Map.of(), List.of(), Map.of(),
                    defendRequest.defenderName(), defendRequest.defenderTokenType().getName(),
                    defendRequest.maxDefenderTokens());
        } else if (action instanceof IMovementRequest movementRequest) {
            Map<String, Map<String, Integer>> moveableTokens = movementRequest.moveableTokens().entrySet().stream()
                    .collect(Collectors.toUnmodifiableMap(Map.Entry::getKey,
                            entry -> this.toTokenMap(entry.getValue())));
            pendingAction = new CurrentStateDTO.PendingActionDTO(
                    "MOVE", List.of(), Map.of(), List.of(), Map.of(), null, null, null,
                    movementRequest.destinations(), moveableTokens);
        } else if (action == null
                && snapshot.phaseId().filter("RISIKONEW_ATTACK"::equals).isPresent()
                && combatResult != null
                && !combatResult.possibleTargets().isEmpty()) {
            Map<String, List<String>> startingZones = combatResult.possibleTargets().stream()
                    .collect(Collectors.toUnmodifiableMap(
                            target -> target,
                            combatResult::possibleStartingZones));
            pendingAction = new CurrentStateDTO.PendingActionDTO(
                    "ATTACK", List.of(), Map.of(), combatResult.possibleTargets(), startingZones);
        }
        return pendingAction;
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
}
