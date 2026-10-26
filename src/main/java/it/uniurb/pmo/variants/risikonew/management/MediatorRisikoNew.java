package it.uniurb.pmo.variants.risikonew.management;

import it.uniurb.pmo.framework.card.ICard;
import it.uniurb.pmo.framework.card.ICardType;
import it.uniurb.pmo.framework.management.AbstractMediator;
import it.uniurb.pmo.framework.players.IPlayer;
import it.uniurb.pmo.framework.players.ITokenType;
import it.uniurb.pmo.variants.risikonew.card.ITerritoryCard;
import it.uniurb.pmo.variants.risikonew.management.interfaces.*;
import it.uniurb.pmo.variants.risikonew.utils.ERisikoNewToken;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class MediatorRisikoNew extends AbstractMediator implements IMediatorRisikoNew {
	
	private IMapManagerRisikoNew mapManager;
	private ICardManagerRisikoNew cardManager;
	private ITankManager tankManager;
	private ITurnManagerRisikoNew turnManager;

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
	public void notifyWinner(IPlayer player) {
		super.notifyWinner(player);
	}

	// Board Management

	@Override
	public int getZoneValue(String zone) {
		return this.mapManager.getZoneValue(zone);
	}

	@Override
	public List<String> getNeighboursOf(String zone) {
		return this.mapManager.getNeighboursOf(zone);
	}

	@Override
	public List<String> getCompletedContinents(IPlayer player) {
		return this.mapManager.checkZoneCompletion(player);
	}

	@Override
	public IPlayer getZoneOwner(String zone) {
		return this.mapManager.getOwner(zone);
	}

	@Override
	public void changeZoneOwnership(String zone, IPlayer newOwner) {
		this.mapManager.updateOwnership(newOwner, zone);
	}


	// Card Management

	@Override
	public List<ICard> getPlayerCardsByType(IPlayer player, ICardType cardType) {
		return List.of();
	}

	@Override
	public void playCard(IPlayer player, ICard card) {
		this.cardManager.playCard(player, card);
	}

	@Override
	public <T extends ICard> Stream<List<T>> getCombinationsOf(List<T> playerCards, int k) {
		return cardManager.getCombinationsOf(playerCards, k);
	}

	@Override
	public Stream<List<ITerritoryCard>> getAvailableTris(IPlayer player) {
		return this.cardManager.getAvailableTris(player);
	}

	@Override
	public void playTris(IPlayer player, List<? extends ITerritoryCard> cards) {
		this.cardManager.playTris(player, cards);
	}


	// Tank Management

	@Override
	public int getPlayerTank(IPlayer player) {
		return this.tankManager.getPlayerTank(player);
	}

	@Override
	public int getZoneTokenByType(String zone, ITokenType tokenType) {
		return this.tankManager.getZoneTank(zone);
	}

	@Override
	public Map<ITokenType, Integer> getZoneTokens(String zone) {
		return Map.of(ERisikoNewToken.TANK,this.getZoneTank(zone));
	}

	@Override
	public Map<ITokenType, Integer> getTokensOwnedBy(IPlayer player) {
		return this.tankManager.getPlayerTokens(player);
	}

	@Override
	public void removeTokens(IPlayer player, ITokenType tokenType, int tokens) {
		this.tankManager.removeTank(player, tokens);
	}

	@Override
	public void deployTank(IPlayer player, String zone, int tanks) {
		this.tankManager.deployTank(player, zone, tanks);
	}

	@Override
	public void addTokens(IPlayer player, ITokenType tokenType, int reinforcements) {
		this.tankManager.assignTank(player, reinforcements);
	}

	@Override
	public void removeTokensFromZone(String zone, ITokenType tokenType, int tokens) {
		this.tankManager.removeTanksFromZone(zone, tokens);
	}

	@Override
	public void addTokensToZone(String zone, ITokenType tokenType, int tokens) {
		this.tankManager.addTanksToZone(zone, tokens);
	}

	@Override
	public void transferTokens(String fromZone, String toZone, ITokenType tokenType, int tokens) {
		this.tankManager.removeTanksFromZone(fromZone, tokens);
		this.tankManager.addTanksToZone(toZone, tokens);
	}



}
