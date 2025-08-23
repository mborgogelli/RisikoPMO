package model.players;

import java.util.Map;

import model.utils.EnumColors;
import model.utils.EnumToken;

public interface IPlayer {
	
	String getName();

	Boolean isReady();

	EnumColors getColor();
	
	/*Map<EnumToken, Integer> getAllTokens();
	
	Integer getToken(EnumToken token);

	void addToken(EnumToken token, Integer amount);*/
	
	void setReady(Boolean ready);
}
