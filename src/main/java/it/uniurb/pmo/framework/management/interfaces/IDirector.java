package it.uniurb.pmo.framework.management.interfaces;


import it.uniurb.pmo.framework.players.IPlayer;

public interface IDirector extends IGameConductor {
	
	// Dichiara il vincitore del gioco
	void declareWinner(IPlayer player);

	// Gestisce l'uscita di un giocatore dal gioco
	void exitGame(IPlayer player);

}
