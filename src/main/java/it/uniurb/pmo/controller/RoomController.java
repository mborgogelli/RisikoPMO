package it.uniurb.pmo.controller;

import java.util.Map;

import it.uniurb.pmo.controller.dto.RoomResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.uniurb.pmo.model.lobby.RoomManager;
import it.uniurb.pmo.model.utils.GameVersion;

@RestController
@RequestMapping("/api")
public class RoomController {

    @PostMapping("/crea-stanza")
    public ResponseEntity<RoomResponseDTO> creaPartita(@RequestBody Map<String, String> payload) {

        // Recupera informazioni dal JSON inviato dal frontend
        String nomeGiocatore = payload.get("playerName");
        GameVersion gameVersion = this.getGameVersionFromString(payload.get("gameVersion"));
        int maxPlayer = Integer.parseInt(payload.get("maxPlayers"));
        
        // Valida i dati recuperati
        if (nomeGiocatore == null ||
            nomeGiocatore.isEmpty()) {
            //TO DO impostare min player in base alla versione
            return ResponseEntity.badRequest().build();
        }

        // Crea la Stanza e aggiungi il giocatore
        RoomManager roomManager = RoomManager.getInstance();
        String roomId = roomManager.createRoom(nomeGiocatore,
        								   maxPlayer,
        								   gameVersion);
        
        // Crea il DTO con le informazioni da esporre al frontend
        RoomResponseDTO response = this.createRoomResponse(roomId, nomeGiocatore);

        // Restituisce il DTO al frontend
        return ResponseEntity.ok(response);
    }
    
	private GameVersion getGameVersionFromString(String version) {
		GameVersion gameVersion;
		switch (version.toLowerCase()) {
			case "classica" -> gameVersion = GameVersion.RISIKONEW;
			case "antartide" -> gameVersion = GameVersion.RISIKOANTARTIDE;
			case "oceano" -> gameVersion = GameVersion.RISIKOOCEANO;
			default -> throw new IllegalArgumentException("Invalid game version: " + version);
		}
		return gameVersion;
    }

    /**
     * Crea un DTO con le informazioni della stanza da esporre al frontend
     */
    private RoomResponseDTO createRoomResponse(String roomId, String playerName) {
        RoomManager roomManager = RoomManager.getInstance();

        return RoomResponseDTO.builder()
            .roomId(roomId)
            .players(roomManager.getPlayers(roomId))
            .currentPlayers(roomManager.getPlayersNumber(roomId))
            .gameVersion(roomManager.getGameVersion(roomId).toString())
            .maxPlayers(roomManager.getMaxPlayers(roomId))
            .isFull(roomManager.isFull(roomId))
            .build();
    }
}