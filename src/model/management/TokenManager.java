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
	
	/**
	 * Restituisce i tipi di token gestiti da questo manager
	 * @return Set dei token gestiti
	 */
	protected abstract Set<EnumToken> getManagedTokens();
	
	protected abstract void resetTokenData();
	
	@Override
	public void setMediator(Mediator mediator) {
		this.mediator = mediator;
		this.mediator.registerManager(this);
	}
	
	protected List<IZone> getAllZones() {
		return this.mediator.getAllZones();
	}

	public List<IZone> getZonesOwnedBy(IPlayer p) {
		return this.mediator.getZonesOwnedBy(p);
	}

	public void canMoveBetween(IPlayer player, IZone fromZone, IZone toZone) {
		// TODO Auto-generated method stub
		
	}

}