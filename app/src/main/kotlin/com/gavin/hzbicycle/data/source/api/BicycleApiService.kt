package com.gavin.hzbicycle.data.source.api

import com.gavin.hzbicycle.data.bean.BicycleResponseBean
import retrofit2.http.GET
import retrofit2.http.Query
import rx.Observable

/**
 * User: Gavin
 * E-mail: GavinChangCN@163.com
 * Desc:
 * Date: 2016-12-11
 * Time: 17:38
 */
interface BicycleApiService {
    @GET("/wz/np_getBikesByWeiXin.do")
    fun loadNearbyStationData(@Query("lat") lat:Double, @Query("lng") lng: Double, @Query("len") len: Int) : Observable<BicycleResponseBean>
}