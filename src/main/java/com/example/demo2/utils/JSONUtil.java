package com.example.demo2.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.springframework.util.StringUtils;

import java.util.Optional;

public class JSONUtil {
    public static Optional<String> JsonObjectGetStringItem(JsonObject jsObject, String field) {
        JsonElement value = jsObject.get(field);
        if (value!=null && StringUtils.hasText(value.getAsString())) {
            return Optional.of(value.getAsString());
        }

        return Optional.empty();
    }
    public static Optional<Integer> JsonObjectGetIntItem(JsonObject jsObject, String field) {
        JsonElement value = jsObject.get(field);
        if (value!=null && StringUtils.hasText(value.getAsString())) {
            return Optional.of(value.getAsInt());
        }

        return Optional.empty();
    }
}
