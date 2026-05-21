import it.uniurb.pmo.framework.management.AbstractTurnManager;
import it.uniurb.pmo.variants.risikonew.GameFactoryRisikoNew;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class TurnManagerTest {

  private java.util.List<it.uniurb.pmo.framework.players.IPlayer> players;
	private it.uniurb.pmo.framework.management.interfaces.ITurnManager turnManager;

    @BeforeEach
    void setUp() {
		GameFactoryRisikoNew gf = new GameFactoryRisikoNew();
		this.players = java.util.List.of(
		    new it.uniurb.pmo.framework.players.Player("Player1", it.uniurb.pmo.framework.utils.EnumColors.RED),
		    new it.uniurb.pmo.framework.players.Player("Player2", it.uniurb.pmo.framework.utils.EnumColors.YELLOW),
		    new it.uniurb.pmo.framework.players.Player("Player3", it.uniurb.pmo.framework.utils.EnumColors.BLUE)
        );
		this.turnManager = gf.getManagers().stream()
				.filter(it.uniurb.pmo.framework.management.interfaces.ITurnManager.class::isInstance)
				.map(it.uniurb.pmo.framework.management.interfaces.ITurnManager.class::cast)
				.findFirst()
				.orElseThrow(() -> new IllegalStateException("Manager of type " + it.uniurb.pmo.framework.management.interfaces.ITurnManager.class.getName() + " not found"));
    }

	@Test
	void testInitializeGameUsesShuffleResult() {
		DeterministicTurnManager deterministicTurnManager = new DeterministicTurnManager();
		deterministicTurnManager.initializeGame(this.players);

		org.junit.jupiter.api.Assertions.assertEquals(java.util.List.of(this.players.get(2), this.players.get(1), this.players.get(0)), deterministicTurnManager.getPlayers());
		org.junit.jupiter.api.Assertions.assertNull(deterministicTurnManager.getCurrentPlayer());
		org.junit.jupiter.api.Assertions.assertEquals(1, deterministicTurnManager.getPlayedTurns());
	}

	@Test
	void testTurnSequenceAndWrapAround() {
		this.players.forEach(player -> player.setPlayerTurnStatus(it.uniurb.pmo.framework.players.PlayerTurnStatus.ACTIVE));
		this.turnManager.initializeGame(this.players);

		it.uniurb.pmo.framework.players.IPlayer firstPlayer = this.turnManager.getNextPlayer();
		this.turnManager.playTurn(firstPlayer);

		it.uniurb.pmo.framework.players.IPlayer secondPlayer = this.turnManager.getNextPlayer();
		this.turnManager.playTurn(secondPlayer);

		it.uniurb.pmo.framework.players.IPlayer thirdPlayer = this.turnManager.getNextPlayer();
		this.turnManager.playTurn(thirdPlayer);

		java.util.Set<it.uniurb.pmo.framework.players.IPlayer> uniquePlayers = java.util.Set.of(firstPlayer, secondPlayer, thirdPlayer);
		org.junit.jupiter.api.Assertions.assertEquals(3, uniquePlayers.size(), "La sequenza di un giro deve contenere tre giocatori distinti");
		org.junit.jupiter.api.Assertions.assertTrue(this.turnManager.getPlayers().containsAll(java.util.List.of(firstPlayer, secondPlayer, thirdPlayer)));

		it.uniurb.pmo.framework.players.IPlayer wrappedPlayer = this.turnManager.getNextPlayer();
		org.junit.jupiter.api.Assertions.assertEquals(firstPlayer, wrappedPlayer, "Dopo un giro completo si torna al primo giocatore della lista interna");
		org.junit.jupiter.api.Assertions.assertEquals(2, this.turnManager.getPlayedTurns(), "Il numero di turni deve incrementare al wrap-around");
	}

	@Test
	void testSkipEliminatedPlayers() {
		this.players.forEach(player -> player.setPlayerTurnStatus(it.uniurb.pmo.framework.players.PlayerTurnStatus.ACTIVE));
		this.turnManager.initializeGame(this.players);

		it.uniurb.pmo.framework.players.IPlayer firstPlayer = this.turnManager.getNextPlayer();
		this.turnManager.playTurn(firstPlayer);

		it.uniurb.pmo.framework.players.IPlayer eliminatedPlayer = this.turnManager.getNextPlayer();
		eliminatedPlayer.setPlayerTurnStatus(it.uniurb.pmo.framework.players.PlayerTurnStatus.ELIMINATED);

		it.uniurb.pmo.framework.players.IPlayer skippedCandidate = this.turnManager.getNextPlayer();
		org.junit.jupiter.api.Assertions.assertNotEquals(eliminatedPlayer, skippedCandidate, "Il giocatore eliminato deve essere saltato");
		org.junit.jupiter.api.Assertions.assertEquals(it.uniurb.pmo.framework.players.PlayerTurnStatus.ACTIVE, skippedCandidate.getPlayerTurnStatus());

		this.turnManager.playTurn(skippedCandidate);
		it.uniurb.pmo.framework.players.IPlayer wrappedPlayer = this.turnManager.getNextPlayer();
		org.junit.jupiter.api.Assertions.assertEquals(firstPlayer, wrappedPlayer, "Dopo aver saltato l'eliminato si deve tornare al primo giocatore attivo della rotazione");
	}

	@Test
	void testExceptionWhenNoActivePlayer() {
		this.turnManager.initializeGame(this.players);
		this.players.forEach(player -> player.setPlayerTurnStatus(it.uniurb.pmo.framework.players.PlayerTurnStatus.ELIMINATED));

		org.junit.jupiter.api.Assertions.assertThrows(IllegalStateException.class, () -> this.turnManager.getNextPlayer());
	}

	private static final class DeterministicTurnManager extends AbstractTurnManager {

		@Override
		public Boolean isReady() {
			return true;
		}

		@Override
		public void resetGame() {
			// no-op for the test double
		}

		@Override
		public void stopGame() {
			// no-op for the test double
		}

		@Override
		protected java.util.List<it.uniurb.pmo.framework.turn.IPhase> createPhases() {
			return java.util.List.of();
		}

		@Override
		protected java.util.List<it.uniurb.pmo.framework.players.IPlayer> shufflePlayers(java.util.List<it.uniurb.pmo.framework.players.IPlayer> players) {
			java.util.List<it.uniurb.pmo.framework.players.IPlayer> reversed = new ArrayList<>(players);
			java.util.Collections.reverse(reversed);
			return reversed;
		}
	}
}
