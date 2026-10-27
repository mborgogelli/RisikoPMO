package it.uniurb.pmo.variants.risikonew.turn;

import it.uniurb.pmo.framework.turn.event.EGameEventType;
import it.uniurb.pmo.framework.turn.event.GameEvent;
import it.uniurb.pmo.framework.turn.command.IGameCommandReceiver;
import it.uniurb.pmo.framework.turn.phase.request.IDeployRequestDTO;
import it.uniurb.pmo.variants.risikonew.turn.command.DeployCommandRisikoNew;
import it.uniurb.pmo.variants.risikonew.utils.ERisikoNewToken;
import it.uniurb.pmo.framework.players.PlayerTurnStatus;
import it.uniurb.pmo.framework.turn.snapshot.PlayerStateDTO;
import it.uniurb.pmo.framework.utils.EColors;
import it.uniurb.pmo.framework.turn.phase.result.ICombatResultDTO;
import it.uniurb.pmo.variants.risikonew.turn.snapshot.RisikoGameSnapshotDTO;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Optional;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

class GameCoordinatorRisikoNewTest {

    @Test
    void currentStateIsEmptyBeforeFirstGameEvent() {
        GameCoordinatorRisikoNew coordinator = new GameCoordinatorRisikoNew();

        assertTrue(coordinator.currentState("room-1").isEmpty());
    }

    @Test
    void submitDeployForPendingReinforceActionForwardsTankDeployment() {
        String territory = "Alaska";
        IDeployRequestDTO pendingAction = mock(IDeployRequestDTO.class);
        when(pendingAction.deployableZones()).thenReturn(List.of(territory));
        RisikoGameSnapshotDTO snapshot = new RisikoGameSnapshotDTO(
                2, Optional.of("REINFORCE"), Optional.of("Player1"),
                Map.of("Player1", new PlayerStateDTO("Player1", EColors.RED,
                        PlayerTurnStatus.ACTIVE, Map.of(), Map.of())),
                Optional.of(pendingAction), Optional.empty(), Optional.empty());
        GameCoordinatorRisikoNew coordinator = new GameCoordinatorRisikoNew();
        IGameCommandReceiver receiver = mock(IGameCommandReceiver.class);
        when(receiver.isValidCommand(any())).thenReturn(true);
        coordinator.setCommandReceiver(receiver);
        coordinator.onGameEvent(new GameEvent(EGameEventType.PHASE_STARTED, snapshot));

        coordinator.submitDeploy("Player1", "TANK", Map.of(territory, 2));

        verify(receiver).handleCommand(new DeployCommandRisikoNew(ERisikoNewToken.TANK, Map.of(territory, 2)));
    }

    @Test
    void currentStateMapsSnapshotAndRisikoNewCombatResult() {
        String roomId = "room-1";
        ICombatResultDTO combatResult = mock(ICombatResultDTO.class);
        RisikoGameSnapshotDTO snapshot = new RisikoGameSnapshotDTO(
                2,
                Optional.of("REINFORCE"),
                Optional.of("Player1"),
                Map.of("Player1", new PlayerStateDTO("Player1", EColors.RED,
                        PlayerTurnStatus.ACTIVE, Map.of(), Map.of())),
                Optional.empty(),
                Optional.empty(),
                Optional.of(combatResult));
        GameCoordinatorRisikoNew coordinator = new GameCoordinatorRisikoNew();
        coordinator.onGameEvent(new GameEvent(EGameEventType.PHASE_STARTED, snapshot));

        var currentState = coordinator.currentState(roomId);

        assertTrue(currentState.isPresent());
        assertEquals(roomId, currentState.orElseThrow().roomId());
        assertEquals(EGameEventType.PHASE_STARTED, currentState.orElseThrow().eventType());
        assertEquals("REINFORCE", currentState.orElseThrow().phaseId());
        assertEquals("Player1", currentState.orElseThrow().currentPlayerName());
        assertSame(combatResult, currentState.orElseThrow().combatResult());
        assertEquals(EColors.RED, currentState.orElseThrow().players().get("Player1").color());
        assertFalse(currentState.orElseThrow().players().containsKey("unknown"));
    }
}
