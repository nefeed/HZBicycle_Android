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
import java.math.BigDecimal;

/**
 * User: Gavin
 * E-mail: GavinChangCN@163.com
 * Desc:
 * Date: 2016-07-21
 * Time: 11:03
 */
public class BigDecimalAdapter implements JsonSerializer<BigDecimal>, JsonDeserializer<BigDecimal> {
    protected static final String TAG = "BigDecimalAdapter";

    @Override
    public BigDecimal deserialize(JsonElement json, Type typeOfT,
                                  JsonDeserializationContext context) throws JsonParseException {
        if (json == null || TextUtils.isEmpty(json.getAsString())) {
            return BigDecimal.valueOf(0.00);
        } else {
            try {
                return json.getAsBigDecimal();
            } catch (Exception e) {
                return BigDecimal.valueOf(0.00);
            }
        }
    }

    @Override
    public JsonElement serialize(BigDecimal src, Type typeOfSrc, JsonSerializationContext context) {
        BigDecimal value = BigDecimal.valueOf(0.00);
        if (src != null) {
            value = src;
        }
        return new JsonPrimitive(value);
    }
}
