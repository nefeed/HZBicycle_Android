package com.gavin.hzbicycle.ui.main

import com.gavin.hzbicycle.data.source.BicycleRepository
import com.gavin.hzbicycle.util.LogUtil
import com.gavin.hzbicycle.util.gsonUtil.GsonUtil
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription

/**
 * User: Gavin
 * E-mail: GavinChangCN@163.com
 * Desc:
 * Date: 2016-12-11
 * Time: 18:36
 */
class MainPresenter(val mView : MainContract.View) : MainContract.Presenter {

    val mSubscription: CompositeSubscription by lazy { CompositeSubscription() }
    init {
//        registerQueryOrderResultEvent()
//        loginAuto()
    }

    override fun loadNearbyBicycleStationData(lat: Double, lng: Double, len: Int) {
        BicycleRepository.INSTANCE.loadNearbyStationData(lat, lng, len)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe( { data ->
                    LogUtil.i("获取的自行车站点信息为${GsonUtil.INSTANCE.get().toJson(data)}" )
                    mView.updateNearbyBicycleStationData(data) })

    }

    override fun unSubscribe() {
        mSubscription.clear()
    }

}