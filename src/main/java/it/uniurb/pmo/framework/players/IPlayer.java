package it.uniurb.pmo.framework.players;

import it.uniurb.pmo.framework.utils.EnumColors;

public interface IPlayer {

	//TO DO remove setter/getter for Color
	String getName();

	Boolean isReady();

	EnumColors getColor();

	void setColor(EnumColors color);

	void removeColor();

	void setReady(Boolean ready);

	PlayerTurnStatus getPlayerTurnStatus();

	void setPlayerTurnStatus(PlayerTurnStatus playerTurnStatus);
}
