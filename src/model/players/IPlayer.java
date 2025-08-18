package model.players;


import model.utils.EnumColors;

public interface IPlayer {
	
	String getName();

	Boolean isReady();

	void setReady(Boolean ready);

	EnumColors getColor();
	
}
