package it.uniurb.pmo.model.board;

import java.util.List;

import com.google.gson.JsonElement;
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

    @Override
    public List<JsonElement> getValues(String rootKey, JsonElement jsonMap) {
        return super.getValues(rootKey, jsonMap);
    }

    @Override
    public List<JsonElement> getValues(String rootKey, List<JsonElement> jsonMap) {
        return super.getValues(rootKey, jsonMap);
    }

    @Override
    public <T> List<T> getValues(String rootKey, List<JsonElement> jsonMap, Class<T> myClass) {
        return super.getValues(rootKey, jsonMap, myClass);
    }
}