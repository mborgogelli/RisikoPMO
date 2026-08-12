package it.uniurb.pmo.variants.risikonew.management;

import it.uniurb.pmo.framework.management.AbstractTokenManager;
import it.uniurb.pmo.framework.players.IPlayer;
import it.uniurb.pmo.framework.players.ITokenType;
import it.uniurb.pmo.variants.risikonew.management.interfaces.ITankManager;
import it.uniurb.pmo.variants.risikonew.utils.ERisikoNewToken;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TankManager extends AbstractTokenManager implements ITankManager {

	private boolean isReady;
	private final Map<String, Integer> deployedTank;
	private final Map<IPlayer, Integer> availableTanks;

	public TankManager() {
		super();
		this.isReady = false;
		this.deployedTank = new HashMap<>();
		this.availableTanks = new HashMap<>();
	}

	@Override
	protected ERisikoNewToken getDefaultTokenType() {
		return ERisikoNewToken.TANK;
	}

	@Override
	public Boolean isReady() {
		return this.isReady;
	}

	@Override
	public void resetGame() {
		this.isReady = false;
		this.resetTokenData();
	}

	@Override
	public void initializeGame(List<IPlayer> players) {
		checkInitialized();
		this.initTokensPerPlayer(players);
		this.initTokensPerZone(super.getAllZones(), players);
		this.isReady = true;
	}

	@Override
	public int getPlayerToken(IPlayer player) {
		return this.availableTanks.getOrDefault(player, 0);
	}

	@Override
	public int getPlayerToken(IPlayer player, ITokenType type) {
		if (type == ERisikoNewToken.TANK) {
			return this.getPlayerToken(player);
		}
		return 0;
	}

	@Override
	public int getZoneToken(String zone) {
		checkReady();
		return this.deployedTank.getOrDefault(zone, 0);
	}

	@Override
	public Map<String, Integer> getDeployedPerZone(IPlayer player) {
		return this.deployedTank.entrySet().stream()
				.filter(entry -> super.getZonesOwnedBy(player).contains(entry.getKey()))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
	}

	@Override
	public int getTotalDeployed(IPlayer player) {
		return this.getDeployedPerZone(player).values().stream().reduce(0, Integer::sum);
	}

	@Override
	public void deployToken(IPlayer player, String zone, int amount) {
		this.deployToken(player, ERisikoNewToken.TANK, zone, amount);
	}

	@Override
	public void deployToken(IPlayer player, ITokenType type, String zone, int amount) {
		checkReady();
		if (type != ERisikoNewToken.TANK) {
			throw new IllegalArgumentException("Unsupported token type: " + type);
		}
		checkIfPlayerHasTank(player, amount);
		checkIfPlayerOwnsZone(player, zone);
		this.addTanksToZone(zone, amount);
		this.removeTanksFromPlayer(player, amount);
	}

	@Override
	public void moveToken(IPlayer player, String toZone, String fromZone, int amount) {
		this.moveToken(player, ERisikoNewToken.TANK, toZone, fromZone, amount);
	}

	@Override
	public void moveToken(IPlayer player, ITokenType type, String toZone, String fromZone, int amount) {
		checkReady();
		if (type != ERisikoNewToken.TANK) {
			throw new IllegalArgumentException("Unsupported token type: " + type);
		}
		checkIfPlayerCanMoveBetween(player, toZone, fromZone);
		checkDeployedTanksAfterMove(fromZone, amount);
		this.removeTanksFromZone(fromZone, amount);
		this.addTanksToZone(toZone, amount);
	}

	@Override
	protected void resetTokenData() {
		checkInitialized();
		this.clearPlayerTokenData();
		this.availableTanks.clear();
		this.deployedTank.clear();
	}

	private void initTokensPerPlayer(List<IPlayer> players) {
		checkInitialized();
		int tanksPerPlayer = this.calculateTanksPerPlayer(players.size());
		players.forEach(p -> this.availableTanks.put(p, tanksPerPlayer));
	}

	private void initTokensPerZone(List<String> territories, List<IPlayer> players) {
		checkInitialized();
		territories.forEach(z -> this.deployedTank.put(z, 1));
		players.forEach(p -> this.availableTanks.put(p, this.availableTanks.getOrDefault(p, 0) - super.getZonesOwnedBy(p).size()));
	}

	private int calculateTanksPerPlayer(int playerCount) {
		return switch (playerCount) {
			case 3 -> 35;
			case 4 -> 30;
			case 5 -> 25;
			case 6 -> 20;
			default -> throw new IllegalArgumentException("Invalid number of players: " + playerCount);
		};
	}

	private void removeTanksFromPlayer(IPlayer player, int amount) {
		int newAmount = this.getPlayerToken(player) - amount;
		this.availableTanks.put(player, Math.max(newAmount, 0));
	}

	private void removeTanksFromZone(String zone, int amount) {
		int newAmount = this.getZoneToken(zone) - amount;
		this.deployedTank.put(zone, Math.max(newAmount, 0));
	}

	private void addTanksToZone(String zone, int amount) {
		int newAmount = this.getZoneToken(zone) + amount;
		this.deployedTank.put(zone, newAmount);
	}

	private void checkIfPlayerHasTank(IPlayer player, int amount) {
		if (this.getPlayerToken(player) - amount < 0) {
			throw new IllegalStateException("Player " + player + " does not have enough tanks to deploy " + amount);
		}
	}

	private void checkDeployedTanksAfterMove(String fromZone, int amount) {
		int current = this.getZoneToken(fromZone);
		if (amount <= 0 || (current - amount < 1)) {
			throw new IllegalArgumentException("Cannot move " + amount + "tanks. Amount must be greater than 0 and less than or equal to " + (current - 1));
		}
	}

	private void checkIfPlayerCanMoveBetween(IPlayer player, String toZone, String fromZone) {
		if (!super.canMoveBetween(player, toZone, fromZone)) {
			throw new IllegalStateException("Cannot move tanks from " + fromZone + " to " + toZone + " for player " + player);
		}
	}

	private void checkIfPlayerOwnsZone(IPlayer player, String zone) {
		if (!super.getZonesOwnedBy(player).contains(zone)) {
			throw new IllegalStateException("Player " + player + " does not own zone " + zone);
		}
	}

	@Override
	public void deployTank(IPlayer player, String zone, int tank) {
		this.deployToken(player, zone, tank);
	}

	@Override
	public void moveTank(IPlayer player, String toZone, String fromZone, int tanks) {
		this.moveToken(player, toZone, fromZone, tanks);
	}

	private void checkReady() {
		if (!this.isReady) {
			throw new IllegalStateException(this.getClass().getSimpleName() + " must be initialized before use.");
		}
	}

	private void checkInitialized() {
		if (this.isReady) {
			throw new IllegalStateException(this.getClass().getSimpleName() + " is already initialized.");
		}
	}

}
