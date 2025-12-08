package model.versions.risikockassic.management;

import java.util.List;

import model.management.Mediator;
import model.management.interfaces.IDirector;
import model.players.IPlayer;

public class MediatorRisikoNew extends Mediator{
	
	private MapManagerRisikoNew mapManager;
	private CardManagerRisikoNew cardManager;
	private TankManager tankManager;
	private TurnManagerRisikoNew phaseManager;
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
		this.phaseManager = super.getManager(TurnManagerRisikoNew.class);
	}
	
	//TODO Rimuovere i getter
	public MapManagerRisikoNew getMapManager() {
		return this.mapManager;
	}
	
	public TankManager getTankManager() {
		return this.tankManager;
	}
}
