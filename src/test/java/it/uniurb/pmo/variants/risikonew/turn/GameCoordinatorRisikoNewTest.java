package it.uniurb.pmo.variants.risikonew.turn;

import it.uniurb.pmo.framework.turn.event.EGameEventType;
import it.uniurb.pmo.controller.game.dto.CurrentStateDTO;
import it.uniurb.pmo.framework.turn.event.GameEvent;
import it.uniurb.pmo.framework.turn.command.IGameCommandReceiver;
import it.uniurb.pmo.framework.turn.command.PassCommand;
import it.uniurb.pmo.framework.turn.phase.request.IDeployRequestDTO;
import it.uniurb.pmo.framework.turn.phase.request.IAttackRequestDTO;
import it.uniurb.pmo.framework.turn.phase.request.IDefendRequestDTO;
import it.uniurb.pmo.variants.risikonew.turn.command.AttackCommandRisikoNew;
import it.uniurb.pmo.variants.risikonew.turn.command.DefendCommandRisikoNew;
import it.uniurb.pmo.variants.risikonew.turn.command.DeployCommandRisikoNew;
import it.uniurb.pmo.variants.risikonew.utils.ERisikoNewToken;
import it.uniurb.pmo.framework.players.PlayerTurnStatus;
import it.uniurb.pmo.framework.turn.snapshot.PlayerSnapshot;
import it.uniurb.pmo.framework.utils.EColors;
import it.uniurb.pmo.framework.turn.phase.result.ICombatResultDTO;
import it.uniurb.pmo.variants.risikonew.turn.snapshot.RisikoGameSnapshotDTO;
import org.junit.jupiter.api.DisplayName;
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
    @DisplayName("Non restituisce uno stato prima del primo evento di gioco")
    void currentStateIsEmptyBeforeFirstGameEvent() {
        GameCoordinatorRisikoNew coordinator = new GameCoordinatorRisikoNew();

        assertTrue(coordinator.currentState("room-1").isEmpty());
    }

    @Test
    @DisplayName("Inoltra al ricevitore il comando di rinforzo")
    void submitDeployForPendingReinforceActionForwardsTankDeployment() {
        String territory = "Alaska";
        IDeployRequestDTO pendingAction = mock(IDeployRequestDTO.class);
        when(pendingAction.deployableZones()).thenReturn(List.of(territory));
        RisikoGameSnapshotDTO snapshot = new RisikoGameSnapshotDTO(
                2, Optional.of("REINFORCE"), Optional.of("Player1"),
                Map.of("Player1", new PlayerSnapshot("Player1", EColors.RED,
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
    @DisplayName("Inoltra il comando di attacco durante la fase di combattimento")
    void submitAttackForwardsAttackCommandDuringAttackRequest() {
        IAttackRequestDTO pendingAction = mock(IAttackRequestDTO.class);
        RisikoGameSnapshotDTO snapshot = new RisikoGameSnapshotDTO(
                2, Optional.of("RISIKONEW_ATTACK"), Optional.of("Player1"), Map.of(),
                Optional.of(pendingAction), Optional.empty(), Optional.empty());
        GameCoordinatorRisikoNew coordinator = new GameCoordinatorRisikoNew();
        IGameCommandReceiver receiver = mock(IGameCommandReceiver.class);
        when(receiver.isValidCommand(any())).thenReturn(true);
        coordinator.setCommandReceiver(receiver);
        coordinator.onGameEvent(new GameEvent(EGameEventType.CHOICE_REQUIRED, snapshot));

        coordinator.submitAttack("Player1", "Alaska", "Northwest Territory", ERisikoNewToken.TANK, 2);

        verify(receiver).handleCommand(new AttackCommandRisikoNew("Alaska", "Northwest Territory", 2));
    }

    @Test
    @DisplayName("Inoltra il comando di difesa al turno del difensore indicato")
    void submitDefendForwardsDefenseCommandForNamedDefender() {
        IDefendRequestDTO pendingAction = mock(IDefendRequestDTO.class);
        when(pendingAction.defenderName()).thenReturn("Player2");
        RisikoGameSnapshotDTO snapshot = new RisikoGameSnapshotDTO(
                2, Optional.of("RISIKONEW_ATTACK"), Optional.of("Player1"), Map.of(),
                Optional.of(pendingAction), Optional.empty(), Optional.empty());
        GameCoordinatorRisikoNew coordinator = new GameCoordinatorRisikoNew();
        IGameCommandReceiver receiver = mock(IGameCommandReceiver.class);
        when(receiver.isValidCommand(any())).thenReturn(true);
        coordinator.setCommandReceiver(receiver);
        coordinator.onGameEvent(new GameEvent(EGameEventType.CHOICE_REQUIRED, snapshot));

        coordinator.submitDefend("Player2", ERisikoNewToken.TANK, 2);

        verify(receiver).handleCommand(new DefendCommandRisikoNew(2));
    }

    @Test
    @DisplayName("Inoltra il comando per passare durante la fase di attacco")
    void submitPassForwardsPassCommandDuringAttackRequest() {
        IAttackRequestDTO pendingAction = mock(IAttackRequestDTO.class);
        RisikoGameSnapshotDTO snapshot = new RisikoGameSnapshotDTO(
                2, Optional.of("RISIKONEW_ATTACK"), Optional.of("Player1"), Map.of(),
                Optional.of(pendingAction), Optional.empty(), Optional.empty());
        GameCoordinatorRisikoNew coordinator = new GameCoordinatorRisikoNew();
        IGameCommandReceiver receiver = mock(IGameCommandReceiver.class);
        when(receiver.isValidCommand(any())).thenReturn(true);
        coordinator.setCommandReceiver(receiver);
        coordinator.onGameEvent(new GameEvent(EGameEventType.CHOICE_REQUIRED, snapshot));

        coordinator.submitPass("Player1");

        verify(receiver).handleCommand(new PassCommand());
    }

    @Test
    @DisplayName("Include i dettagli della difesa pendente nello stato corrente")
    void currentStateMapsPendingDefenseDetails() {
        IDefendRequestDTO pendingAction = mock(IDefendRequestDTO.class);
        when(pendingAction.defenderName()).thenReturn("Player2");
        when(pendingAction.defenderTokenType()).thenReturn(ERisikoNewToken.TANK);
        when(pendingAction.maxDefenderTokens()).thenReturn(3);
        RisikoGameSnapshotDTO snapshot = new RisikoGameSnapshotDTO(
                2, Optional.of("RISIKONEW_ATTACK"), Optional.of("Player1"), Map.of(),
                Optional.of(pendingAction), Optional.empty(), Optional.empty());
        GameCoordinatorRisikoNew coordinator = new GameCoordinatorRisikoNew();
        coordinator.onGameEvent(new GameEvent(EGameEventType.CHOICE_REQUIRED, snapshot));

        CurrentStateDTO state = coordinator.currentState("room-combat").orElseThrow();

        assertEquals("DEFEND", state.pendingAction().type());
        assertEquals("Player2", state.pendingAction().defenderName());
        assertEquals("Tank", state.pendingAction().defenderTokenType());
        assertEquals(3, state.pendingAction().maxDefenderTokens());
    }

    @Test
    @DisplayName("Mappa lo snapshot e il risultato del combattimento nello stato corrente")
    void currentStateMapsSnapshotAndRisikoNewCombatResult() {
        String roomId = "room-1";
        ICombatResultDTO combatResult = mock(ICombatResultDTO.class);
        RisikoGameSnapshotDTO snapshot = new RisikoGameSnapshotDTO(
                2,
                Optional.of("REINFORCE"),
                Optional.of("Player1"),
                Map.of("Player1", new PlayerSnapshot("Player1", EColors.RED,
                        PlayerTurnStatus.ACTIVE, Map.of(), Map.of())),
                Optional.empty(),
                Optional.empty(),
                Optional.of(combatResult));
        GameCoordinatorRisikoNew coordinator = new GameCoordinatorRisikoNew();
        coordinator.onGameEvent(new GameEvent(EGameEventType.PHASE_STARTED, snapshot));

        Optional<CurrentStateDTO> currentState = coordinator.currentState(roomId);

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
