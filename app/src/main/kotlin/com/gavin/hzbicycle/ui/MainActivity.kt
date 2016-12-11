package com.gavin.hzbicycle.ui

import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import com.amap.api.maps.*
import com.amap.api.maps.model.LatLng
import com.gavin.hzbicycle.R
import com.gavin.hzbicycle.base.BaseActivity
import com.gavin.hzbicycle.util.LogUtil
import com.gavin.hzbicycle.widget.button.NoDoubleClickListener
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.onClick
import java.text.SimpleDateFormat
import java.util.*






/**
 * User: Gavin
 * E-mail: GavinChangCN@163.com
 * Desc:
 * Date: 2016-11-29
 * Time: 00:28
 */
class MainActivity : BaseActivity() {

    val mMapView: MapView by lazy { findViewById(R.id.map) as MapView }
    val mAMap: AMap by lazy { mMapView.map }
    val mUiSettings: UiSettings by lazy { mAMap.uiSettings }//定义一个UiSettings对象
    //声明AMapLocationClient类对象并初始化
    val mLocationClient: AMapLocationClient by lazy { AMapLocationClient(applicationContext) }
    //声明AMapLocationClientOption对象并初始化
    val mLocationOption: AMapLocationClientOption by lazy { AMapLocationClientOption() }

    // 自定义的 View 防双击点击事件
    val mClickListener: CustomClickListener by lazy { CustomClickListener() }

    var mLatitude: Double = 0.toDouble() // 纬度
    var mLongitude: Double = 0.toDouble() // 经度

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
        mMapView.onCreate(savedInstanceState)
        mAMap.mapType = AMap.MAP_TYPE_NORMAL
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener)

        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.locationMode = AMapLocationClientOption.AMapLocationMode.Hight_Accuracy
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.isNeedAddress = true
        //设置是否只定位一次,默认为false
        mLocationOption.isOnceLocation = true
        //设置是否强制刷新WIFI，默认为强制刷新
        mLocationOption.isWifiActiveScan = true
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.isMockEnable = false
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.interval = 2000
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption)

        mUiSettings.isZoomControlsEnabled = false // 是否显示放大缩小按钮

        mAMap.setLocationSource(InnerLocationSource())
        mUiSettings.isMyLocationButtonEnabled = false // 是否显示高德自带的定位按钮
        ibLocation.onClick {
            ibLocation.isEnabled = false
            ibLocation.setImageResource(R.drawable.null_content)
            pbLocation.visibility = View.VISIBLE
            mLocationClient.startLocation()
        } // 触发高德的定位
        mUiSettings.isScaleControlsEnabled = true //显示比例尺控件

        mAMap.isMyLocationEnabled = true // 进入时默认触发高德定位

        //启动定位  onResume（）中进行定位
        mLocationClient.startLocation()
    }

    override fun onResume() {
        super.onResume()
        //在activity执行onResume时执行mMapView.onResume ()，实现地图生命周期管理
        mMapView.onResume()
        //启动定位
//        mLocationClient.startLocation()
    }

    override fun onPause() {
        super.onPause()
        mLocationClient.stopLocation()//停止定位后，本地定位服务并不会被销毁
        //在activity执行onPause时执行mMapView.onPause ()，实现地图生命周期管理
        mMapView.onPause()
    }

    override fun onDestroy() {
        mLocationClient.onDestroy();//销毁定位客户端，同时销毁本地定位服务。
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy()
        super.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，实现地图生命周期管理
        mMapView.onSaveInstanceState(outState)
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

    var mLocationChangeListener: LocationSource.OnLocationChangedListener?= null

    /**
     * 自带高德地图定位监听器
     */
    inner class InnerLocationSource : LocationSource {
        override fun deactivate() {
            LogUtil.i("高德自带定位停止工作了！")
            mLocationChangeListener = null
        }

        override fun activate(listener: LocationSource.OnLocationChangedListener?) {
            LogUtil.i("高德自带定位开始工作了！")
            mLocationChangeListener = listener
        }

    }

    /**
     * 自定义高德地图定位监听器
     */
    val mLocationListener = AMapLocationListener { amapLocation ->
        if (amapLocation != null) {
            LogUtil.i("自定义高德定位工作了！")
            if (amapLocation.errorCode == 0) {
                //可在其中解析amapLocation获取相应内容。
                //定位成功回调信息，设置相关消息
                val _locationType = amapLocation.locationType // 获取当前定位结果来源，如网络定位结果，详见定位类型表
                mLatitude = amapLocation.latitude // 获取纬度
                mLongitude = amapLocation.longitude // 获取经度
                val _accuracy = amapLocation.accuracy // 获取精度信息)
                val _df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                val _date = Date(amapLocation.time)
                _df.format(_date)//定位时间

                LogUtil.i("获取高德定位信息回调：\n 定位来源：$_locationType \n纬度，经度：($mLatitude，$mLongitude)\n 精度信息：$_accuracy\n 时间：$_date")

                // 设置当前地图显示为当前位置
                mAMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(mLatitude, mLongitude), 50f))
                //点击定位按钮 能够将地图的中心移动到定位点
                mLocationChangeListener?.onLocationChanged(amapLocation)
//                val markerOptions = MarkerOptions()
//                markerOptions.position(LatLng(mLatitude, mLongitude))
//                markerOptions.title("当前位置")
//                markerOptions.visible(true)
//                val bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(resources, R.drawable.main_location_marker))
//                markerOptions.icon(bitmapDescriptor)
//                mAMap.addMarker(markerOptions)
            } else {
                //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                LogUtil.e("location Error, ErrCode:${amapLocation.errorCode}, errInfo:${amapLocation.errorInfo}")
            }
        }

        pbLocation.visibility = View.GONE
        ibLocation.setImageResource(R.drawable.main_location)
        ibLocation.isEnabled = true
    }
}