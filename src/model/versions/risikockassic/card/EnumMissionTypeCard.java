package model.versions.risikockassic.card;
import model.card.ISymbolCard;
import model.utils.EnumColors;
import java.util.ArrayList;
import java.util.List;

public enum EnumMissionTypeCard implements ISymbolCard {
		CONQUER,
	    DESTROY;
	    
	@Override
	public ISymbolCard getSymbol() {
		return this;
	}
	    
//	private final String description;
//
//    EnumMissionTypeCard(String description) {
//        this.description = description;
//    }
//
//    /**
//     * Restituisce la descrizione della carta Obiettivo.
//     * 
//     * @return Descrizione della carta Obiettivo
//     */
//    public String  getDescription() {
//        return description;
//    }
//	    
//	/**
//	 * Restituisce la lista delle carte Obiettivo disponibili.
//	 * 
//	 * @return Lista di descrizioni delle carte Obiettivo
//	 */
//
//	    public static List<String> getMissionCards() {
//	        List<String> missions = new ArrayList<>();
//	        // Aggiungi le missioni di conquista
//	        for (EnumMissionTypeCard mission : EnumMissionTypeCard.values()) {
//	            if (!mission.name().startsWith("DESTROY_")) {
//	                missions.add(mission.getDescription());
//	            }
//	        }
////	        // Aggiungi le missioni di distruzione
////	        missions.addAll(getDestroyMission());
//	        return missions;
//	    }
//	    
//	private static List<EnumMissionTypeCard> getConqMissionCard() {
//		return List.of(EnumMissionTypeCard.values());
//	}
//	    
//	private static List<String> getDestroyMissionCards() {
//		return java.util.Arrays.stream(EnumColors.values())
//				.map(color -> "Distruggere completamente l’armata " + color.name()).toList();
//	}
}

