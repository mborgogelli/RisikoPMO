package it.uniurb.pmo.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import it.uniurb.pmo.controller.dto.RoomResponseDTO;
import it.uniurb.pmo.framework.lobby.GameStartCoordinator;
import it.uniurb.pmo.framework.lobby.GameStartResult;
import it.uniurb.pmo.framework.lobby.RoomManager;
import it.uniurb.pmo.framework.players.IPlayer;
import it.uniurb.pmo.framework.utils.EnumColors;
import it.uniurb.pmo.framework.utils.GameVersion;

/**
 * Controller per la gestione delle stanze di gioco. Espone endpoint REST per
 * creare stanze, entrare in stanze esistenti e avviare il gioco. Utilizza
 * RoomManager per gestire la logica delle stanze e Director per inizializzare e
 * avviare il gioco.
 */
@RestController 
@RequestMapping("/api")
public class RoomController {
  private final GameStartCoordinator gameStartCoordinator;

  public RoomController(GameStartCoordinator gameStartCoordinator) {
    this.gameStartCoordinator = gameStartCoordinator;
  }

	// Endpoint per creare una nuova stanza di gioco
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
        RoomResponseDTO response = this.createRoomResponse(roomId);

        // Restituisce il DTO al frontend
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint per entrare in una stanza esistente
     */
    @PostMapping("/stanza/{roomId}/entra")
    public ResponseEntity<?> entraStanza(@PathVariable String roomId, @RequestBody Map<String, String> payload) {
        String nomeGiocatore = payload.get("playerName");
        if (nomeGiocatore == null || nomeGiocatore.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Nome giocatore mancante"));
        }

        RoomManager roomManager = RoomManager.getInstance();
        try {
            roomManager.enterRoom(roomId, nomeGiocatore);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        }

        return ResponseEntity.ok(this.createRoomResponse(roomId));
    }

    /**
     * Endpoint per impostare lo stato di pronto di un giocatore
     */
    @PostMapping("/stanza/{roomId}/pronto")
    public ResponseEntity<?> setPronto(@PathVariable String roomId, @RequestBody Map<String, Object> payload) {
        String nomeGiocatore = payload.get("playerName") != null ? payload.get("playerName").toString() : null;
        Boolean isReady = payload.get("ready") instanceof Boolean ? (Boolean) payload.get("ready") : null;

        if (nomeGiocatore == null || nomeGiocatore.isEmpty() || isReady == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Dati pronti mancanti"));
        }

        RoomManager roomManager = RoomManager.getInstance();
        try {
            roomManager.setPlayerReady(roomId, nomeGiocatore, isReady);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        }

        return ResponseEntity.ok(this.createRoomResponse(roomId));
    }

    /**
     * Endpoint per leggere lo stato corrente di una stanza.
     * Utile al frontend per aggiornare in polling la lobby di tutti i player.
     */
    @GetMapping("/stanza/{roomId}")
    public ResponseEntity<?> getStanza(@PathVariable String roomId) {
        try {
            return ResponseEntity.ok(this.createRoomResponse(roomId));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Endpoint per ottenere la lista delle stanze attive
     */
    @GetMapping("/stanze")
    public ResponseEntity<List<RoomResponseDTO>> getStanze() {
        RoomManager roomManager = RoomManager.getInstance();
        List<String> roomIds = roomManager.getActiveRooms();

        List<RoomResponseDTO> rooms = roomIds.stream().map(this::createRoomResponse).collect(Collectors.toList());

        return ResponseEntity.ok(rooms);
    }

    /**
     * Endpoint per iniziare il gioco quando la stanza è piena
     */
    @PostMapping("/stanza/{roomId}/avvia-gioco")
    public ResponseEntity<?> avviaGioco(@PathVariable String roomId) {

        GameStartResult result;

        try {
            result = this.gameStartCoordinator.startGame(roomId);
        } catch (IllegalArgumentException | IllegalStateException ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        }

        return ResponseEntity.ok(Map.of(
                "message", "Gioco avviato con successo",
                "roomId", result.roomId(),
                "playersCount", result.playersCount()
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
    private RoomResponseDTO createRoomResponse(String roomId) {
        RoomManager roomManager = RoomManager.getInstance();
        Map<String, EnumColors> players = this.buildPlayersMap(roomManager.getPlayers(roomId));
        Map<String, Boolean> readyStates = this.buildReadyMap(roomManager.getPlayers(roomId));

        return RoomResponseDTO.builder()
            .roomId(roomId)
            .players(players)
            .readyStates(readyStates)
            .currentPlayers(roomManager.getPlayersNumber(roomId))
            .gameVersion(roomManager.getGameVersion(roomId).toString())
            .maxPlayers(roomManager.getMaxPlayers(roomId))
            .isFull(roomManager.isFull(roomId))
            .build();
    }

    private Map<String, EnumColors> buildPlayersMap(List<IPlayer> players) {
        return players.stream().collect(Collectors.toMap(IPlayer::getName, IPlayer::getColor));
    }

    private Map<String, Boolean> buildReadyMap(List<IPlayer> players) {
        return players.stream().collect(Collectors.toMap(IPlayer::getName, IPlayer::isReady));
    }
}

