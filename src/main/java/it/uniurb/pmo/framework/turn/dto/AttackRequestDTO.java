package it.uniurb.pmo.framework.turn.dto;

import it.uniurb.pmo.framework.players.IPlayer;
import it.uniurb.pmo.framework.turn.IPlayerRequestDTO;

import java.util.List;

public class AttackRequestDTO implements IPlayerRequestDTO {

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
}
