package com.gavin.hzbicycle.data.bean

import com.gavin.hzbicycle.base.BaseBean
import java.util.*

/**
 * User: Gavin
 * E-mail: GavinChangCN@163.com
 * Desc:
 * Date: 2016-12-11
 * Time: 17:31
 */
data class BicycleResponseBean(var count:Int,
                               var data:ArrayList<BicycleStationBean>) : BaseBean