package it.uniurb.pmo.model.board;

import com.google.gson.JsonObject;

import it.uniurb.pmo.model.utils.GameVersion;

public class BoardCreatorTestSupport extends BoardCreator {

    public BoardCreatorTestSupport() {
        super(GameVersion.RISIKONEW);
    }

    @Override
    protected IGameBoard createMap() {
        return null;
    }

    @Override
    public JsonObject getLoadedMap() {
        return super.getLoadedMap();
    }
}

