package it.uniurb.pmo.framework.lobby;

import it.uniurb.pmo.framework.players.IPlayer;
import it.uniurb.pmo.framework.utils.EnumColors;
import it.uniurb.pmo.framework.utils.GameVersion;

import java.util.List;

public interface IRoom {
	
	void enterRoom(String playerName);
	
	void exitRoom(String playerName);
	
	EnumColors getAssignedColor(String playerName);
	
	boolean isRoomFull();

	boolean areAllPlayersReady();

	List<IPlayer> getPlayersReady(String roomId);

	public void setPlayerReady(String playerName, boolean isReady);
	
	int getNumberOfPlayers();
	
	int getMaxPlayers();
	
	void kickPlayer(String playerName);
	
	GameVersion getRisikoVersion();

	boolean hasPlayer(String playerName);

	List<IPlayer> getPlayers();
}
