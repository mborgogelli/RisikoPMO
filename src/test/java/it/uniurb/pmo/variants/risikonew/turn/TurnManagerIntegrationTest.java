package it.uniurb.pmo.variants.risikonew.turn;

import it.uniurb.pmo.framework.management.interfaces.ITurnManager;
import it.uniurb.pmo.framework.players.IPlayer;
import it.uniurb.pmo.framework.players.PlayerTurnStatus;
import it.uniurb.pmo.variants.risikonew.utils.RisikoNewTestSetup;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TurnManagerIntegrationTest extends RisikoNewTestSetup {

    private ITurnManager resolveTurnManager() {
        return resolveManager(ITurnManager.class);
    }


    @Test
    void testTurnSequenceAndWrapAround() {
        ITurnManager turnManager = resolveTurnManager();
        this.players.forEach(player -> player.setPlayerTurnStatus(PlayerTurnStatus.ACTIVE));
        turnManager.initializeGame(this.players);

        IPlayer firstPlayer = turnManager.getNextPlayer();
        turnManager.startTurn(firstPlayer);

        IPlayer secondPlayer = turnManager.getNextPlayer();
        turnManager.startTurn(secondPlayer);

        IPlayer thirdPlayer = turnManager.getNextPlayer();
        turnManager.startTurn(thirdPlayer);

        java.util.Set<IPlayer> uniquePlayers = java.util.Set.of(firstPlayer, secondPlayer, thirdPlayer);
        assertEquals(3, uniquePlayers.size(), "La sequenza di un giro deve contenere tre giocatori distinti");
        assertTrue(turnManager.getPlayers().containsAll(List.of(firstPlayer, secondPlayer, thirdPlayer)));

        IPlayer fourthPlayer = turnManager.getNextPlayer();
        turnManager.startTurn(fourthPlayer);

        IPlayer wrappedPlayer = turnManager.getNextPlayer();
        assertEquals(firstPlayer, wrappedPlayer, "Dopo un giro completo si torna al primo giocatore della lista interna");
        assertEquals(2, turnManager.getPlayedTurns(), "Il numero di turni deve incrementare al wrap-around");
    }

    @Test
    void testSkipEliminatedPlayers() {
        ITurnManager turnManager = resolveTurnManager();
        this.players.forEach(player -> player.setPlayerTurnStatus(PlayerTurnStatus.ACTIVE));
        turnManager.initializeGame(this.players);

        IPlayer firstPlayer = turnManager.getNextPlayer();
        turnManager.startTurn(firstPlayer);

        IPlayer eliminatedPlayer = turnManager.getNextPlayer();
        eliminatedPlayer.setPlayerTurnStatus(PlayerTurnStatus.ELIMINATED);

        IPlayer skippedCandidate = turnManager.getNextPlayer();
        assertNotEquals(eliminatedPlayer, skippedCandidate, "Il giocatore eliminato deve essere saltato");
        assertEquals(PlayerTurnStatus.ACTIVE, skippedCandidate.getPlayerTurnStatus());

        turnManager.startTurn(skippedCandidate);
        IPlayer nextPlayer = turnManager.getNextPlayer();
        assertNotEquals(eliminatedPlayer, nextPlayer, "Il giocatore eliminato non deve mai essere selezionato");
    }

    @Test
    void testExceptionWhenNoActivePlayer() {
        ITurnManager turnManager = resolveTurnManager();
        turnManager.initializeGame(this.players);
        this.players.forEach(player -> player.setPlayerTurnStatus(PlayerTurnStatus.ELIMINATED));

        assertThrows(IllegalStateException.class, () -> turnManager.getNextPlayer());
    }

}
