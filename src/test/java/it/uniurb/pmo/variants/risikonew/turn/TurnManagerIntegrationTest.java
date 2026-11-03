package it.uniurb.pmo.variants.risikonew.turn;

import it.uniurb.pmo.controller.game.dto.CurrentStateDTO;
import it.uniurb.pmo.framework.management.interfaces.IDirector;
import it.uniurb.pmo.framework.management.interfaces.ITurnManager;
import it.uniurb.pmo.framework.players.IPlayer;
import it.uniurb.pmo.framework.players.PlayerTurnStatus;
import it.uniurb.pmo.framework.turn.event.EGameEventType;
import it.uniurb.pmo.variants.risikonew.utils.RisikoNewTestSetup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class TurnManagerIntegrationTest extends RisikoNewTestSetup {

    @Test
    @DisplayName("Aggiorna lo stato del territorio dopo lo schieramento iniziale")
    void initialDeploymentUpdatesTerritoryCountInCurrentState() {
        this.players.forEach(player -> player.setPlayerTurnStatus(PlayerTurnStatus.ACTIVE));
        this.turnManager.startGame();

        CurrentStateDTO before = this.gameCoordinator.currentState("room").orElseThrow();
        String player = before.currentPlayerName();
        String territory = before.pendingAction().deployableTerritories().getFirst();
        int countBefore = before.players().get(player).deployedTokensByTerritory().get(territory).get("Tank");

        this.gameCoordinator.submitDeploy(player, "Tank", Map.of(territory, 3));

        CurrentStateDTO after = this.gameCoordinator.currentState("room").orElseThrow();
        assertEquals(countBefore + 3,
                after.players().get(player).deployedTokensByTerritory().get(territory).get("Tank"));
    }

    @Test
    @DisplayName("Rispetta la sequenza dei turni e riparte dal primo giocatore")
    void testTurnSequenceAndWrapAround() {
        ITurnManager turnManager = super.getManager(ITurnManager.class);
        this.players.forEach(player -> player.setPlayerTurnStatus(PlayerTurnStatus.ACTIVE));
        turnManager.initializeGame(this.players);

        IPlayer firstPlayer = turnManager.getNextPlayer();
        turnManager.startTurn(firstPlayer);

        IPlayer secondPlayer = turnManager.getNextPlayer();
        turnManager.startTurn(secondPlayer);

        IPlayer thirdPlayer = turnManager.getNextPlayer();
        turnManager.startTurn(thirdPlayer);

        Set<IPlayer> uniquePlayers = Set.of(firstPlayer, secondPlayer, thirdPlayer);
        assertEquals(3, uniquePlayers.size(), "La sequenza di un giro deve contenere tre giocatori distinti");
        assertTrue(turnManager.getPlayers().containsAll(List.of(firstPlayer, secondPlayer, thirdPlayer)));

        IPlayer fourthPlayer = turnManager.getNextPlayer();
        turnManager.startTurn(fourthPlayer);

        IPlayer wrappedPlayer = turnManager.getNextPlayer();
        assertEquals(firstPlayer, wrappedPlayer, "Dopo un giro completo si torna al primo giocatore della lista interna");
        assertEquals(2, turnManager.getPlayedTurns(), "Il numero di turni deve incrementare al wrap-around");
    }

    @Test
    @DisplayName("Salta i giocatori eliminati durante la selezione del turno")
    void testSkipEliminatedPlayers() {
        ITurnManager turnManager = super.getManager(ITurnManager.class);
        this.players.forEach(player -> player.setPlayerTurnStatus(PlayerTurnStatus.ACTIVE));
        turnManager.initializeGame(this.players);

        IPlayer firstPlayer = turnManager.getNextPlayer();
        turnManager.startTurn(firstPlayer);

        IPlayer eliminatedPlayer = turnManager.getNextPlayer();
        eliminatedPlayer.setPlayerTurnStatus(PlayerTurnStatus.ELIMINATED);

        IPlayer skippedCandidate = turnManager.getNextPlayer();
        assertNotEquals(eliminatedPlayer, skippedCandidate, "Il giocatore eliminato deve essere saltato");
        assertEquals(PlayerTurnStatus.ACTIVE, skippedCandidate.getTurnStatus());

        turnManager.startTurn(skippedCandidate);
        IPlayer nextPlayer = turnManager.getNextPlayer();
        assertNotEquals(eliminatedPlayer, nextPlayer, "Il giocatore eliminato non deve mai essere selezionato");
    }

    @Test
    @DisplayName("Segnala un errore quando non ci sono giocatori attivi")
    void testExceptionWhenNoActivePlayer() {
        ITurnManager turnManager = super.getManager(ITurnManager.class);
        turnManager.initializeGame(this.players);
        this.players.forEach(player -> player.setPlayerTurnStatus(PlayerTurnStatus.ELIMINATED));

        assertThrows(IllegalStateException.class, turnManager::getNextPlayer);
    }

    @Test
    @DisplayName("Pubblica la vittoria prima di inoltrarla al director")
    void endTurnPublishesWinnerBeforeNotifyingDirector() {
        IPlayer winner = this.players.getFirst();
        this.players.forEach(player -> player.setPlayerTurnStatus(PlayerTurnStatus.ELIMINATED));
        winner.setPlayerTurnStatus(PlayerTurnStatus.ACTIVE);
        boolean[] directorNotified = {false};
        IDirector director = new IDirector() {
            @Override
            public void declareWinner(IPlayer player) {
                CurrentStateDTO stateAtNotification = gameCoordinator.currentState("room").orElseThrow();
                assertSame(winner, player);
                assertEquals(EGameEventType.GAME_ENDED, stateAtNotification.eventType());
                assertEquals(winner.getName(), stateAtNotification.winnerName());
                directorNotified[0] = true;
            }

            @Override
            public void exitGame(IPlayer player) {
            }

            @Override
            public void startGame() {
            }

            @Override
            public void stopGame() {
            }

            @Override
            public void resetGame() {
            }
        };
        this.mediator.setDirector(director);

        this.turnManager.endTurn(winner);

        CurrentStateDTO finalState = this.gameCoordinator.currentState("room").orElseThrow();
        assertEquals(EGameEventType.GAME_ENDED, finalState.eventType());
        assertEquals(winner.getName(), finalState.winnerName());
        assertTrue(directorNotified[0]);
    }

    @Test
    @DisplayName("Avanza il turno senza dichiarare una vittoria quando restano più giocatori")
    void endTurnWithoutWinnerAdvancesToNextActivePlayer() {
        this.players.forEach(player -> player.setPlayerTurnStatus(PlayerTurnStatus.ACTIVE));
        IPlayer currentPlayer = this.turnManager.getNextPlayer();
        this.turnManager.startTurn(currentPlayer);
        IPlayer expectedNextPlayer = this.turnManager.getNextPlayer();

        this.turnManager.endTurn(currentPlayer);

        assertSame(expectedNextPlayer, this.turnManager.getCurrentPlayer());
        CurrentStateDTO state = this.gameCoordinator.currentState("room").orElseThrow();
        assertNotEquals(EGameEventType.GAME_ENDED, state.eventType());
        assertEquals(expectedNextPlayer.getName(), state.currentPlayerName());
    }

}
