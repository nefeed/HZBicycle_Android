package com.gavin.hzbicycle.http;

import android.text.TextUtils;

import com.gavin.hzbicycle.BuildConfig;
import com.gavin.hzbicycle.data.PreferenceRepository;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * User: Gavin
 * E-mail: GavinChangCN@163.com
 * Desc:
 * Date: 2016-11-30
 * Time: 18:54
 */
public class RetrofitHolder {
    protected static final String TAG = "RetrofitHolder";
    private Retrofit mRetrofit;

    private RetrofitHolder(boolean isBaseApi) {
        Retrofit.Builder _builder = new Retrofit.Builder();
        String _url;
        if (isBaseApi) {
            _url = BuildConfig.BASE_API_URL;
            if (BuildConfig.SWITCH_SERVER_URL && !TextUtils.isEmpty(PreferenceRepository.INSTANCE.getRootUrl())) {
                _url = PreferenceRepository.INSTANCE.getRootUrl();
            }
        } else {
            _url =  "http://c.ggzxc.com.cn/";
        }
        mRetrofit = _builder
                .baseUrl(_url)//注意此处,设置服务器的地址
                .addConverterFactory(GsonConverterFactory.create()) // 用于Json数据的转换
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()) // 用于返回Rxjava调用,同步
//                .addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io()))//异步
                .client(getClient())
                .build();
    }

    public OkHttpClient getClient() {
        OkHttpClient.Builder _builder = new OkHttpClient.Builder();
        return _builder.build();
    }

    public <T> T create(final Class<T> cls) {
        return mRetrofit.create(cls);
    }


    // 在访问BaseApiHttpMethods时创建单例
    private static class BaseApiSingletonHolder {
        private static final RetrofitHolder INSTANCE = new RetrofitHolder(true);
    }

    // 在访问CustomApiHttpMethods时创建单例
    private static class CustomApiSingletonHolder {
        private static final RetrofitHolder INSTANCE = new RetrofitHolder(false);
    }

    // 获取BaseApi单例
    public static RetrofitHolder getBaseApiInstance() {
        return BaseApiSingletonHolder.INSTANCE;
    }

    // 获取 CustomApi 单例
    public static RetrofitHolder getCustomApiInstance() {
        return CustomApiSingletonHolder.INSTANCE;
    }
}
