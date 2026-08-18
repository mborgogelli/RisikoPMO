package it.uniurb.pmo.framework.turn.dto;

import it.uniurb.pmo.framework.players.IPlayer;
import it.uniurb.pmo.framework.turn.IPlayerRequestDTO;
import it.uniurb.pmo.framework.utils.EnumColors;

import java.util.List;

public class FortifyRequestDTO implements IPlayerRequestDTO {

	private final IPlayer player;
	private final List<String> ownedZones;

	public FortifyRequestDTO(IPlayer player, List<String> ownedZones) {
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
	public EnumColors playerColor() {
		return player.getColor();
	}
}
