package com.gavin.hzbicycle.data.source.remote

import com.gavin.hzbicycle.base.BaseRemoteDataSource
import com.gavin.hzbicycle.data.bean.BicycleStationBean
import com.gavin.hzbicycle.data.source.api.BicycleApiService
import com.gavin.hzbicycle.http.RetrofitHolder
import rx.Observable
import java.util.*

/**
 * User: Gavin
 * E-mail: GavinChangCN@163.com
 * Desc:
 * Date: 2016-12-11
 * Time: 17:49
 */
class BicycleRemoteDataSource : BaseRemoteDataSource() {

    private val mApiService: BicycleApiService
            by lazy {
                RetrofitHolder.getCustomApiInstance().create(BicycleApiService::class.java)
            }

    companion object {

        //在访问HttpMethods时创建单例
        private object SingletonHolder {
            internal val INSTANCE = BicycleRemoteDataSource()
        }

        //获取单例
        fun getInstance(): BicycleRemoteDataSource {
            return SingletonHolder.INSTANCE
        }
    }

    fun loadNearbyStationData(lat:Double, lng: Double, len: Int) : Observable<ArrayList<BicycleStationBean>> {
        return mApiService.loadNearbyStationData(lat, lng, len)
                .map({ source -> source.data })
    }

}