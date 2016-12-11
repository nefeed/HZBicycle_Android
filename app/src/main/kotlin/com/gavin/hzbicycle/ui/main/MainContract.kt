package com.gavin.hzbicycle.ui.main

import com.gavin.hzbicycle.data.bean.BasePresenter
import com.gavin.hzbicycle.data.bean.BaseView
import com.gavin.hzbicycle.data.bean.BicycleStationBean
import java.util.*

/**
 * User: Gavin
 * E-mail: GavinChangCN@163.com
 * Desc:
 * Date: 2016-12-11
 * Time: 18:22
 */
interface MainContract {
    interface View : BaseView {
        fun updateNearbyBicycleStationData(data: ArrayList<BicycleStationBean>)
    }
    interface Presenter : BasePresenter {
        fun loadNearbyBicycleStationData(lat:Double, lng: Double, len: Int = 800) // 默认范围为800米
    }
}