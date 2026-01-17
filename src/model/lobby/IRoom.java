package model.lobby;

import model.utils.EnumColors;
import model.utils.GameVersion;

public interface IRoom {
	
	void enterRoom(String playerName);
	
	void exitRoom(String playerName);
	
	void assignColor(String playerName, String color);
	
	EnumColors getAssignedColor(String playerName);
	
	boolean isRoomFull();
	
	int getNumberOfPlayers();
	
	int getMaxPlayers();
	
	int getRoomId();
	
	void kickPlayer(String playerName);
	
	GameVersion getRisikoVersion();

}
