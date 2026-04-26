package it.uniurb.pmo.controller.dto;


import it.uniurb.pmo.framework.utils.EnumColors;

/**
 * DTO per la risposta della creazione/accesso a una stanza.
 * Contiene solo le informazioni necessarie alla View.
 * Uso del pattern Builder per una costruzione flessibile.
 */
public class RoomResponseDTO {

    private final String roomId;
    private final String playerName;
    private final EnumColors color;
    private final String gameVersion;
    private final int currentPlayers;
    private final int maxPlayers;
    private final boolean isFull;

    //
    private RoomResponseDTO(Builder builder) {
        this.playerName = builder.playerName;
        this.color = builder.color;
        this.gameVersion = builder.gameVersion;
        this.currentPlayers = builder.currentPlayers;
        this.maxPlayers = builder.maxPlayers;
        this.isFull = builder.isFull;
        this.roomId = builder.roomId;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getRoomId() {
        return roomId;
    }

    public String getPlayers() {
        return this.playerName;
    }

    public EnumColors getColor() {
        return this.color;
    }

    public String getGameVersion() {
        return gameVersion;
    }

    public int getCurrentPlayers() {
        return currentPlayers;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public boolean isFull() {
        return isFull;
    }

    /**
     * Builder per RoomResponseDTO.
     * Permette una costruzione flessibile e leggibile dell'oggetto.
     */
    public static class Builder {
        private String roomId;
        private String playerName;
        private EnumColors color;
        private String gameVersion;
        private int currentPlayers;
        private int maxPlayers;
        private boolean isFull;

        public Builder roomId(String roomId){
            this.roomId = roomId;
            return this;
        }

        public Builder players(String playerName) {
            this.playerName = playerName;
            return this;
        }

        public Builder color(EnumColors color) {
            this.color = color;
            return this;
        }

        public Builder gameVersion(String gameVersion) {
            this.gameVersion = gameVersion;
            return this;
        }

        public Builder currentPlayers(int currentPlayers) {
            this.currentPlayers = currentPlayers;
            return this;
        }

        public Builder maxPlayers(int maxPlayers) {
            this.maxPlayers = maxPlayers;
            return this;
        }

        public Builder isFull(boolean isFull) {
            this.isFull = isFull;
            return this;
        }

        public RoomResponseDTO build() {
            return new RoomResponseDTO(this);
        }
    }
}
