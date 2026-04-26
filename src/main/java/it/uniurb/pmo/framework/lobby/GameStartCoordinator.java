package it.uniurb.pmo.framework.lobby;

import java.util.List;

import it.uniurb.pmo.framework.management.Director;
import org.springframework.stereotype.Component;

import it.uniurb.pmo.framework.players.IPlayer;
import it.uniurb.pmo.framework.utils.GameVersion;

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
