package com.gavin.hzbicycle.data;

import android.content.Context;

import com.gavin.hzbicycle.data.local.PreferenceLocalDataSource;

/**
 * User: Gavin
 * E-mail: GavinChangCN@163.com
 * Desc:
 * Date: 2016-12-02
 * Time: 12:47
 */
public enum PreferenceRepository {
    INSTANCE;

    public void buildPreferenceHelper(Context context) {
        PreferenceLocalDataSource.INSTANCE.buildPreferenceHelper(context);
    }

    /**
     * 设置服务器地址
     */
    public void setRootUrl(String url) {
        PreferenceLocalDataSource.INSTANCE.setRootUrl(url);
    }

    /**
     * 获取服务器地址
     */
    public String getRootUrl() {
        return PreferenceLocalDataSource.INSTANCE.getRootUrl();
    }


    /**
     * 获取device id
     */
    public String getDeviceId() {
        return PreferenceLocalDataSource.INSTANCE.getDeivceId();
    }

    /**
     * 获取device token
     */
    public String getDeivceToken() {
        return PreferenceLocalDataSource.INSTANCE.getDeivceToken();
    }

    /**
     * 保存device token
     */
    public void setDeviceToken(String deviceToken) {
        PreferenceLocalDataSource.INSTANCE.setDeviceToken(deviceToken);
    }
}
