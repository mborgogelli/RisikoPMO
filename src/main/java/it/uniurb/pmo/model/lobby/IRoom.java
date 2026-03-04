package it.uniurb.pmo.model.lobby;

import it.uniurb.pmo.model.players.Player;
import it.uniurb.pmo.model.utils.EnumColors;
import it.uniurb.pmo.model.utils.GameVersion;

import java.util.Map;

public interface IRoom {
	
	void enterRoom(String playerName);
	
	void exitRoom(String playerName);
	
	EnumColors getAssignedColor(String playerName);
	
	boolean isRoomFull();
	
	int getNumberOfPlayers();
	
	int getMaxPlayers();
	
	void kickPlayer(String playerName);
	
	GameVersion getRisikoVersion();

	boolean hasPlayer(String playerName);

	Map<Player, EnumColors> getPlayers();
}
