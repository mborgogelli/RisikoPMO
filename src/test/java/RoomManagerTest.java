import it.uniurb.pmo.model.lobby.IRoomManager;
import it.uniurb.pmo.model.lobby.RoomManager;
import it.uniurb.pmo.model.utils.GameVersion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RoomManagerTest {

    IRoomManager roomManager = RoomManager.getInstance();

    @BeforeEach
    public void setUp() {
        String roomId = roomManager.createRoom("Maronno",4, GameVersion.RISIKONEW);

        roomManager.enterRoom(roomId,"Gianni");
        roomManager.enterRoom(roomId,"Pinotto");
        roomManager.enterRoom(roomId,"Santana");
    }

    @Test
    public void checkPeopleInTheRoom(){

        String roomId = roomManager.filterRoomsByGameVersion(GameVersion.RISIKONEW).getFirst();

        assertEquals(4, roomManager.getPlayersNumber(roomId));
    }
}
