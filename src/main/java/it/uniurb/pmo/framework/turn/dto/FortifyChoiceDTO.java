package it.uniurb.pmo.framework.turn.dto;

public class FortifyChoiceDTO {

    private final String fromZone;
    private final String toZone;
    private final int numberOfTanks;

    public FortifyChoiceDTO(String fromZone, String toZone, int numberOfTanks) {
        this.fromZone = fromZone;
        this.toZone = toZone;
        this.numberOfTanks = numberOfTanks;
    }

    public String getFromZone() {
        return this.fromZone;
    }

    public String getToZone() {
        return this.toZone;
    }

    public int getNumberOfTanks() {
        return this.numberOfTanks;
    }
}
