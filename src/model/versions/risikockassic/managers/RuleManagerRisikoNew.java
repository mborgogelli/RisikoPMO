package model.versions.risikockassic.managers;

import java.util.List;

import model.board.IZone;
import model.management.CardManager;
import model.management.RuleManager;
import model.management.TokenManager;
import model.management.interfaces.IMapManager;
import model.players.IPlayer;

public class RuleManagerRisikoNew extends RuleManager{
	
	
	private boolean isReady;
	
	private IMapManager mapManager;
    private TokenManager tokenManager;
    private CardManager cardManager;
    
    
    public RuleManagerRisikoNew() {
    }

	@Override
	public Boolean isReady() {
		return this.isReady;
	}

	@Override
	public void initializeGame(List<IPlayer> players) {
		// TODO Auto-generated method stub

	}

	@Override
	public void resetGame() {
		// TODO Auto-generated method stub

	}

	@Override
	public List<IZone> getAllZones() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<IZone> getZonesOwnedBy(IPlayer player) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean canMoveBetween(IPlayer player, IZone fromZone, IZone toZone) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public IMapManager getMapManager() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TokenManager getTokenManager() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CardManager getCardManager() {
		// TODO Auto-generated method stub
		return null;
	}

}
