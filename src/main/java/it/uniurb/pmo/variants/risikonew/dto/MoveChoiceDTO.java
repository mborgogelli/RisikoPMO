package it.uniurb.pmo.variants.risikonew.dto;

public class MoveChoiceDTO {

    private final String fromZone;
    private final String toZone;
    private final int numberOfTanks;

    public MoveChoiceDTO(String fromZone, String toZone, int numberOfTanks) {
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
