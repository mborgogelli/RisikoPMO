package it.uniurb.pmo.controller.dto;


import it.uniurb.pmo.model.utils.EnumColors;

/**
 * DTO per la risposta della creazione/accesso a una stanza.
 * Contiene solo le informazioni necessarie alla View.
 * Uso del pattern Builder per una costruzione flessibile.
 */
public class RoomResponseDTO {

    private String playerName;
    private EnumColors assignedColor;
    private String gameVersion;
    private int currentPlayers;
    private int maxPlayers;
    private boolean isFull;

    private RoomResponseDTO(Builder builder) {
        this.playerName = builder.playerName;
        this.assignedColor = builder.assignedColor;
        this.gameVersion = builder.gameVersion;
        this.currentPlayers = builder.currentPlayers;
        this.maxPlayers = builder.maxPlayers;
        this.isFull = builder.isFull;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public EnumColors getAssignedColor() {
        return assignedColor;
    }

    public void setAssignedColor(EnumColors assignedColor) {
        this.assignedColor = assignedColor;
    }

    public String getGameVersion() {
        return gameVersion;
    }

    public void setGameVersion(String gameVersion) {
        this.gameVersion = gameVersion;
    }

    public int getCurrentPlayers() {
        return currentPlayers;
    }

    public void setCurrentPlayers(int currentPlayers) {
        this.currentPlayers = currentPlayers;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public boolean isFull() {
        return isFull;
    }

    /**
     * Builder per RoomResponseDTO.
     * Permette una costruzione flessibile e leggibile dell'oggetto.
     */
    public static class Builder {
        private String playerName;
        private EnumColors assignedColor;
        private String gameVersion;
        private int currentPlayers;
        private int maxPlayers;
        private boolean isFull;

        public Builder playerName(String playerName) {
            this.playerName = playerName;
            return this;
        }

        public Builder assignedColor(EnumColors assignedColor) {
            this.assignedColor = assignedColor;
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
