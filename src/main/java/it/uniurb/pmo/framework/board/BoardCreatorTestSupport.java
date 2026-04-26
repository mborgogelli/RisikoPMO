package it.uniurb.pmo.framework.board;

import com.google.gson.JsonObject;

import it.uniurb.pmo.framework.utils.GameVersion;

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

