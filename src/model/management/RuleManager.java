
package model.management;

import java.util.List;

import model.management.interfaces.IMapManager;
import model.management.interfaces.IRuleManager;
import model.players.IPlayer;

public abstract class RuleManager implements IRuleManager {

    private IMapManager mapManager;
    private TokenManager tokenManager;
    private CardManager cardManager;
    private PhaseManager phaseManager;
    
    @Override
	public void initializeGame(List<IPlayer> players) {
        mapManager.initializeGame(players);
        tokenManager.initializeGame(players);
        cardManager.initializeGame(players);
        phaseManager.initializeGame(players);
	}
}
