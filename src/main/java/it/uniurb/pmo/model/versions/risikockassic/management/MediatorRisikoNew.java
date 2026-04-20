package it.uniurb.pmo.model.versions.risikockassic.management;

import it.uniurb.pmo.model.management.AbstractMediator;
import it.uniurb.pmo.model.management.interfaces.IDirector;
import it.uniurb.pmo.model.players.IPlayer;
import it.uniurb.pmo.model.versions.risikockassic.management.interfaces.ICardManagerRisikoNew;
import it.uniurb.pmo.model.versions.risikockassic.management.interfaces.IMapManagerRisikoNew;
import it.uniurb.pmo.model.versions.risikockassic.management.interfaces.ITankManager;
import it.uniurb.pmo.model.versions.risikockassic.management.interfaces.ITurnManagerRisikoNew;

import java.util.List;

public class MediatorRisikoNew extends AbstractMediator {
	
	private IMapManagerRisikoNew mapManager;
	private ICardManagerRisikoNew cardManager;
	private ITankManager tankManager;
	private ITurnManagerRisikoNew turnManager;
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
}
