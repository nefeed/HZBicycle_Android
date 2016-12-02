package com.gavin.hzbicycle.util;

import com.gavin.hzbicycle.BuildConfig;
import com.orhanobut.logger.Logger;

/**
 * 日志工具类
 */
public class LogUtil {
	private static boolean sDebug = BuildConfig.DEBUG;

	public static void i(String msg) {
		if (sDebug)
			Logger.i(msg);
	}

	public static void i(String tag, String msg) {
		if (sDebug)
			Logger.i(tag, msg);
	}

	public static void d(String msg) {
		if (sDebug)
			Logger.i(msg);
	}

	public static void d(String tag, String msg) {
		if (sDebug)
			Logger.d(tag, msg);
	}

	public static void w(String msg) {
		if (sDebug)
			Logger.w(msg);
	}

	public static void w(String tag, String msg) {
		if (sDebug)
			Logger.w(tag, msg);
	}

	public static void e(String msg) {
		if (sDebug)
			Logger.e(msg);
	}

	public static void e(String tag, String msg) {
		if (sDebug)
			Logger.e(tag, msg);
	}

}
