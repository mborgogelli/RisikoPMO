package it.uniurb.pmo.game;

import java.util.List;

import it.uniurb.pmo.model.management.Director;
import org.springframework.stereotype.Component;

import it.uniurb.pmo.model.lobby.RoomManager;
import it.uniurb.pmo.model.players.IPlayer;
import it.uniurb.pmo.model.utils.GameVersion;

@Component
public class GameStartCoordinator {

    private final RoomManager roomManager;

    public GameStartCoordinator() {
        this.roomManager = RoomManager.getInstance();
    }

    public GameStartResult startGame(String roomId) {
        if (!this.roomManager.isFull(roomId)) {
            throw new IllegalStateException("La stanza non è ancora piena");
        }

        GameVersion gameVersion = this.roomManager.getGameVersion(roomId);
        List<IPlayer> players = this.roomManager.getPlayers(roomId);

        new Director(gameVersion, players);
        this.roomManager.closeRoom(roomId);

        return new GameStartResult(roomId, players.size());
    }
}
