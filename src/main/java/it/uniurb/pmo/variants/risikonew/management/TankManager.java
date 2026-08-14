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
	private final Map<String, Integer> territoryTanks;
	private final ITokenType defaultTokenType;

	public TankManager() {
		super();
		this.defaultTokenType = ERisikoNewToken.TANK;
		this.isReady = false;
		this.territoryTanks = new HashMap<>();
	}

	@Override
	public Boolean isReady() {
		return this.isReady;
	}

	@Override
	public void initializeGame(List<IPlayer> players) {
		checkInitialized();
		this.initTokensPerPlayer(players);
		this.initTokensPerZone(super.getAllZones(), players);
		this.isReady = true;
	}

	public Map<String, Integer> getDeployedPerZone(IPlayer player) {
		return super.getZonesOwnedBy(player).stream()
				.collect(Collectors.toMap(z -> z, z -> this.territoryTanks.getOrDefault(z, 0)));
	}

	@Override
	public int getTotalDeployed(IPlayer player, ITokenType type) {
		if (type != this.defaultTokenType) {
			throw new IllegalArgumentException("Unsupported token type: " + type);
		}
		return this.territoryTanks.keySet().stream()
				.filter(z -> super.getZonesOwnedBy(player).contains(z))
				.mapToInt(z -> this.territoryTanks.getOrDefault(z, 0))
				.sum();
	}

	@Override
	public int getPlayerToken(IPlayer player, ITokenType type) {
		if (type != this.defaultTokenType) {
			throw new IllegalArgumentException("Unsupported token type: " + type);
		}
		return super.getPlayerToken(player, type);
	}

	@Override
	public Map<ITokenType, Integer> getZoneToken(String zone) {
		checkReady();
		return Map.of(this.defaultTokenType, this.territoryTanks.getOrDefault(zone, 0));
	}

	@Override
	public int getTotalDeployed(IPlayer player) {
		return this.getDeployedPerZone(player).values().stream().reduce(0, Integer::sum);
	}

	@Override
	public void deployToken(IPlayer player, ITokenType type, String zone, int amount) {
		checkReady();
		if (type != this.defaultTokenType) {
			throw new IllegalArgumentException("Unsupported token type: " + type);
		}
		checkIfPlayerHasTank(player, amount);
		checkIfPlayerOwnsZone(player, zone);
		this.addTanksToZone(zone, amount);
		this.removeTanksFromPlayer(player, amount);
	}

	@Override
	public void moveToken(IPlayer player, ITokenType type, String toZone, String fromZone, int amount) {
		checkReady();
		if (type != this.getDefaultTokenType()) {
			throw new IllegalArgumentException("Unsupported token type: " + type);
		}
		checkIfPlayerCanMoveBetween(player, toZone, fromZone);
		checkDeployedTanksAfterMove(fromZone, amount);
		this.removeTanksFromZone(fromZone, amount);
		this.addTanksToZone(toZone, amount);
	}

	@Override
	public ERisikoNewToken getDefaultTokenType() {
		return ERisikoNewToken.TANK;
	}

	@Override
	protected void resetTokenData() {
		checkInitialized();
		this.territoryTanks.clear();
	}

	private void initTokensPerPlayer(List<IPlayer> players) {
		checkInitialized();
		int tanksPerPlayer = this.calculateTanksPerPlayer(players.size());
		players.forEach(p -> super.addPlayerTokenAmount(p, this.defaultTokenType, tanksPerPlayer));
	}

	private void initTokensPerZone(List<String> territories, List<IPlayer> players) {
		checkInitialized();
		territories.forEach(z -> this.territoryTanks.put(z, 1));
		players.forEach(p -> super.removePlayerTokenAmount(p, this.defaultTokenType, super.getZonesOwnedBy(p).size()));
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
		super.removePlayerTokenAmount(player, this.defaultTokenType, amount);
	}

	private void removeTanksFromZone(String zone, int amount) {
		int newAmount = this.territoryTanks.getOrDefault(zone, 0) - amount;
		this.territoryTanks.put(zone, Math.max(newAmount, 0));
	}

	private void addTanksToZone(String zone, int amount) {
		int newAmount = this.territoryTanks.getOrDefault(zone, 0) + amount;
		this.territoryTanks.put(zone, newAmount);
	}

	private void checkIfPlayerHasTank(IPlayer player, int amount) {
		if (this.getPlayerTank(player) - amount < 0) {
			throw new IllegalStateException("Player " + player + " does not have enough tanks to deploy " + amount);
		}
	}

	private void checkDeployedTanksAfterMove(String fromZone, int amount) {
		int current = this.territoryTanks.getOrDefault(fromZone, 0);
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
