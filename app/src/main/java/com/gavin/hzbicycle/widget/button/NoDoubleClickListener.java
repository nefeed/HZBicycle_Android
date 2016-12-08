package com.gavin.hzbicycle.widget.button;

import android.os.SystemClock;
import android.view.View;

/**
 * User: Gavin
 * E-mail: GavinChangCN@163.com
 * Desc: 解决"帕金森"式连续点击
 * Date: 2016-12-01
 * Time: 17:26
 */
public abstract class NoDoubleClickListener implements View.OnClickListener {
    protected static final String TAG = "NoDoubleClickListener";

    private static final int MIN_CLICK_DELAY_TIME = 1000;
    private long lastClickTime = 0;

    public abstract void onNoDoubleClick(View v);

    @Override
    public void onClick(View v) {
        long currentTime = SystemClock.uptimeMillis();
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime;
            onNoDoubleClick(v);
        }
    }
}
