package it.uniurb.pmo.framework.players;

import it.uniurb.pmo.framework.utils.EColors;

public interface IPlayer {

	//TO DO remove setter/getter for Color
	String getName();

	Boolean isReady();

	EColors getColor();

	void setColor(EColors color);

	void removeColor();

	void setReady(Boolean ready);

	PlayerTurnStatus getPlayerTurnStatus();

	void setPlayerTurnStatus(PlayerTurnStatus playerTurnStatus);
}
