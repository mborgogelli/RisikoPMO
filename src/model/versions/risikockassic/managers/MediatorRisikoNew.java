package model.versions.risikockassic.managers;

import java.util.List;

import model.board.IZone;
import model.management.Mediator;
import model.management.interfaces.IDirector;
import model.players.IPlayer;

public class MediatorRisikoNew extends Mediator{
	
	private MapManagerRisikoNew mapManager;
	private CardManagerRisikoNew cardManager;
	private TankManager tokenManager;
	private PhaseManagerRisikoNew phaseManager;
	private IDirector director;
	
	
	@Override
	public List<IZone> getAllZones() {
		return mapManager.getAllZones();
	}

	@Override
	public List<IZone> getZonesOwnedBy(IPlayer player) {
		return mapManager.getZonesOwnedBy(player);
	}

	@Override
	public boolean canMoveBetween(IPlayer player, IZone toZone, IZone fromZone) {
		return mapManager.canMoveBetween(player, toZone, fromZone);
	}
	
	@Override
	public void initManagers() {
		this.mapManager = super.getManager(MapManagerRisikoNew.class);
		this.tokenManager = super.getManager(TankManager.class);
		this.cardManager = super.getManager(CardManagerRisikoNew.class);
		this.phaseManager = super.getManager(PhaseManagerRisikoNew.class);
	}
	
	public MapManagerRisikoNew getMapManager() {
		return this.mapManager;
	}
	
	public TankManager getTankManager() {
		return this.tokenManager;
	}
}
