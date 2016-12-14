package com.gavin.hzbicycle.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.text.DecimalFormat;

/**
 * User: Gavin
 * E-mail: GavinChangCN@163.com
 * Desc:
 * Date: 2016-12-13
 * Time: 20:58
 */
public class Util {
    protected static final String TAG = "Util";

    public static String getAppVersionName(Context ctx) {
        PackageManager _pm = ctx.getPackageManager();
        try {
            PackageInfo _pi = _pm.getPackageInfo(ctx.getPackageName(), 0);
            return _pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static int getAppVersionCode(Context ctx) {
        PackageManager _pm = ctx.getPackageManager();
        try {
            PackageInfo _pi = _pm.getPackageInfo(ctx.getPackageName(), 0);
            return _pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 按照英语系数字计数法三位数字一个分隔符进行数字规范，浮点类型
     *
     * @param value
     * @return
     */
    public static String formatEnglishDouble(Double value) {
        if (value == null) {
            return "0.00";
        }
        DecimalFormat _df = new DecimalFormat("###,##0.00");
        String _fv = _df.format(value);
        return _fv.equals(".00") ? "0.00" : _fv;
    }

    /**
     * 按照英语系数字计数法三位数字一个分隔符进行数字规范，整形类型
     *
     * @param value
     * @return
     */
    public static String formatEnglishInt(int value) {
        DecimalFormat _df = new DecimalFormat("###,##0");
        String _fv = _df.format(value);
        return _fv.equals("") ? "0" : _fv;
    }
}
