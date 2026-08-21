package it.uniurb.pmo.framework.lobby;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import it.uniurb.pmo.framework.utils.EGameVersion;

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

        GameStartResult result = new GameStartCoordinator().startGame(this.roomId);

        assertEquals(this.roomId, result.roomId());
        assertEquals(3, result.playersCount());
        assertThrows(IllegalArgumentException.class, () -> this.roomManager.getPlayersNumber(this.roomId));

        this.roomId = null;
    }

    @Test
    void startGameRejectsNotFullRoom() {
        this.roomId = this.roomManager.createRoom("Alice", 4, EGameVersion.RISIKONEW);

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> new GameStartCoordinator().startGame(this.roomId));

        assertEquals("La stanza non è ancora piena", exception.getMessage());
    }
}
