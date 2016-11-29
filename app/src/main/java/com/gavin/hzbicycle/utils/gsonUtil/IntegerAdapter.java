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
 * Time: 11:09
 */
public class IntegerAdapter implements JsonSerializer<Integer>, JsonDeserializer<Integer> {
    protected static final String TAG = "IntegerAdapter";

    @Override
    public Integer deserialize(JsonElement json, Type typeOfT,
                               JsonDeserializationContext context) throws JsonParseException {
        if (json == null || TextUtils.isEmpty(json.getAsString())) {
            return 0;
        } else {
            try {
                return json.getAsInt();
            } catch (Exception e) {
                return 0;
            }
        }
    }

    @Override
    public JsonElement serialize(Integer src, Type typeOfSrc, JsonSerializationContext context) {
        Integer value = 0;
        if (src != null) {
            value = src;
        }
        return new JsonPrimitive(value);
    }
}