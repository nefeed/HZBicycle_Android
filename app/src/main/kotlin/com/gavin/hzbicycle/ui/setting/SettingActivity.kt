package com.gavin.hzbicycle.ui

import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.amap.api.maps.offlinemap.OfflineMapManager
import com.amap.api.maps.offlinemap.OfflineMapStatus
import com.gavin.hzbicycle.R
import com.gavin.hzbicycle.base.BaseActivity
import com.gavin.hzbicycle.util.LogUtil
import kotlinx.android.synthetic.main.activity_setting.*
import kotlinx.android.synthetic.main.toolbar_normal_layout.*
import org.jetbrains.anko.onClick
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * User: Gavin
 * E-mail: GavinChangCN@163.com
 * Desc:
 * Date: 2016-12-12
 * Time: 11:11
 */
class SettingActivity : BaseActivity() {

    val mOfflineMapManager: OfflineMapManager by lazy { OfflineMapManager(application, CustomOfflineMapDownloadListener()) }
    var mIsDownloaded = false
    val HANGZHOU_AD_CODE = "330100"
    val HANGZHOU_CODE = "0571"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        setupToolbar(toolbar)
        tvTitle.text = "设置"

        checkHaveDownloaded()
        btnOfflineMap.onClick {
            if (mIsDownloaded) {
                mOfflineMapManager.remove("hangzhou")
            } else {
                btnOfflineMap.text = getString(R.string.setting_pause)
                mOfflineMapManager.downloadByCityName("hangzhou")
            }
        }
    }

    override fun onDestroy() {
        mOfflineMapManager.destroy()
        super.onDestroy()
    }

    fun checkHaveDownloaded() {


        Observable.from(mOfflineMapManager.downloadOfflineMapCityList)
                .filter({ data ->
                    data.code == HANGZHOU_CODE
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ data ->
                    btnOfflineMap.text = getString(R.string.setting_remove)
                    mIsDownloaded = true
                })


    }

    inner class CustomOfflineMapDownloadListener : OfflineMapManager.OfflineMapDownloadListener {
        override fun onDownload(status: Int, completeCode: Int, downName: String?) {
            when (status) {
                OfflineMapStatus.SUCCESS -> {
                    LogUtil.i("下载完成，completeCode：$completeCode，downName：$downName")
                    btnOfflineMap.text = getString(R.string.setting_remove)
                    mIsDownloaded = true
                }
                OfflineMapStatus.LOADING -> LogUtil.d("amap-download", "download: " + completeCode + "%" + ","
                        + downName)
                OfflineMapStatus.UNZIP -> LogUtil.d("amap-unzip", "unzip: $completeCode%,$downName")
                OfflineMapStatus.WAITING -> LogUtil.d("amap-waiting", "WAITING: " + completeCode + "%" + ","
                        + downName)
                OfflineMapStatus.PAUSE -> LogUtil.d("amap-pause", "pause: $completeCode%,$downName")
                OfflineMapStatus.STOP -> {
                }
                OfflineMapStatus.ERROR -> LogUtil.e("amap-download", "download: " + " ERROR " + downName)
                OfflineMapStatus.EXCEPTION_AMAP -> LogUtil.e("amap-download", "download: " + " EXCEPTION_AMAP " + downName)
                OfflineMapStatus.EXCEPTION_NETWORK_LOADING -> {
                    LogUtil.e("amap-download", "download: " + " EXCEPTION_NETWORK_LOADING "
                            + downName)
                    Toast.makeText(this@SettingActivity, "网络异常", Toast.LENGTH_SHORT)
                            .show()
                    mOfflineMapManager.pause()
                }
                OfflineMapStatus.EXCEPTION_SDCARD -> LogUtil.e("amap-download", "download: " + " EXCEPTION_SDCARD "
                        + downName)
                else -> {
                }
            }
        }

        override fun onCheckUpdate(hasNew: Boolean, name: String?) {
            LogUtil.i("amap-demo", "onCheckUpdate $name : $hasNew")
        }

        override fun onRemove(success: Boolean, name: String?, describe: String?) {
            LogUtil.i("amap-demo", "onRemove " + name + " : " + success + " , "
                    + describe)

            btnOfflineMap.text = getString(R.string.setting_download)
            mIsDownloaded = false
        }

    }

    companion object {
        fun startActivity(context: Context, view: View) {
            val intent = Intent(context, SettingActivity::class.java)
            if (Build.VERSION.SDK_INT > 21) {
                context.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(context as Activity, view, "setting").toBundle())
            } else {
                context.startActivity(intent)
            }
        }

    }
}