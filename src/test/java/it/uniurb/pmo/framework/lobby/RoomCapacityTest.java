package it.uniurb.pmo.framework.lobby;

import it.uniurb.pmo.framework.utils.EGameVersion;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class RoomCapacityTest {

    @Test
    @DisplayName("Rifiuta stanze con meno di tre posti")
    void rejectsRoomCapacityBelowThree() {
        assertThrows(IllegalArgumentException.class, () -> new Room(2, EGameVersion.RISIKONEW));
    }

    @Test
    @DisplayName("Rifiuta stanze con più di sei posti")
    void rejectsRoomCapacityAboveSix() {
        assertThrows(IllegalArgumentException.class, () -> new Room(7, EGameVersion.RISIKONEW));
    }
}
