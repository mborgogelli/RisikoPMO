package it.uniurb.pmo.model.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import java.util.List;
import java.util.stream.Collectors;

public final class RiskJsonParser {

    private RiskJsonParser(){}

    /**
     * Converte una lista di JsonElement in una lista di tipo T.
     *
     * @param list La lista di JsonElement da convertire
     * @param myClass La classe di tipo T in cui convertire gli elementi
     * @return Una lista di tipo T
     * @throws IllegalArgumentException se gli elementi non possono essere convertiti al tipo specificato
     */
    public static <T> List<T> convertJsonPrimitiveList(List<JsonElement> list, Class<T> myClass) {
        List<T> result;
        JsonPrimitive elem = list.getFirst().getAsJsonPrimitive();
        if (myClass == String.class && elem.isString()) {
            result = list.stream().map(e -> myClass.cast(e.getAsString())).collect(Collectors.toList());
        } else if (myClass == Integer.class && elem.isNumber()) {
            result = list.stream().map(e -> myClass.cast(e.getAsInt())).collect(Collectors.toList());
        } else if (myClass == Double.class && elem.isNumber()) {
            result = list.stream().map(e -> myClass.cast(e.getAsDouble())).collect(Collectors.toList());
        } else if (myClass == Boolean.class) {
            result = list.stream().map(e -> myClass.cast(e.getAsBoolean())).collect(Collectors.toList());
        } else if (myClass == JsonPrimitive.class) {
            result = list.stream().map(e -> myClass.cast(e.getAsJsonPrimitive())).collect(Collectors.toList());
        } else {
            throw new IllegalArgumentException("Elements cannot be cast to " + myClass.getSimpleName());
        }
        return result;
    }
}
