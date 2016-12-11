package com.gavin.hzbicycle.data.source

import com.gavin.hzbicycle.data.bean.BicycleStationBean
import com.gavin.hzbicycle.data.source.remote.BicycleRemoteDataSource
import rx.Observable
import java.util.*

/**
 * User: Gavin
 * E-mail: GavinChangCN@163.com
 * Desc:
 * Date: 2016-12-11
 * Time: 18:17
 */
enum class BicycleRepository {
    INSTANCE;

    fun loadNearbyStationData(lat:Double, lng: Double, len: Int) : Observable<ArrayList<BicycleStationBean>> {
        return BicycleRemoteDataSource.getInstance().loadNearbyStationData(lat, lng, len)
    }
}