package it.uniurb.pmo.model.players;

import it.uniurb.pmo.model.utils.EnumColors;

public interface IPlayer {
	
	String getName();

	Boolean isReady();

	EnumColors getColor();
	
	void setColor(EnumColors color);
	
	void setReady(Boolean ready);
}
