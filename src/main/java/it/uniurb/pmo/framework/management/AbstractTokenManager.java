package it.uniurb.pmo.framework.management;

import java.util.List;

import it.uniurb.pmo.framework.management.interfaces.IMediator;
import it.uniurb.pmo.framework.management.interfaces.ITokenManager;
import it.uniurb.pmo.framework.players.IPlayer;

/**
 * AbstractTokenManager gestisce i token nel gioco, sia quelli distribuiti nelle zone che quelli posseduti dai giocatori.
 * Fornisce metodi per assegnare, aggiungere e rimuovere token.
 */
public abstract class AbstractTokenManager implements ITokenManager{
	
	private IMediator mediator;
	
	protected abstract void resetTokenData();
	
	@Override
	public void setMediator(IMediator mediator) {
		this.mediator = mediator;
		this.mediator.registerManager(this);
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