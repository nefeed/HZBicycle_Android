package com.gavin.hzbicycle.utils.rx;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * User: Gavin
 * E-mail: GavinChangCN@163.com
 * Desc:
 * Date: 2016-11-30
 * Time: 17:36
 */
public class RxUtil {
    private static Observable.Transformer sIOToMainThreadSchedulerTransformer;

    static {
        sIOToMainThreadSchedulerTransformer = createIOToMainThreadScheduler();
    }

    private static <T> Observable.Transformer<T, T> createIOToMainThreadScheduler() {
        return new Observable.Transformer<T, T>() {

            @Override
            public Observable<T> call(Observable<T> tObservable) {
                return tObservable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    public static <T> Observable.Transformer<T, T> applyIOTOMainThreadSchedulers() {
        return sIOToMainThreadSchedulerTransformer;
    }

}
