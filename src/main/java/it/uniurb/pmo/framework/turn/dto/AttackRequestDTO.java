package it.uniurb.pmo.framework.turn.dto;

import it.uniurb.pmo.framework.players.IPlayer;
import it.uniurb.pmo.framework.turn.IPlayerDataDTO;
import it.uniurb.pmo.framework.utils.EColors;

import java.util.List;

public class AttackRequestDTO implements IPlayerDataDTO {

	private final IPlayer player;
	private final List<String> ownedZones;

	public AttackRequestDTO(IPlayer player, List<String> ownedZones) {
		this.player = player;
		this.ownedZones = ownedZones;
	}

	public IPlayer getPlayer() {
		return this.player;
	}

	public List<String> getOwnedZones() {
		return this.ownedZones;
	}

	@Override
	public String playerName() {
		return player.getName();
	}

	@Override
	public EColors playerColor() {
		return player.getColor();
	}
}
