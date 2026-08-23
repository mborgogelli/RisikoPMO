package it.uniurb.pmo.variants.risikonew.management;

import it.uniurb.pmo.framework.card.ICard;
import it.uniurb.pmo.framework.card.ICardType;
import it.uniurb.pmo.framework.management.AbstractMediator;
import it.uniurb.pmo.framework.management.interfaces.IDirector;
import it.uniurb.pmo.framework.players.IPlayer;
import it.uniurb.pmo.variants.risikonew.management.interfaces.*;

import java.util.List;

public class MediatorRisikoNew extends AbstractMediator implements IMediatorRisikoNew {
	
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
	public int getZoneValue(String zone) {
		return this.mapManager.getZoneValue(zone);
	}

	@Override
	public boolean canMoveBetween(IPlayer player, String toZone, String fromZone) {
		return mapManager.canMoveBetween(player, toZone, fromZone);
	}

	@Override
	public List<String> getCompletedContinents(IPlayer player) {
		return this.mapManager.checkZoneCompletion(player);
	}

	@Override
	public void notifyWinner(IPlayer player) {
		this.director.declareWinner(player);
	}

	@Override
	public List<ICard> getPlayerCardsByType(IPlayer player, ICardType cardType) {
		return List.of();
	}

	@Override
	public void playCard(IPlayer player, ICard card) {
		this.cardManager.playCard(player, card);
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

}
