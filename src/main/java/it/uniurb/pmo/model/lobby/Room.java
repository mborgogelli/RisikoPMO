package it.uniurb.pmo.model.lobby;


import it.uniurb.pmo.model.players.IPlayer;
import it.uniurb.pmo.model.players.Player;
import it.uniurb.pmo.model.utils.EnumColors;
import it.uniurb.pmo.model.utils.GameVersion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Room implements IRoom {
	
	private boolean isFull;
	private boolean isEmpty;
	private final int maxPlayers;
	private final GameVersion gameVersion;
	private final List<IPlayer> players;
	private List<EnumColors> availableColors;

	
	public Room(int maxPlayers, GameVersion gameVersion) {
		this.maxPlayers = maxPlayers;
		this.gameVersion = gameVersion;
		this.players = new ArrayList<>();
		this.availableColors = new ArrayList<>(EnumColors.getAvailableColors());
		this.isFull = false;
		this.isEmpty = true;
	}
	
	@Override
	public void enterRoom(String playerName) {
		if(!this.isFull) {
			this.addPlayer(playerName);
		} else  {
			throw new RuntimeException("The Room is full.");
		}
	}

	@Override
	public void exitRoom(String playerName) {
		this.removePlayer(playerName);
	}

	@Override
	public boolean isRoomFull() {
		return this.isFull;
	}

	@Override
	public boolean areAllPlayersReady() {
		return this.players.stream().allMatch(IPlayer::isReady);
	}

	@Override
	public List<IPlayer> getPlayersReady(String roomId) {
		return this.players.stream()
				.filter(IPlayer::isReady)
				.toList();
	}

	@Override
	public void setPlayerReady(String playerName, boolean isReady) {
		this.getPlayer(playerName).setReady(isReady);
	}

	@Override
	public int getNumberOfPlayers() {
		return this.players.size();
	}

	@Override
	public int getMaxPlayers() {
		return this.maxPlayers;
	}

	@Override
	public void kickPlayer(String playerName) {
		this.removePlayer(playerName);
	}

	@Override
	public GameVersion getRisikoVersion() {
		return this.gameVersion;
	}

	@Override
	public EnumColors getAssignedColor(String playerName) {
		return this.getPlayer(playerName).getColor();
	}
	
    @Override
	public boolean hasPlayer(String playerName) {
		return this.players.contains(this.getPlayer(playerName));
	}

	@Override
	public List<IPlayer> getPlayers() {
		return Collections.unmodifiableList(this.players);
	}

	private void removePlayer(String playerName){
		IPlayer p = this.getPlayer(playerName);
		this.availableColors.add(p.getColor());
		p.removeColor();
		this.players.remove(p);
		if (this.isFull){
			this.isFull = false;
		}
		if (this.players.isEmpty()){
			this.isEmpty = true;
		}
	}

	private void addPlayer(String playerName){
		this.players.add(new Player(playerName));
		this.getPlayer(playerName).setColor(this.pickRandomColor());
		if (this.isEmpty) {
			this.isEmpty = false;
		}
		if (this.players.size() == this.maxPlayers){
			this.isFull =  true;
		}
	}

	private EnumColors pickRandomColor() {
		int index = (int) (Math.random() * this.availableColors.size());
		EnumColors color = this.availableColors.get(index);
		this.availableColors.remove(index);
		return color;
	}

	private IPlayer getPlayer(String playerName){
		return this.players.stream()
				.filter(p -> p.getName().equals(playerName))
				.findFirst().orElseThrow();
	}

}
