package it.uniurb.pmo.framework.lobby;

import java.util.List;

import it.uniurb.pmo.framework.management.Director;
import org.springframework.stereotype.Component;

import it.uniurb.pmo.framework.players.IPlayer;
import it.uniurb.pmo.framework.utils.EGameVersion;

@Component
public class GameStartCoordinator {

    private final RoomManager roomManager;

    public GameStartCoordinator() {
        this.roomManager = RoomManager.getInstance();
    }

    //TODO: Verifica se i giocatori sono tutti pronti

    public GameStartResult startGame(String roomId) {
        if (!this.roomManager.isFull(roomId)) {
            throw new IllegalStateException("La stanza non è ancora piena");
        }

        EGameVersion gameVersion = this.roomManager.getGameVersion(roomId);
        List<IPlayer> players = this.roomManager.getPlayers(roomId);

        new Director(gameVersion, players);
        this.roomManager.closeRoom(roomId);

        return new GameStartResult(roomId, players.size());
    }
}
