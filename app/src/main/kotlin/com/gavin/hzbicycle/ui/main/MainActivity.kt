package com.gavin.hzbicycle.ui.main

import android.content.DialogInterface
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import com.amap.api.maps.*
import com.amap.api.maps.AMap.OnInfoWindowClickListener
import com.amap.api.maps.model.*
import com.gavin.hzbicycle.R
import com.gavin.hzbicycle.base.BaseActivity
import com.gavin.hzbicycle.data.PreferenceRepository
import com.gavin.hzbicycle.data.bean.BicycleStationBean
import com.gavin.hzbicycle.ui.SettingActivity
import com.gavin.hzbicycle.ui.search.SearchActivity
import com.gavin.hzbicycle.util.LogUtil
import com.gavin.hzbicycle.widget.button.NoDoubleClickListener
import kotlinx.android.synthetic.main.activity_main.*
import rx.Observable
import rx.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.*


/**
 * User: Gavin
 * E-mail: GavinChangCN@163.com
 * Desc:
 * Date: 2016-11-29
 * Time: 00:28
 */
class MainActivity : BaseActivity(), MainContract.View, AMap.InfoWindowAdapter {

    val mMapView: MapView by lazy { findViewById(R.id.map) as MapView }
    val mAMap: AMap by lazy { mMapView.map }
    val mUiSettings: UiSettings by lazy { mAMap.uiSettings }//定义一个UiSettings对象
    //声明AMapLocationClient类对象并初始化
    val mLocationClient: AMapLocationClient by lazy { AMapLocationClient(applicationContext) }
    //声明AMapLocationClientOption对象并初始化
    val mLocationOption: AMapLocationClientOption by lazy { AMapLocationClientOption() }

    // 自定义的 View 防双击点击事件
    val mClickListener: CustomClickListener by lazy { CustomClickListener() }
    val mConverter: CoordinateConverter by lazy { CoordinateConverter(applicationContext) }

    var mLatitude: Double = 0.toDouble() // 纬度
    var mLongitude: Double = 0.toDouble() // 经度

