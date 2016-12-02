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
 * Time: 11:07
 */
public class FloatAdapter implements JsonSerializer<Float>, JsonDeserializer<Float> {
    protected static final String TAG = "FloatAdapter";

    @Override
    public Float deserialize(JsonElement json, Type typeOfT,
                             JsonDeserializationContext context) throws JsonParseException {
        if (json == null || TextUtils.isEmpty(json.getAsString())) {
            return 0.0f;
        } else {
            try {
                return json.getAsFloat();
            } catch (Exception e) {
                return 0.0f;
            }
        }
    }

    @Override
    public JsonElement serialize(Float src, Type typeOfSrc, JsonSerializationContext context) {
        Float value = 0.0f;
        if (src != null) {
            value = src;
        }
        return new JsonPrimitive(value);
    }
}
