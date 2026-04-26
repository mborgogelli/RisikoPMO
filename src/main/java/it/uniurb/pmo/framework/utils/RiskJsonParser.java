package it.uniurb.pmo.framework.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public final class RiskJsonParser {

    private RiskJsonParser(){}

    public static List<JsonElement> getValues(String rootKey, JsonElement jsonMap){
        if (jsonMap == null || jsonMap.isJsonNull() || jsonMap.isJsonPrimitive()) {
            throw new IllegalArgumentException("Parameter is null or JsonNull or JsonPrimitive");
        }
        List<JsonElement> list = extractValuesFromElement(rootKey, jsonMap).collect(Collectors.toList());
        checkOutputList(list);
        return list;
    }

    public static List<JsonElement> getValues(String rootKey, List<JsonElement> jsonMap){
        checkInputList(jsonMap);
        List<JsonElement> list;
        if (jsonMap.getFirst().isJsonArray() || jsonMap.getFirst().isJsonObject()) {
            list = getValueFromList(rootKey, jsonMap);
        } else {
            throw new IllegalArgumentException("Parameter must contain JsonObject or JsonArray");
        }
        checkOutputList(list);
        return list;
    }

    public static <T> List<T> getValues(String rootKey, List<JsonElement> jsonMap, Class<T> myClass) {
        List<JsonElement> elements = getValues(rootKey, jsonMap);
        if (!elements.getFirst().isJsonPrimitive()) {
            throw new IllegalArgumentException("Cannot convert elements to " + myClass.getSimpleName());
        }
        return convertJsonPrimitiveList(elements, myClass);
    }

    public static List<JsonElement> splitJsonArray(JsonArray jsonArray){
        return StreamSupport.stream(jsonArray.spliterator(), false)
                .collect(Collectors.toList());
    }

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

    private static boolean isValidKey(String key, JsonObject json) {
        if (!json.has(key) || json.get(key).isJsonNull()) {
            throw new IllegalArgumentException(key + " is not a valid key in the JSON object, or its value is null.");
        }
        return true;
    }

    private static void checkInputList(List<JsonElement> list) {
        checkOutputList(list);
        if (list.getFirst().isJsonPrimitive()) {
            throw new IllegalArgumentException("Elements of List " + list + " are JsonPrimitive. Cannot use provided key.");
        }
    }

    private static void checkOutputList(List<JsonElement> list) {
        if (list == null) {
            throw new IllegalArgumentException("List is null.");
        } else if (list.isEmpty()){
            throw new IllegalArgumentException("List is empty.");
        } else if (!checkSameType(list)){
            throw new IllegalArgumentException("Elements of List are not of the same type.");
        } else if (list.getFirst().isJsonNull()) {
            throw new IllegalArgumentException("Elements of List are JsonNull.");
        }
    }

    private static boolean checkSameType(List<JsonElement> jsonElements) {
        Class<?> firstType = jsonElements.getFirst().getClass();
        return jsonElements.stream().allMatch(e -> e.getClass().equals(firstType));
    }

    private static List<JsonElement> getValueFromList(String rootKey, List<JsonElement> jsonMap) {
        return jsonMap.stream()
                .flatMap(element -> extractValuesFromElement(rootKey, element))
                .collect(Collectors.toList());
    }

    private static Stream<JsonElement> extractValuesFromElement(String rootKey, JsonElement element) {
        if (element.isJsonObject()) {
            return getValueByKey(rootKey, element.getAsJsonObject()).stream();
        }
        if (element.isJsonArray()) {
            return getValueByKey(rootKey, element.getAsJsonArray()).stream();
        }
        return Stream.empty();
    }

    private static List<JsonElement> getValueByKey(String key, JsonArray jsonArray) {
        return StreamSupport.stream(jsonArray.spliterator(), false)
                .filter(JsonElement::isJsonObject)
                .map(JsonElement::getAsJsonObject)
                .filter(obj -> isValidKey(key, obj))
                .map(obj -> obj.get(key))
                .collect(Collectors.toList());
    }

    private static List<JsonElement> getValueByKey(String rootKey, JsonObject jsonObject){
        return Stream.ofNullable(rootKey)
                .filter(key -> isValidKey(key, jsonObject))
                .map(jsonObject::get)
                .collect(Collectors.toCollection(ArrayList::new));
    }
}
