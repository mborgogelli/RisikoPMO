package it.uniurb.pmo.controller;

import java.util.List;
import java.util.Map;

import it.uniurb.pmo.controller.dto.RoomResponseDTO;
import it.uniurb.pmo.model.management.Director;
import it.uniurb.pmo.model.players.IPlayer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    /**
     * Endpoint per iniziare il gioco quando la stanza è piena
     */
    @PostMapping("/stanza/{roomId}/avvia-gioco")
    public ResponseEntity<?> avviaGioco(@PathVariable String roomId) {

        RoomManager roomManager = RoomManager.getInstance();

        // 1. Verifica che la stanza sia piena e che tutti siano pronti
        if (!roomManager.isFull(roomId)) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "La stanza non è ancora piena"));
        }

        // 2. Ottieni la versione del gioco dalla stanza
        GameVersion gameVersion = roomManager.getGameVersion(roomId);

        // 3. Crea il Director con la versione del gioco
        Director director = new Director(gameVersion);

        // 4. Estrai i giocatori dalla stanza e convertili in List<IPlayer>
        List<IPlayer> players = roomManager.getPlayers(roomId);

        // 5. Inizializza il gioco nel Director
        director.initializeGame(players);

        // 6. Avvia il gioco
        director.StartGame();

        // 7. (OPZIONALE) Rimuovi la stanza dal RoomManager
        // roomManager.removeRoom(roomId);  // Se hai questo metodo

        // 8. Restituisci la risposta al frontend
        return ResponseEntity.ok(Map.of(
                "message", "Gioco avviato con successo",
                "roomId", roomId,
                "playersCount", players.size()
        ));
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
        List<String> players = roomManager.getPlayers(roomId).stream()
                .map(IPlayer::getName)
                .toList();

        return RoomResponseDTO.builder()
            .roomId(roomId)
            .players(players)
            .currentPlayers(roomManager.getPlayersNumber(roomId))
            .gameVersion(roomManager.getGameVersion(roomId).toString())
            .maxPlayers(roomManager.getMaxPlayers(roomId))
            .isFull(roomManager.isFull(roomId))
            .build();
    }
}