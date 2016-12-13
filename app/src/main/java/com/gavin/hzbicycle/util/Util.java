package com.gavin.hzbicycle.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

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
}
