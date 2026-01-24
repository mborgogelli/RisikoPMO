package model.lobby;

import model.players.Player;
import model.utils.EnumColors;
import model.utils.GameVersion;

public interface IRoom {
	
	void enterRoom(String playerName);
	
	void exitRoom(String playerName);
	
	void assignColor(Player player, String color);
	
	EnumColors getAssignedColor(String playerName);
	
	boolean isRoomFull();
	
	int getNumberOfPlayers();
	
	int getMaxPlayers();
	
	void kickPlayer(String playerName);
	
	GameVersion getRisikoVersion();

	boolean hasPlayer(String playerName);
}
