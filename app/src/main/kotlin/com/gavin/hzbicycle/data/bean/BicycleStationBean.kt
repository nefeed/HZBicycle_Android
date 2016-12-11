package com.gavin.hzbicycle.data.bean

import com.gavin.hzbicycle.base.BaseBean

/**
 * User: Gavin
 * E-mail: GavinChangCN@163.com
 * Desc:
 * Date: 2016-12-11
 * Time: 17:26
 */
data class BicycleStationBean(var address: String,
                              var areaname: String,
                              var bikenum: Int,
                              var createTime: Long,
                              var guardType: String,
                              var id: Long,
                              var lat: Double,
                              var len: Int,
                              var lon: Double,
                              var name: String,
                              var number: String,
                              var rentcount: Int,
                              var restorecount: Int,
                              var serviceType: String,
                              var sort: Int,
                              var stationPhone: String,
                              var stationPhone2: String,
                              var useflag: String
                              ) : BaseBean
//"address": "江南大道江汉路口东北100米江一村公交站",
//"areaname": "城南",
//"bikenum": "32",
//"createTime": 1481448366684,
//"guardType": "无人值守",
//"id": 23855,
//"lat": "30.210051",
//"len": "135",
//"lon": "120.213465",
//"name": "公交江一村站南",
//"number": "6118",
//"rentcount": "23",
//"restorecount": "9",
//"serviceType": "05:30-24:00",
//"sort": 0,
//"stationPhone": "(7:00-19:00)|13606518704",
//"stationPhone2": "(19:00-24:00)|13606517487",
//"useflag": "正常"