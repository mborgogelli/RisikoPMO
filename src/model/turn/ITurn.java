package model.turn;

import model.players.IPlayer;

public interface ITurn {
	
	void playTurn(IPlayer player);
	
	IPlayer whoIsPlaying();
	
	IPlayer whoIsNext();
	
	void endTurn();

}
