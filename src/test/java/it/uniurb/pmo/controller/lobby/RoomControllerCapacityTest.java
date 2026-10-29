package it.uniurb.pmo.controller.lobby;

import it.uniurb.pmo.controller.lobby.dto.RoomResponseDTO;
import it.uniurb.pmo.framework.lobby.GameStartCoordinator;
import it.uniurb.pmo.framework.lobby.RoomManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RoomControllerCapacityTest {

    @Test
    @DisplayName( "Verifica che la creazione di una stanza con capacità superiore a 6 restituisca un errore 400")
    void rejectsCapacityAboveSixWithBadRequest() {
        RoomController controller = new RoomController(new GameStartCoordinator());
        ResponseEntity<RoomResponseDTO> response = controller.creaPartita(Map.of(
                "playerName", "Alice", "gameVersion", "Classica", "maxPlayers", "7"));

        if (response.getBody() != null) {
            RoomManager.getInstance().closeRoom(response.getBody().getRoomId());
        }
        assertEquals(400, response.getStatusCode().value());
    }
}
