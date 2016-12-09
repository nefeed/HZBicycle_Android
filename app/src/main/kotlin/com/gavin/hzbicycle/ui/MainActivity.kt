package com.gavin.hzbicycle.ui

import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import com.amap.api.maps2d.AMap
import com.amap.api.maps2d.LocationSource
import com.amap.api.maps2d.UiSettings
import com.gavin.hzbicycle.R
import com.gavin.hzbicycle.base.BaseActivity
import com.gavin.hzbicycle.util.LogUtil
import com.gavin.hzbicycle.util.gsonUtil.GsonUtil
import com.gavin.hzbicycle.widget.button.NoDoubleClickListener
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.onClick


/**
 * User: Gavin
 * E-mail: GavinChangCN@163.com
 * Desc:
 * Date: 2016-11-29
 * Time: 00:28
 */
class MainActivity : BaseActivity() {

    val mAMap: AMap by lazy { map.map }
    val mUiSettings: UiSettings by lazy { mAMap.uiSettings }//定义一个UiSettings对象
    //声明AMapLocationClient类对象并初始化
    val mLocationClient: AMapLocationClient by lazy { AMapLocationClient(applicationContext) }
    //声明AMapLocationClientOption对象并初始化
    val mLocationOption: AMapLocationClientOption by lazy {AMapLocationClientOption()}

    // 自定义的 View 防双击点击事件
    val mClickListener : CustomClickListener by lazy { CustomClickListener() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        checkPermission()
        initAMap(savedInstanceState)
        // Example of a call to a native method
        val tv = findViewById(R.id.sample_text) as TextView
        tv.setOnClickListener(mClickListener)
//        tv.text = stringFromJNI()
        ibSetting.setOnClickListener(mClickListener)
    }

    /**
     * 初始化高德地图模块
     */
    fun initAMap(savedInstanceState: Bundle?) {
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，实现地图生命周期管理
        map.onCreate(savedInstanceState)
        mAMap.mapType = AMap.MAP_TYPE_NORMAL
        //设置定位回调监听
        mLocationClient.setLocationListener(CustomLocationListener())
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.locationMode = AMapLocationClientOption.AMapLocationMode.Hight_Accuracy
        //获取一次定位结果：
        //该方法默认为false。
        mLocationOption.isOnceLocation = true
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.isNeedAddress = true
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption)

        mUiSettings.isZoomControlsEnabled = false // 不显示放大缩小按钮

        mAMap.setLocationSource(InnerLocationSource())
        mUiSettings.isMyLocationButtonEnabled = false // 不显示高德自带的定位按钮
        ibLocation.onClick { mLocationClient.startLocation() } // 触发高德的定位
        mUiSettings.isScaleControlsEnabled = true //显示比例尺控件

        mAMap.isMyLocationEnabled = true // 进入时默认触发高德定位

    }

    override fun onResume() {
        super.onResume()
        //在activity执行onResume时执行mMapView.onResume ()，实现地图生命周期管理
        map.onResume()
        //启动定位
        mLocationClient.startLocation()
    }

    override fun onPause() {
        super.onPause()
        mLocationClient.stopLocation()//停止定位后，本地定位服务并不会被销毁
        //在activity执行onPause时执行mMapView.onPause ()，实现地图生命周期管理
        map.onPause()
    }

    override fun onDestroy() {
        mLocationClient.onDestroy();//销毁定位客户端，同时销毁本地定位服务。
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        map.onDestroy()
        super.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，实现地图生命周期管理
        map.onSaveInstanceState(outState)
    }

    inner class CustomClickListener : NoDoubleClickListener() {
        override fun onNoDoubleClick(v: View?) {
            when (v?.id) {
                R.id.ibSetting -> {
                    if (v != null) {
                        AboutActivity.startActivity(this@MainActivity, v)
                    }
                }
                R.id.sample_text -> {
                    if (v != null) {
                        HtmlActivity.startActivity(this@MainActivity, v, "https://github.com/GavinChangCN")
                    }
                }
            }
        }
    }

//    /**
//     * A native method that is implemented by the 'native-lib' native library,
//     * which is packaged with this application.
//     */
//    external fun stringFromJNI(): String
//
//    companion object {
//
//        // Used to load the 'native-lib' library on application startup.
//        init {
//            System.loadLibrary("native-lib")
//        }
//    }

    /**
     * 自带高德地图定位监听器
     */
    inner class InnerLocationSource : LocationSource {
        override fun deactivate() {
        }

        override fun activate(listener: LocationSource.OnLocationChangedListener?) {
            LogUtil.i("高德自带定位工作了！")
        }

    }
    /**
     * 自定义高德地图定位监听器
     */
    inner class CustomLocationListener: AMapLocationListener {
        override fun onLocationChanged(amapLocation: AMapLocation?) {
            if (amapLocation != null) {
                LogUtil.i("自定义高德定位工作了！")
                LogUtil.i("获取高德定位信息回调：${GsonUtil.INSTANCE.get().toJson(amapLocation)}")
                if (amapLocation.errorCode == 0) {
                //可在其中解析amapLocation获取相应内容。
                }else {
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                    LogUtil.e("location Error, ErrCode:${amapLocation.errorCode}, errInfo:${amapLocation.errorInfo}")
                }
            }
        }

    }
}