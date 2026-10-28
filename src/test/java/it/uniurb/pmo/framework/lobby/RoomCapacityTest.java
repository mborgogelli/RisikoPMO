package it.uniurb.pmo.framework.lobby;

import it.uniurb.pmo.framework.utils.EGameVersion;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class RoomCapacityTest {

    @Test
    void rejectsRoomCapacityBelowThree() {
        assertThrows(IllegalArgumentException.class, () -> new Room(2, EGameVersion.RISIKONEW));
    }

    @Test
    void rejectsRoomCapacityAboveSix() {
        assertThrows(IllegalArgumentException.class, () -> new Room(7, EGameVersion.RISIKONEW));
    }
}
