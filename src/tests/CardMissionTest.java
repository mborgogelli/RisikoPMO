package tests;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import model.versions.risikockassic.card.EnumMissionCard;

public class CardMissionTest {

    
    @Test
    public void testGetAllMissions() {
    	
      List<String> missions = EnumMissionCard.getMissionCards();
     
      assertTrue(missions.stream().allMatch(mission -> mission != null), "Tutte le missioni dovrebbero essere non nulle");
      
      assert !missions.isEmpty() : "La lista delle missioni non dovrebbe essere vuota";
      assert missions.size() == EnumMissionCard.values().length : "Il numero dimissioni dovrebbe corrispondere al numero di enum";
    
    
      assertTrue(missions.stream().allMatch(mission -> mission != null && !mission.isEmpty()),
    		  	"Tutte le missioni dovrebbero avere una descrizione valida");
    }


}

