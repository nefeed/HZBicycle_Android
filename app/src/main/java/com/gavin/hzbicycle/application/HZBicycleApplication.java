package com.gavin.hzbicycle.application;

import android.app.Activity;
import android.support.multidex.MultiDexApplication;
import android.view.View;

import com.facebook.stetho.Stetho;
import com.orhanobut.logger.Logger;
import com.orm.SugarContext;
import com.umeng.analytics.MobclickAgent;

public class HZBicycleApplication extends MultiDexApplication {

    private ActivityLifecycleHelper mActivityLifecycleHelper;
    private static HZBicycleApplication sInstance;
    private boolean mIsLoginned;
    public static boolean sAppIsOpened;

    public static HZBicycleApplication getInstance() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sAppIsOpened = true;
        sInstance = this;
        Logger.init("HZBicycler");
        registerActivityLifecycleCallbacks(mActivityLifecycleHelper = new ActivityLifecycleHelper());
        SugarContext.init(this);
        initStetho();
        Foreground.init(this);
        initUmeng();
    }

    public ActivityLifecycleHelper getActivityLifecycleHelper() {
        return mActivityLifecycleHelper;
    }

    public void onSlideBack(boolean isReset, float distance) {
        if(mActivityLifecycleHelper != null) {
            Activity lastActivity = mActivityLifecycleHelper.getPreActivity();
            if(lastActivity != null) {
                View contentView = lastActivity.findViewById(android.R.id.content);
                if(isReset) {
                    contentView.setX(contentView.getLeft());
                } else {
                    final int width = getResources().getDisplayMetrics().widthPixels;
                    contentView.setX(-width / 3 + distance / 3);
                }
            }
        }
    }

    /**
     * 初始化友盟
     */
    private void initUmeng() {
        // 友盟统计
        MobclickAgent.setScenarioType(getApplicationContext(), MobclickAgent.EScenarioType.E_UM_NORMAL);
    }


    //初始化OkHttpUtils
//    private void initOkHttpUtils() {
//        CookieJarImpl _cookieJar = new CookieJarImpl(new ShireCookieStore(getApplicationContext()));
//        OkHttpClient _okHttpClient = new OkHttpClient.Builder()
//                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
//                .readTimeout(10000L, TimeUnit.MILLISECONDS)
//                .cookieJar(_cookieJar)
//                //其他配置
//                .build();
//        OkHttpUtils.initClient(_okHttpClient);
//    }

    public boolean isLoginned() {
        return mIsLoginned;
    }

    public void setIsLoginned(boolean isLoginned) {
        this.mIsLoginned = isLoginned;
    }

    private void initStetho() {
        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                        .build());
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        SugarContext.terminate();
    }

    public void clean() {
        setIsLoginned(false);
//        BaseApi.clean();
//        //登出清除cookie
//        ((CookieJarImpl) (OkHttpUtils.getInstance().getOkHttpClient().cookieJar())).getCookieStore().removeAll();
    }

}
