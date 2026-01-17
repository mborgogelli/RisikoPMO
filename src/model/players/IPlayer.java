package model.players;

import java.util.Optional;

import model.utils.EnumColors;

public interface IPlayer {
	
	String getName();

	Boolean isReady();

	EnumColors getColor();
	
	void setColor(EnumColors color);
	
	void setReady(Boolean ready);
}
