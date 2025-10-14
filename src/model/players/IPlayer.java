package model.players;

import model.utils.EnumColors;

public interface IPlayer {
	
	String getName();

	Boolean isReady();

	EnumColors getColor();
	
	void setReady(Boolean ready);
}
