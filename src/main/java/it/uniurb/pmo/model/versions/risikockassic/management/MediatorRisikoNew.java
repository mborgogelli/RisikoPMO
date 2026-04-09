package it.uniurb.pmo.model.versions.risikockassic.management;

import it.uniurb.pmo.model.management.Mediator;
import it.uniurb.pmo.model.management.interfaces.IDirector;
import it.uniurb.pmo.model.players.IPlayer;
import it.uniurb.pmo.model.versions.risikockassic.management.interfaces.ICardManagerRisikoNew;
import it.uniurb.pmo.model.versions.risikockassic.management.interfaces.IMapManagerRisikoNew;
import it.uniurb.pmo.model.versions.risikockassic.management.interfaces.ITankManager;
import it.uniurb.pmo.model.versions.risikockassic.management.interfaces.ITurnManagerRisikoNew;

import java.util.List;

public class MediatorRisikoNew extends Mediator {
	
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
	public IMapManagerRisikoNew getMapManager() {
		return this.mapManager;
	}
	
	public ITankManager getTankManager() {
		return this.tankManager;
	}

	public ITurnManagerRisikoNew getTurnManager(){
		return this.turnManager;
	}
}
