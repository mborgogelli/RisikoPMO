package it.uniurb.pmo.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.uniurb.pmo.game.RisikoPmoApplication;
import it.uniurb.pmo.model.lobby.Room;
import it.uniurb.pmo.model.lobby.RoomManager;
import it.uniurb.pmo.model.utils.GameVersion;

@RestController
@RequestMapping("/api")
public class RoomController {

    @PostMapping("/crea-stanza")
    public ResponseEntity<Room> creaPartita(@RequestBody Map<String, String> payload) {
        
        // 1. Recupera il nome dal JSON inviato dal frontend
        String nomeGiocatore = payload.get("playerName");
        GameVersion gameVersion = this.getGameVersionFromString(payload.get("gameVersion"));
        int maxPlayer = Integer.parseInt(payload.get("maxPlayers"));
        
        
        if (nomeGiocatore == null || nomeGiocatore.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        // 2. Crea la Stanza e aggiungi il giocatore
        RoomManager roomManager = RoomManager.getInstance();
        Room room = roomManager.createRoom(nomeGiocatore,
        								   maxPlayer,
        								   gameVersion);
        
        // 3. Restituisci l'oggetto Room completo al frontend
        return ResponseEntity.ok(room);
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
}