package it.uniurb.pmo.model.versions.risikockassic.management;

import it.uniurb.pmo.model.management.Mediator;
import it.uniurb.pmo.model.management.interfaces.IDirector;
import it.uniurb.pmo.model.players.IPlayer;

import java.util.List;

public class MediatorRisikoNew extends Mediator{
	
	private MapManagerRisikoNew mapManager;
	private CardManagerRisikoNew cardManager;
	private TankManager tankManager;
	private TurnManagerRisikoNew turnManager;
	private IDirector director;
	
	
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
	public void initManagers() {
		this.mapManager = super.getManager(MapManagerRisikoNew.class);
		this.tankManager = super.getManager(TankManager.class);
		this.cardManager = super.getManager(CardManagerRisikoNew.class);
		this.turnManager = super.getManager(TurnManagerRisikoNew.class);
	}

	@Override
	public void startGame() {
		this.turnManager.startTurn();
	}

	//TODO Rimuovere i getter
	public MapManagerRisikoNew getMapManager() {
		return this.mapManager;
	}
	
	public TankManager getTankManager() {
		return this.tankManager;
	}
}
