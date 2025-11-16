package model.management;

import java.util.List;
import java.util.Map;
import java.util.Set;

import model.board.IZone;
import model.management.interfaces.IManager;
import model.management.interfaces.IMediator;
import model.players.IPlayer;
import model.utils.EnumToken;

/**
 * TokenManager gestisce i token nel gioco, sia quelli distribuiti nelle zone che quelli posseduti dai giocatori.
 * Fornisce metodi per assegnare, aggiungere e rimuovere token.
 */
public abstract class TokenManager implements IManager{
	
	private Mediator mediator;
	
	protected abstract void resetTokenData();
	
	@Override
	public void setMediator(Mediator mediator) {
		this.mediator = mediator;
		this.mediator.registerManager(this);
	}
	
	protected List<IZone> getAllZones() {
		return this.mediator.getAllZones();
	}

	protected List<IZone> getZonesOwnedBy(IPlayer p) {
		return this.mediator.getZonesOwnedBy(p);
	}

	protected void canMoveBetween(IPlayer player, IZone toZone, IZone fromZone) {
		this.mediator.canMoveBetween(player, toZone, fromZone);
	}

}