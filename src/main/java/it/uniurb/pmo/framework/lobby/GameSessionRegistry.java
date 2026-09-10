package it.uniurb.pmo.framework.lobby;

import it.uniurb.pmo.framework.turn.IGameCoordinator;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Mantiene il collegamento tra l'identificativo della stanza e la partita
 * effettivamente avviata. Il coordinator registrato qui deve essere la stessa
 * istanza consegnata dalla factory ai manager e alle fasi della partita.
 */
@Component
public class GameSessionRegistry {

    private final Map<String, IGameCoordinator> coordinators;

    public GameSessionRegistry() {
        this.coordinators = new ConcurrentHashMap<>();
    }

    /**
     * Registra la partita avviata per una stanza.
     *
     * @throws IllegalArgumentException se l'id non e' valido o una partita per
     *                                  la stanza e' gia' presente
     */
    public void register(String roomId, IGameCoordinator gameCoordinator) {
        this.validateRoomId(roomId);
        if (gameCoordinator == null) {
            throw new IllegalArgumentException("Game coordinator is required");
        }
        if (this.coordinators.putIfAbsent(roomId, gameCoordinator) != null) {
            throw new IllegalArgumentException("A game session for room " + roomId + " already exists.");
        }
    }

    /**
     * Restituisce il coordinator della partita, se la stanza e' stata avviata.
     */
    public Optional<IGameCoordinator> getGameCoordinator(String roomId) {
        this.validateRoomId(roomId);
        return Optional.ofNullable(this.coordinators.get(roomId));
    }

    /**
     * Elimina il riferimento alla partita terminata.
     */
    public void remove(String roomId) {
        this.validateRoomId(roomId);
        this.coordinators.remove(roomId);
    }

    private void validateRoomId(String roomId) {
        if (roomId == null || roomId.isBlank()) {
            throw new IllegalArgumentException("Room ID is required");
        }
    }
}
