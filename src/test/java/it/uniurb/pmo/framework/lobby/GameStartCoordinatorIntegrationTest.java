package it.uniurb.pmo.framework.lobby;

import it.uniurb.pmo.framework.utils.EGameVersion;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameStartCoordinatorTest {

    private final RoomManager roomManager = RoomManager.getInstance();
    private String roomId;

    @AfterEach
    void tearDown() {
        if (this.roomId == null) {
            return;
        }

        try {
            this.roomManager.closeRoom(this.roomId);
        } catch (IllegalArgumentException ignored) {
            // Room già chiusa dal coordinatore.
        }
    }

    @Test
    void startGameClosesRoomAndReturnsResult() {
        this.roomId = this.roomManager.createRoom("Alice", 3, EGameVersion.RISIKONEW);
        this.roomManager.enterRoom(this.roomId, "Bob");
        this.roomManager.enterRoom(this.roomId, "Charlie");
        this.roomManager.setPlayerReady(this.roomId, "Alice", true);
        this.roomManager.setPlayerReady(this.roomId, "Bob", true);
        this.roomManager.setPlayerReady(this.roomId, "Charlie", true);

        GameStartResult result = new GameStartCoordinator().startGame(this.roomId);

        assertEquals(this.roomId, result.roomId());
        assertEquals(3, result.playersCount());
        assertThrows(IllegalArgumentException.class, () -> this.roomManager.getPlayersNumber(this.roomId));

        this.roomId = null;
    }

    @Test
    void startGameRegistersTheGameCoordinatorForTheRoom() {
        GameSessionRegistry gameSessions = new GameSessionRegistry();
        this.roomId = this.roomManager.createRoom("Alice", 3, EGameVersion.RISIKONEW);
        this.roomManager.enterRoom(this.roomId, "Bob");
        this.roomManager.enterRoom(this.roomId, "Charlie");
        this.roomManager.setPlayerReady(this.roomId, "Alice", true);
        this.roomManager.setPlayerReady(this.roomId, "Bob", true);
        this.roomManager.setPlayerReady(this.roomId, "Charlie", true);
        new GameStartCoordinator(gameSessions).startGame(this.roomId);

        assertTrue(gameSessions.getGameCoordinator(this.roomId).isPresent());
        gameSessions.remove(this.roomId);
        this.roomId = null;
    }

    @Test
    void startGameAllowsThreeReadyPlayersInSixSeatRoom() {
        this.roomId = this.roomManager.createRoom("Alice", 6, EGameVersion.RISIKONEW);
        this.roomManager.enterRoom(this.roomId, "Bob");
        this.roomManager.enterRoom(this.roomId, "Charlie");
        this.roomManager.setPlayerReady(this.roomId, "Alice", true);
        this.roomManager.setPlayerReady(this.roomId, "Bob", true);
        this.roomManager.setPlayerReady(this.roomId, "Charlie", true);

        GameStartResult result = new GameStartCoordinator().startGame(this.roomId);

        assertEquals(3, result.playersCount());
    }

    @Test
    void startGameRejectsFewerThanThreePlayers() {
        this.roomId = this.roomManager.createRoom("Alice", 6, EGameVersion.RISIKONEW);
        this.roomManager.enterRoom(this.roomId, "Bob");
        this.roomManager.setPlayerReady(this.roomId, "Alice", true);
        this.roomManager.setPlayerReady(this.roomId, "Bob", true);

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> new GameStartCoordinator().startGame(this.roomId));

        assertEquals("Servono da 3 a 6 giocatori per iniziare", exception.getMessage());
    }

    @Test
    void startGameRejectsWhenAPlayerIsNotReady() {
        this.roomId = this.roomManager.createRoom("Alice", 6, EGameVersion.RISIKONEW);
        this.roomManager.enterRoom(this.roomId, "Bob");
        this.roomManager.enterRoom(this.roomId, "Charlie");
        this.roomManager.setPlayerReady(this.roomId, "Alice", true);
        this.roomManager.setPlayerReady(this.roomId, "Bob", true);

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> new GameStartCoordinator().startGame(this.roomId));

        assertEquals("Tutti i giocatori devono essere pronti", exception.getMessage());
    }
}
