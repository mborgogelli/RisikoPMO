package it.uniurb.pmo.model.players;

import it.uniurb.pmo.model.utils.EnumColors;

public interface IPlayer {

	//TO DO remove setter/getter for Color
	String getName();

	Boolean isReady();

	EnumColors getColor();

	void setColor(EnumColors color);

	void removeColor();

	void setReady(Boolean ready);
}
