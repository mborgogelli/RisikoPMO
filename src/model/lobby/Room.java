package model.lobby;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.players.Player;
import model.utils.EnumColors;
import model.utils.GameVersion;

public class Room implements IRoom {
	
	private final Map<Player, EnumColors> players;
	private final int roomId;
	private final List<EnumColors> colors;
	
	
	public Room(int roomId) {
		this.roomId = roomId;
		this.players = new HashMap<>();
		this.colors = EnumColors.getAvailableColors();
	}
	
	@Override
	public void enterRoom(String playerName) {
		this.players.put(new Player(playerName), this.pickRandomColor());
	}

	@Override
	public void exitRoom(String playerName) {
	}

	@Override
	public void assignColor(String playerName, String color) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isRoomFull() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getNumberOfPlayers() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getMaxPlayers() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getRoomId() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void kickPlayer(String playerName) {
		// TODO Auto-generated method stub

	}

	@Override
	public GameVersion getRisikoVersion() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EnumColors getAssignedColor(String playerName) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private EnumColors pickRandomColor() {
		int index = (int) (Math.random() * this.colors.size());
		EnumColors color = this.colors.get(index);
		this.colors.remove(index);
		return color;
	}

}
