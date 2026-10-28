package it.uniurb.pmo.framework.lobby;

import it.uniurb.pmo.framework.management.Director;
import it.uniurb.pmo.framework.players.IPlayer;
import it.uniurb.pmo.framework.utils.EGameVersion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GameStartCoordinator {

    private final RoomManager roomManager;
    private final GameSessionRegistry gameSessionRegistry;

    @Autowired
    public GameStartCoordinator(GameSessionRegistry gameSessionRegistry) {
        this.roomManager = RoomManager.getInstance();
        this.gameSessionRegistry = gameSessionRegistry;
    }

    public GameStartCoordinator() {
        this(new GameSessionRegistry());
    }

    public synchronized GameStartResult startGame(String roomId) {
        int playersCount = this.roomManager.getPlayersNumber(roomId);
        if (playersCount < 3 || playersCount > 6) {
            throw new IllegalStateException("Servono da 3 a 6 giocatori per iniziare");
        }
        if (!this.roomManager.areAllPlayersReady(roomId)) {
            throw new IllegalStateException("Tutti i giocatori devono essere pronti");
        }

        EGameVersion gameVersion = this.roomManager.getGameVersion(roomId);
        List<IPlayer> players = this.roomManager.getPlayers(roomId);

        Director director = new Director(gameVersion, players);
        this.gameSessionRegistry.register(roomId, director.getGameCoordinator());
        this.roomManager.closeRoom(roomId);

        return new GameStartResult(roomId, playersCount);
    }
}
