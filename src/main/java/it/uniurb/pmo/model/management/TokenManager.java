package it.uniurb.pmo.model.management;

import java.util.List;

import it.uniurb.pmo.model.management.interfaces.ITokenManager;
import it.uniurb.pmo.model.players.IPlayer;

/**
 * TokenManager gestisce i token nel gioco, sia quelli distribuiti nelle zone che quelli posseduti dai giocatori.
 * Fornisce metodi per assegnare, aggiungere e rimuovere token.
 */
public abstract class TokenManager implements ITokenManager{
	
	private Mediator mediator;
	
	protected abstract void resetTokenData();
	
	@Override
	public void setMediator(Mediator mediator) {
		this.mediator = mediator;
		this.mediator.registerManager(this);
	}
	
	protected List<String> getAllZones() {
		return this.mediator.getAllZones();
	}

	protected List<String> getZonesOwnedBy(IPlayer p) {
		return this.mediator.getZonesOwnedBy(p);
	}

	protected void canMoveBetween(IPlayer player, String toZone, String fromZone) {
		this.mediator.canMoveBetween(player, toZone, fromZone);
	}

}