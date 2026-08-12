package it.uniurb.pmo.framework.management;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.uniurb.pmo.framework.management.interfaces.IMediator;
import it.uniurb.pmo.framework.management.interfaces.ITokenManager;
import it.uniurb.pmo.framework.players.IPlayer;
import it.uniurb.pmo.framework.players.ITokenType;

/**
 * AbstractTokenManager gestisce i token nel gioco, sia quelli distribuiti nelle zone che quelli posseduti dai giocatori.
 * Fornisce metodi per assegnare, aggiungere e rimuovere token.
 */
public abstract class AbstractTokenManager implements ITokenManager{
	
	private IMediator mediator;
	private final Map<IPlayer, Map<ITokenType, Integer>> playerTokens;

	public AbstractTokenManager() {
		this.playerTokens = new HashMap<>();
	}

	@Override
	public void setMediator(IMediator mediator) {
		this.mediator = mediator;
		this.mediator.registerManager(this);
	}

	@Override
	public int getPlayerToken(IPlayer player, ITokenType type) {
		return this.getPlayerTokenAmount(player, type);
	}

	@Override
	public void assignToken(IPlayer player, ITokenType type, int token) {
		if (token >= 0) {
			this.addPlayerTokenAmount(player, type, token);
		} else {
			this.removePlayerTokenAmount(player, type, -token);
		}
	}

	@Override
	public void assignToken(IPlayer player, int token) {
		this.assignToken(player, this.getDefaultTokenType(), token);
	}

	@Override
	public void removeToken(IPlayer player, ITokenType type, int token) {
		this.removePlayerTokenAmount(player, type, token);
	}

	@Override
	public void removeToken(IPlayer player, int token) {
		this.removeToken(player, this.getDefaultTokenType(), token);
	}

	protected abstract void resetTokenData();

	protected abstract ITokenType getDefaultTokenType();

	protected final int getPlayerTokenAmount(IPlayer player, ITokenType type) {
		return this.playerTokens.getOrDefault(player, Map.of()).getOrDefault(type, 0);
	}

	protected final void addPlayerTokenAmount(IPlayer player, ITokenType type, int amount) {
		if (amount < 0) {
			throw new IllegalArgumentException("Token amount cannot be negative");
		}
		if (amount > 0) {
			Map<ITokenType, Integer> tokens = this.playerTokens.computeIfAbsent(player, key -> new HashMap<>());
			tokens.put(type, tokens.getOrDefault(type, 0) + amount);
		}
	}

	protected final void removePlayerTokenAmount(IPlayer player, ITokenType type, int amount) {
		if (amount < 0) {
			throw new IllegalArgumentException("Token amount cannot be negative");
		}
		if (amount > 0) {
			Map<ITokenType, Integer> tokens = this.playerTokens.computeIfAbsent(player, key -> new HashMap<>());
			int currentAmount = tokens.getOrDefault(type, 0);
			tokens.put(type, Math.max(currentAmount - amount, 0));
		}
	}

	protected final void clearPlayerTokenData() {
		this.playerTokens.clear();
	}

	protected List<String> getAllZones() {
		return this.mediator.getAllZones();
	}

	protected List<String> getZonesOwnedBy(IPlayer p) {
		return this.mediator.getZonesOwnedBy(p);
	}

	protected boolean canMoveBetween(IPlayer player, String toZone, String fromZone) {
		return this.mediator.canMoveBetween(player, toZone, fromZone);
	}

}