package it.uniurb.pmo.variants.risikonew.management;

import java.util.List;
import java.util.Map;

import it.uniurb.pmo.framework.management.AbstractMediator;
import it.uniurb.pmo.framework.management.interfaces.IDirector;
import it.uniurb.pmo.framework.players.IPlayer;
import it.uniurb.pmo.framework.players.IPlayerInputProvider;
import it.uniurb.pmo.variants.risikonew.management.interfaces.ICardManagerRisikoNew;
import it.uniurb.pmo.variants.risikonew.management.interfaces.IMapManagerRisikoNew;
import it.uniurb.pmo.variants.risikonew.management.interfaces.IMediatorRisikoNew;
import it.uniurb.pmo.variants.risikonew.management.interfaces.ITankManager;
import it.uniurb.pmo.variants.risikonew.management.interfaces.ITurnManagerRisikoNew;

public class MediatorRisikoNew extends AbstractMediator implements IMediatorRisikoNew {
	
	private IMapManagerRisikoNew mapManager;
	private ICardManagerRisikoNew cardManager;
	private ITankManager tankManager;
	private ITurnManagerRisikoNew turnManager;
	private IDirector director;
	private IPlayerInputProvider playerInputProvider;
	
	
	@Override
	public List<String> getAllZones() {
		return mapManager.getAllZones();
	}

	@Override
	public List<String> getZonesOwnedBy(IPlayer player) {
		return mapManager.getZonesOwnedBy(player);
	}

	@Override
	public boolean canMoveBetween(IPlayer player, String toZone, String fromZone) {
		return mapManager.canMoveBetween(player, toZone, fromZone);
	}

	@Override
	public void notifyWinner(IPlayer player) {
		this.director.declareWinner(player);
	}

	@Override
	public void initManagers() {
		this.mapManager = super.resolveManager(MapManagerRisikoNew.class);
		this.tankManager = super.resolveManager(TankManager.class);
		this.cardManager = super.resolveManager(CardManagerRisikoNew.class);
		this.turnManager = super.resolveManager(TurnManagerRisikoNew.class);
	}

	@Override
	public void startGame() {
		this.turnManager.startGame();
	}

	@Override
	public void stopGame() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resetGame() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getPlayerTank(IPlayer player) {
		return this.tankManager.getPlayerTank(player);
	}

	@Override
	public int getZoneTank(String zone) {
		return this.tankManager.getZoneTank(zone);
	}

	@Override
	public void deployTank(IPlayer player, String zone, int tanks) {
		this.tankManager.deployTank(player, zone, tanks);
	}

	@Override
	public void setPlayerInputProvider(IPlayerInputProvider playerInputProvider) {
		this.playerInputProvider = playerInputProvider;
	}

	@Override
	public Map<String, Integer> acquireTargetZones(IPlayer player, int toDeploy) {
		return this.playerInputProvider.acquireDeployment(player, toDeploy);
	}
}
