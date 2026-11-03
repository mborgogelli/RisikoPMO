package it.uniurb.pmo.framework.management;

import it.uniurb.pmo.framework.players.IPlayer;
import it.uniurb.pmo.framework.players.Player;
import it.uniurb.pmo.framework.utils.EColors;
import it.uniurb.pmo.framework.utils.EGameVersion;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DirectorTest {

    @Test
    @DisplayName("Dichiara il vincitore e completa la pulizia della partita")
    void declareWinnerCompletesCleanupForAllPlayers() {
        List<IPlayer> players = new ArrayList<>(List.of(
                new Player("Player1"), new Player("Player2"), new Player("Player3")));
        List<IPlayer> allPlayers = List.copyOf(players);
        players.get(0).setColor(EColors.RED);
        players.get(1).setColor(EColors.BLUE);
        players.get(2).setColor(EColors.GREEN);
        Director director = new Director(EGameVersion.RISIKONEW, players);

        assertDoesNotThrow(() -> director.declareWinner(players.getFirst()));

        assertTrue(players.isEmpty());
        allPlayers.forEach(player -> assertNull(player.getColor()));
    }
}
