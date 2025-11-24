package model.versions.risikockassic.card;

import model.card.ISymbolCard;
import model.players.Player;

public abstract class MissionCard {
	
	private final ISymbolCard symbol;
	private final String description;
	
    // Carta territorio con simbolo specifico
    MissionCard(EnumMissionCard symbol, String description ) {
		this.symbol = symbol;
		this.description = description;
	}
    
	public String getName() {
		return description;
	}
	
	public ISymbolCard getSymbol() {
		return this.symbol;
	}
	
	public abstract boolean isAchievementReached(Player player);
	

///**
// * Restituisce la lista delle carte Obiettivo disponibili.
// * 
// * @return Lista di descrizioni delle carte Obiettivo
// */
//
//    public static List<String> getMissionCards() {
//        List<String> missions = new ArrayList<>();
//        // Aggiungi le missioni di conquista
//        for (EnumMissionTypeCard mission : EnumMissionTypeCard.values()) {
//            if (!mission.name().startsWith("DESTROY_")) {
//                missions.add(mission.getDescription());
//            }
//        }
////        // Aggiungi le missioni di distruzione
////        missions.addAll(getDestroyMission());
//        return missions;
//    }
//    
//private static List<EnumMissionTypeCard> getMissionTypeCard() {
//	return List.of(EnumMissionTypeCard.values());
//}
//    
//private static List<String> getDestroyMissionCards(Player player) {
////	return java.util.Arrays.stream(player.getColor().values())
////			.map(color -> "Distruggere completamente l’armata " + color.name()).toList();
//}
//	
	
	
}
