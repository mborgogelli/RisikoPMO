package tests;

import java.util.List;

import org.junit.jupiter.api.Test;

import model.versions.risikockassic.card.EnumMissionCard;

public class EnumMissionTest {

    
    @Test
    public void testGetAllMissions() {
        List<String> missions = null;
		try {
			missions = EnumMissionCard.getAllMissions();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        int i = 1;
        assert !missions.isEmpty() : "La lista delle missioni non dovrebbe essere vuota";
        for (String string : missions) {
        	System.out.println("Mission " + i + ": " + string);
        	i++;
		}
        assert missions.size() == EnumMissionCard.values().length : "Il numero di missioni dovrebbe corrispondere al numero di enum";
    }
}
