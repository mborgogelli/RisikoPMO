package model.management;

import java.util.List;
import java.util.Map;
import java.util.Set;

import model.board.IZone;
import model.management.interfaces.IManager;
import model.management.interfaces.IRuleManager;
import model.players.IPlayer;
import model.utils.EnumToken;

/**
 * TokenManager gestisce i token nel gioco, sia quelli distribuiti nelle zone che quelli posseduti dai giocatori.
 * Fornisce metodi per assegnare, aggiungere e rimuovere token.
 */
public abstract class TokenManager implements IManager{
	
	private IRuleManager ruleManager;
	
	/**
	 * Restituisce i tipi di token gestiti da questo manager
	 * @return Set dei token gestiti
	 */
	protected abstract Set<EnumToken> getManagedTokens();
	
	protected abstract void resetTokenData();
	
	protected List<IZone> getAllZones() {
		return this.ruleManager.getAllZones();
	}

	public List<IZone> getZonesOwnedBy(IPlayer p) {
		return this.ruleManager.getZonesOwnedBy(p);
	}

	public void canMoveBetween(IPlayer player, IZone fromZone, IZone toZone) {
        this.ruleManager.canMoveBetween(player, fromZone, toZone);		
	}
	
	
}