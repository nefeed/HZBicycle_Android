package com.gavin.hzbicycle.utils.gsonUtil;

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
 * Time: 11:03
 */
public class DoubleAdapter implements JsonSerializer<Double>, JsonDeserializer<Double> {
    protected static final String TAG = "DoubleAdapter";

    @Override
    public Double deserialize(JsonElement json, Type typeOfT,
                              JsonDeserializationContext context) throws JsonParseException {
        if (json == null || TextUtils.isEmpty(json.getAsString())) {
            return 0.00;
        } else {
            try {
                return json.getAsDouble();
            } catch (Exception e) {
                return 0.00;
            }
        }
    }

    @Override
    public JsonElement serialize(Double src, Type typeOfSrc, JsonSerializationContext context) {
        Double value = 0.00;
        if (src != null) {
            value = src;
        }
        return new JsonPrimitive(value);
    }
}
