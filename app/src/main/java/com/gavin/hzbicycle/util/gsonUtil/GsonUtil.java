package com.gavin.hzbicycle.util.gsonUtil;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.math.BigDecimal;

/**
 * User: Gavin
 * E-mail: GavinChangCN@163.com
 * Desc:
 * Date: 2016-07-21
 * Time: 11:12
 */
public enum GsonUtil {
    INSTANCE;

    private Gson mGson;

    public Gson get() {
        if (mGson == null) {
            mGson = createGson();
        }
        return mGson;
    }

    private Gson createGson() {
        GsonBuilder _gsonBuilder = new GsonBuilder();
        _gsonBuilder.registerTypeAdapter(int.class, new IntegerAdapter());
        _gsonBuilder.registerTypeAdapter(Integer.class, new IntegerAdapter());
        _gsonBuilder.registerTypeAdapter(long.class, new LongAdapter());
        _gsonBuilder.registerTypeAdapter(Long.class, new LongAdapter());
        _gsonBuilder.registerTypeAdapter(float.class, new FloatAdapter());
        _gsonBuilder.registerTypeAdapter(Float.class, new FloatAdapter());
        _gsonBuilder.registerTypeAdapter(double.class, new DoubleAdapter());
        _gsonBuilder.registerTypeAdapter(Double.class, new DoubleAdapter());
        _gsonBuilder.registerTypeAdapter(BigDecimal.class, new BigDecimalAdapter());
        return _gsonBuilder.create();
    }

}
