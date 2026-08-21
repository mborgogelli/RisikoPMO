package it.uniurb.pmo.framework.lobby;

import it.uniurb.pmo.framework.utils.EGameVersion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RoomManagerUnitTest {

    private final IRoomManager roomManager = RoomManager.getInstance();
    private String roomId;

    @BeforeEach
    public void setUp() {
        roomId = roomManager.createRoom("Maronno", 4, EGameVersion.RISIKONEW);

        roomManager.enterRoom(roomId, "Gianni");
        roomManager.enterRoom(roomId, "Pinotto");
        roomManager.enterRoom(roomId, "Santana");
    }

    @Test
    public void checkPeopleInTheRoom() {
        assertEquals(4, roomManager.getPlayersNumber(roomId));
        assertEquals(4, roomManager.getMaxPlayers(roomId));
        assertTrue(roomManager.isFull(roomId));
    }

    @Test
    public void ingressToRoom() {
        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> roomManager.enterRoom(roomId, "NuovoPlayer")
        );

        assertEquals("The Room is full.", ex.getMessage());
        assertEquals(4, roomManager.getPlayersNumber(roomId));

    }
}