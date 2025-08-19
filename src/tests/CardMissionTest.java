package tests;

import java.util.List;

import org.junit.jupiter.api.Test;

import model.versions.risikockassic.card.EnumMissionCard;

public class CardMissionTest {

    
    @Test
    public void testGetAllMissions() {
    	
    
//        List<String> missions = null;
//		try {
//			missions = EnumMissionCard.GetAllMissions();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//        int i = 1;
//        assert !missions.isEmpty() : "La lista delle missioni non dovrebbe essere vuota";
//        for (String string : missions) {
//        	System.out.println("Mission " + i + ": " + string);
//        	i++;
//		}
//        assert missions.size() == EnumMissionCard.values().length : "Il numero di missioni dovrebbe corrispondere al numero di enum";
//        System.out.println("\n Numero degli obiettivi : " + missions.size());

      List<EnumMissionCard> missions = EnumMissionCard.GetAllMissions();

      int i = 1;
      assert !missions.isEmpty() : "La lista delle missioni non dovrebbe essere vuota";
      for (EnumMissionCard e : missions) {
      	System.out.println("Mission " + i + ": " + e);
      	i++;
		}
      assert missions.size() == EnumMissionCard.values().length : "Il numero dimissioni dovrebbe corrispondere al numero di enum";
      System.out.println("\n Numero degli obiettivi : " + missions.size());
    }
}

