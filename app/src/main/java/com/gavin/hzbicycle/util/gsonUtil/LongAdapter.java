package com.gavin.hzbicycle.util.gsonUtil;

import android.text.TextUtils;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

/**
 * User: Gavin
 * E-mail: GavinChangCN@163.com
 * Desc:
 * Date: 2016-07-21
 * Time: 11:10
 */
public class LongAdapter implements JsonSerializer<Long>, JsonDeserializer<Long> {
    protected static final String TAG = "LongAdapter";

    @Override
    public Long deserialize(JsonElement json, Type typeOfT,
                            JsonDeserializationContext context) throws JsonParseException {
        if (json == null || TextUtils.isEmpty(json.getAsString())) {
            return 0L;
        } else {
            try {
                return json.getAsLong();
            } catch (Exception e) {
                return 0L;
            }
        }
    }

    @Override
    public JsonElement serialize(Long src, Type typeOfSrc, JsonSerializationContext context) {
        Long value = 0L;
        if (src != null) {
            value = src;
        }
        return new JsonPrimitive(value);
    }
}