    val mPresenter: MainContract.Presenter by lazy { MainPresenter(this@MainActivity) }
    var mIsFirstLocation = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initAMap(savedInstanceState)
        ibSetting.setOnClickListener(mClickListener)
        btnSearch.setOnClickListener(mClickListener)
    }

    /**
     * 初始化高德地图模块
     */
    fun initAMap(savedInstanceState: Bundle?) {
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，实现地图生命周期管理
        mMapView.onCreate(savedInstanceState)

        // 移动地图初始位置为浙江省政府位置
        mAMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(30.266511, 120.152734), 16f))

        mAMap.mapType = AMap.MAP_TYPE_NORMAL
        // 设置定位的类型为定位模式 ，可以由定位、跟随或地图根据面向方向旋转几种
        mAMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE)
        setupLocationStyle()
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener)

        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.locationMode = AMapLocationClientOption.AMapLocationMode.Hight_Accuracy
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.isNeedAddress = true
        //设置是否只定位一次,默认为false
        mLocationOption.isOnceLocation = true
        //设置是否强制刷新WIFI，默认为强制刷新
        mLocationOption.isWifiScan = true
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.isMockEnable = false
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.interval = 2000
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption)

        mUiSettings.isZoomControlsEnabled = false // 是否显示放大缩小按钮

        mAMap.setLocationSource(InnerLocationSource())
        mUiSettings.isMyLocationButtonEnabled = false // 是否显示高德自带的定位按钮
        ibLocation.setOnClickListener {
            ibLocation.isEnabled = false
            ibLocation.setImageResource(R.drawable.null_content)
            pbLocation.visibility = View.VISIBLE
            mLocationClient.startLocation()
        } // 触发高德的定位
        mUiSettings.isScaleControlsEnabled = true //显示比例尺控件

        mAMap.isMyLocationEnabled = true // 进入时默认触发高德定位

        val _infoWindowListener = OnInfoWindowClickListener { marker -> }
        val _markerClickListener = AMap.OnMarkerClickListener { marker -> false }

        // 绑定Marker信息窗点击事件
        mAMap.setOnMarkerClickListener(_markerClickListener)
        mAMap.setOnInfoWindowClickListener(_infoWindowListener)
        mAMap.setOnMapLongClickListener { latlon ->
            LogUtil.d("获取的长按地点的坐标为：（${latlon.latitude}，${latlon.longitude})")
            mLatitude = latlon.latitude
            mLongitude = latlon.longitude
            createDialog(this, R.string.advice, R.string.dialog_long_click_map_search_content,
                    R.string.dialog_ok,
                    mMapLongClickSearchListener)
        }
        mAMap.setInfoWindowAdapter(this@MainActivity)

        //启动定位  onResume（）中进行定位
        mLocationClient.startLocation()

    }

    private val STROKE_COLOR = Color.argb(30, 16, 118, 244)
    private val FILL_COLOR = Color.argb(80, 16, 118, 244)

    /**
     * 设置自定义定位样式
     */
    fun setupLocationStyle() {
        // 自定义系统定位蓝点
        val myLocationStyle = MyLocationStyle()
        // 自定义定位蓝点图标
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable.main_gps_point))
        // 自定义精度范围的圆形边框颜色
        myLocationStyle.strokeColor(STROKE_COLOR)
        //自定义精度范围的圆形边框宽度
        myLocationStyle.strokeWidth(5f)
        // 设置圆形的填充颜色
        myLocationStyle.radiusFillColor(FILL_COLOR)
        // 将自定义的 myLocationStyle 对象添加到地图上
        mAMap.setMyLocationStyle(myLocationStyle)
    }

    override fun onResume() {
        super.onResume()
        //在activity执行onResume时执行mMapView.onResume ()，实现地图生命周期管理
        mMapView.onResume()
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
        override fun onNoDoubleClick(v: View) {
            when (v.id) {
                R.id.ibSetting -> {
                    SettingActivity.startActivity(this@MainActivity, v)
                }
                R.id.btnSearch -> {
                    SearchActivity.startActivity(this@MainActivity, v)
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

    var mLocationChangeListener: LocationSource.OnLocationChangedListener? = null

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
                mAMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(mLatitude, mLongitude), 16f))
                //点击定位按钮 能够将地图的中心移动到定位点
                mLocationChangeListener?.onLocationChanged(amapLocation)
                mPresenter.loadNearbyBicycleStationData(mLatitude, mLongitude)

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

        if (mIsFirstLocation) {
            Observable.just(PreferenceRepository.INSTANCE.appStartNumber())
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .subscribe({number -> PreferenceRepository.INSTANCE.appStartNumberPlus(number) })
            Thread {
                run {
                    Thread.sleep(300)
                    runOnUiThread {
                        //启动定位  onResume（）中进行定位
                        mLocationClient.startLocation()
                        mIsFirstLocation = false
                        // 每打开5次，进行一次提醒
                        if ((PreferenceRepository.INSTANCE.appStartNumber() + 7) % 8 == 0) {
                            Toast.makeText(this@MainActivity,
                                    R.string.toast_long_click_map_search_content, Toast.LENGTH_SHORT)
                                    .show()
                        }
                    }
                }
            }.start()
        }
        pbLocation.visibility = View.GONE
        ibLocation.setImageResource(R.drawable.main_location)
        ibLocation.isEnabled = true
    }

    val mMarkerArray by lazy { ArrayList<Marker>() }
    override fun updateNearbyBicycleStationData(data: ArrayList<BicycleStationBean>) {
        for (it in mMarkerArray) {
            it.remove()
        }
        mMarkerArray.clear()
        // CoordType.GPS 待转换坐标类型
        mConverter.from(CoordinateConverter.CoordType.BAIDU)
        for (it in data) {
            // sourceLatLng待转换坐标点 LatLng类型
            mConverter.coord(LatLng(it.lat, it.lon))
            // 执行转换操作
            val _desLatLng = mConverter.convert()
            val _markOptions = MarkerOptions()
                    .position(_desLatLng)
                    .draggable(false)
                    .title("  ${it.name}(编号：${it.number})  ")
                    .snippet("  可借：${it.rentcount}    可还：${it.restorecount}")
                    .icon(BitmapDescriptorFactory
                            .fromBitmap(BitmapFactory
                                    .decodeResource(resources, R.drawable.main_bicycle_marker)))
            val _marker: Marker = mAMap.addMarker(_markOptions)
            mMarkerArray.add(_marker)
        }
        if (mMarkerArray.size > 0) {
            mMarkerArray[0].showInfoWindow()
        }
    }

    private val mMapLongClickSearchListener by lazy {
        DialogInterface.OnClickListener { dialog, which ->
            dialog.dismiss()

            mPresenter.loadNearbyBicycleStationData(mLatitude, mLongitude)
        }
    }

    /******************************************
     *
     *   高德地图标记点的点击信息窗口 Begin
     *
     ******************************************/
    override fun getInfoContents(marker: Marker): View? {
        val infoContent = layoutInflater.inflate(
                R.layout.custom_info_contents, null)
        render(marker, infoContent)
        return infoContent
    }

    override fun getInfoWindow(marker: Marker): View? {
        val infoWindow = layoutInflater.inflate(
                R.layout.custom_info_window, null)

        render(marker, infoWindow)
        return infoWindow
    }

    /**
     * 自定义infowinfow窗口
     */
    fun render(marker: Marker, view: View) {
        val title = marker.title
        val titleUi = view.findViewById(R.id.title) as TextView
        if (title != null) {
            val titleText = SpannableString(title)
//            titleText.setSpan(ForegroundColorSpan(Color.RED), 0,
//                    titleText.length, 0)
//            titleUi.textSize = 15f
            titleUi.text = titleText

        } else {
            titleUi.text = ""
        }
        val snippet = marker.snippet
        val snippetUi = view.findViewById(R.id.snippet) as TextView
        if (snippet != null) {
            val snippetText = SpannableString(snippet)
//            snippetText.setSpan(ForegroundColorSpan(Color.GREEN), 0,
//                    snippetText.length, 0)
//            snippetUi.textSize = 20f
            snippetUi.text = snippetText
        } else {
            snippetUi.text = ""
        }
    }

    /******************************************
     *
     *   高德地图标记点的点击信息窗口 End
     *
     ******************************************/

}