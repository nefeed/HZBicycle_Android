package com.gavin.hzbicycle.data.local;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

/**
 * User: Gavin
 * E-mail: GavinChangCN@163.com
 * Desc:
 * Date: 2016-12-02
 * Time: 12:48
 */
public enum PreferenceLocalDataSource {
    INSTANCE;

    private static final String KEY_ROOT_URL = "HZBicycle.rootUrl";
    private static final String KEY_DEVICE_ID = "HZBicycle.deviceId";
    private static final String KEY_DEVICE_TOKEN = "HZBicycle.deviceToken";
    private static final String KEY_APP_SHARE_INFO = "HZBicycle.appShareInfo";
    private static final String KEY_APP_START_NUMBER = "HZBicycle.appStartNumber";
    private static final String KEY_MAIN_LAUNCHED = "HZBicycle.mainLaunched";

    private SharedPreferences mSharedPreferences;


    public void buildPreferenceHelper(Context context) {
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        if (getDeivceId() == null) {
            setDeviceId(context);
        }
    }

    public void clear() {
        mSharedPreferences.edit().clear().apply();
    }

    /**
     * 设置服务器地址
     */
    public void setRootUrl(String url) {
        mSharedPreferences.edit().putString(KEY_ROOT_URL, url).apply();
    }

    /**
     * 获取服务器地址
     */
    public String getRootUrl() {
        return mSharedPreferences.getString(KEY_ROOT_URL, null);
    }

    /**
     * 获取device id
     */
    public String getDeivceId() {
        return mSharedPreferences.getString(KEY_DEVICE_ID, null);
    }

    /**
     * 设置设备device id
     *
     * @param ctx
     */
    private void setDeviceId(Context ctx) {
        String _androidId = Settings.Secure.getString(ctx.getContentResolver(), Settings.Secure.ANDROID_ID);
        UUID _uuid;
        try {
            if (!"9774d56d682e549c".equals(_androidId)) {
                _uuid = UUID.nameUUIDFromBytes(_androidId.getBytes("utf8"));
            } else {
                final String deviceId = ((TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
                _uuid = deviceId != null ? UUID.nameUUIDFromBytes(deviceId.getBytes("utf8")) : UUID.randomUUID();
            }
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        mSharedPreferences.edit().putString(KEY_DEVICE_ID, _uuid.toString()).apply();
    }


    /**
     * 获取device token
     */
    public String getDeivceToken() {
        return mSharedPreferences.getString(KEY_DEVICE_TOKEN, null);
    }

    /**
     * 保存device token
     */
    public void setDeviceToken(String deviceToken) {
        mSharedPreferences.edit().putString(KEY_DEVICE_TOKEN, deviceToken).apply();
    }

    public void saveMainActivityLaunched(boolean launched) {
        SharedPreferences.Editor _editor = mSharedPreferences.edit();
        _editor.putBoolean(KEY_MAIN_LAUNCHED, launched);
        _editor.commit();
    }

    public boolean isMainActivityLaunched() {
        return mSharedPreferences.getBoolean(KEY_MAIN_LAUNCHED, false);
    }

    public int appStartNumber() {
        return mSharedPreferences.getInt(KEY_APP_START_NUMBER, 0);
    }

    public boolean appStartNumber(int number) {
        return mSharedPreferences.edit().putInt(KEY_APP_START_NUMBER, number).commit();
    }
}
