package it.uniurb.pmo.framework.management.interfaces;


import it.uniurb.pmo.framework.players.IPlayer;

public interface IDirector extends IGameConductor {
	
	void declareWinner(IPlayer player);

	void exitGame(IPlayer player);

}
