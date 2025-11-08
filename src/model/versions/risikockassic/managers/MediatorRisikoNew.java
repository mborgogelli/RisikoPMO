package model.versions.risikockassic.managers;

import java.util.List;

import model.board.IZone;
import model.management.CardManager;
import model.management.Mediator;
import model.management.TokenManager;
import model.management.interfaces.IDirector;
import model.management.interfaces.IManager;
import model.management.interfaces.IMapManager;
import model.management.interfaces.IMediator;
import model.players.IPlayer;

public class MediatorRisikoNew extends Mediator{
	
	private MapManagerRisikoNew mapManager;
	private CardManagerRisikoNew cardManager;
	private TankManager tokenManager;
	private PhaseManagerRisikoNew phaseManager;
	private IDirector director;
	
	
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
	public void initManagers() {
		this.mapManager = super.getManager(MapManagerRisikoNew.class);
		this.cardManager = super.getManager(CardManagerRisikoNew.class);
		this.tokenManager = super.getManager(TankManager.class);
		this.phaseManager = super.getManager(PhaseManagerRisikoNew.class);
	}
}
