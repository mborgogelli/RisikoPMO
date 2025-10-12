package tests;

import org.junit.jupiter.api.Test;

import model.versions.risikockassic.IMapManagerRisikoNew;
import model.versions.risikockassic.TestMapManager;
import model.versions.risikockassic.board.BoardCreatorRisikoNew;

public class TestMapManagerTest {

	@Test
	public void testSOmething() {
		IMapManagerRisikoNew mapManager = new TestMapManager(BoardCreatorRisikoNew.getInstance());
		
		mapManager.getOwnedTerritories(null);
	}
	
	
}
