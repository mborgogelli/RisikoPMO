package it.uniurb.pmo.controller.game.dto;

import it.uniurb.pmo.framework.utils.EnumColors;

import java.util.List;
import java.util.Map;

public class CurrentStateDTO {
    private final String currentPlayerName;
    private final EnumColors currentPlayerColor;
    private final String nextPlayerName;
    private final EnumColors nextPlayerColor;
    private final Map<EnumColors, String> players;
    private final Map<String, Map<String, Integer>> deployedTokensByTerritory;
    private final Map<String, Integer> availableTokens;
    private final Map<String, List<String>> playerTerritories;
    private final Integer currentPhaseId;
    private final Integer currentStepId;
    private final Integer nextPhaseId;
    private final Integer nextStepId;

    private CurrentStateDTO(Builder builder) {
        this.currentPlayerName = builder.currentPlayerName;
        this.currentPlayerColor = builder.currentPlayerColor;
        this.nextPlayerName = builder.nextPlayerName;
        this.nextPlayerColor = builder.nextPlayerColor;
        this.players = builder.players;
        this.deployedTokensByTerritory = builder.deployedTokensByTerritory;
        this.availableTokens = builder.availableTokens;
        this.playerTerritories = builder.playerTerritories;
        this.currentPhaseId = builder.currentPhaseId;
        this.currentStepId = builder.currentStepId;
        this.nextPhaseId = builder.nextPhaseId;
        this.nextStepId = builder.nextStepId;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getCurrentPlayerName() {
        return currentPlayerName;
    }

    public EnumColors getCurrentPlayerColor() {
        return currentPlayerColor;
    }

    public String getNextPlayerName() {
        return nextPlayerName;
    }

    public EnumColors getNextPlayerColor() {
        return nextPlayerColor;
    }

    public Map<EnumColors, String> getPlayers() {
        return players;
    }

    public Map<String, Map<String, Integer>> getDeployedTokensByTerritory() {
        return deployedTokensByTerritory;
    }

    public Map<String, Integer> getAvailableTokens() {
        return availableTokens;
    }

    public Map<String, List<String>> getPlayerTerritories() {
        return playerTerritories;
    }

    public Integer getCurrentPhaseId() {
        return currentPhaseId;
    }

    public Integer getCurrentStepId() {
        return currentStepId;
    }

    public Integer getNextPhaseId() {
        return nextPhaseId;
    }

    public Integer getNextStepId() {
        return nextStepId;
    }

    public static class Builder {

        private String currentPlayerName;
        private EnumColors currentPlayerColor;
        private String nextPlayerName;
        private EnumColors nextPlayerColor;
        private Map<EnumColors, String> players;
        private Map<String, Map<String, Integer>> deployedTokensByTerritory;
        private Map<String, Integer> availableTokens;
        private Map<String, List<String>> playerTerritories;
        private Integer currentPhaseId;
        private Integer currentStepId;
        private Integer nextPhaseId;
        private Integer nextStepId;

        public Builder currentPlayerName(String currentPlayerName) {
            this.currentPlayerName = currentPlayerName;
            return this;
        }

        public Builder currentPlayerColor(EnumColors currentPlayerColor) {
            this.currentPlayerColor = currentPlayerColor;
            return this;
        }

        public Builder nextPlayerName(String nextPlayerName) {
            this.nextPlayerName = nextPlayerName;
            return this;
        }

        public Builder nextPlayerColor(EnumColors nextPlayerColor) {
            this.nextPlayerColor = nextPlayerColor;
            return this;
        }

        public Builder players(Map<EnumColors, String> players) {
            this.players = players;
            return this;
        }

        public Builder deployedTokensByTerritory(Map<String, Map<String, Integer>> deployedTokensByTerritory) {
            this.deployedTokensByTerritory = deployedTokensByTerritory;
            return this;
        }

        public Builder availableTokens(Map<String, Integer> availableTokens) {
            this.availableTokens = availableTokens;
            return this;
        }

        public Builder playerTerritories(Map<String, List<String>> playerTerritories) {
            this.playerTerritories = playerTerritories;
            return this;
        }

        public Builder currentPhaseId(Integer currentPhaseId) {
            this.currentPhaseId = currentPhaseId;
            return this;
        }

        public Builder currentStepId(Integer currentStepId) {
            this.currentStepId = currentStepId;
            return this;
        }

        public Builder nextPhaseId(Integer nextPhaseId) {
            this.nextPhaseId = nextPhaseId;
            return this;
        }

        public Builder nextStepId(Integer nextStepId) {
            this.nextStepId = nextStepId;
            return this;
        }

        public CurrentStateDTO build() {
            return new CurrentStateDTO(this);
        }
    }
}
