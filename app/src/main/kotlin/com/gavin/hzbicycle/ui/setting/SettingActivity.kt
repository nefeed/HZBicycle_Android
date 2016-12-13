package com.gavin.hzbicycle.ui

import android.Manifest
import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.view.View
import android.widget.Toast
import com.amap.api.maps.offlinemap.OfflineMapManager
import com.amap.api.maps.offlinemap.OfflineMapStatus
import com.gavin.hzbicycle.R
import com.gavin.hzbicycle.base.BaseActivity
import com.gavin.hzbicycle.util.LogUtil
import com.gavin.hzbicycle.widget.button.NoDoubleClickListener
import kotlinx.android.synthetic.main.activity_setting.*
import kotlinx.android.synthetic.main.toolbar_normal_layout.*
import org.jetbrains.anko.ctx
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
    val PHONE_NUMBER_SERVICE = "0571-85331122"
    val HANGZHOU_AD_CODE = "330100"
    val HANGZHOU_CODE = "0571"

    val mClickListener: CustomClickListener by lazy { CustomClickListener() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        setupToolbar(toolbar)
        tvTitle.text = "设置"

        checkHaveDownloaded()
        btnOfflineMap.onClick {
            v -> mClickListener.onNoDoubleClick(v)
        }

        rLayoutPhone.onClick {
            v -> mClickListener.onNoDoubleClick(v)
        }

        rLayoutMail.onClick {
            v -> mClickListener.onNoDoubleClick(v)
        }
        rLayoutAbout.onClick {
            v -> mClickListener.onNoDoubleClick(v)
        }

    }

    override fun onDestroy() {
        mOfflineMapManager.destroy()
        super.onDestroy()
    }

    inner class CustomClickListener : NoDoubleClickListener() {
        override fun onNoDoubleClick(v: View?) {
            when (v?.id) {
                R.id.btnOfflineMap -> {
                    if (mIsDownloaded) {
                        mOfflineMapManager.remove("hangzhou")
                    } else {
                        btnOfflineMap.text = getString(R.string.setting_pause)
                        mOfflineMapManager.downloadByCityName("hangzhou")
                    }
                }
                R.id.rLayoutPhone -> callForService()

                R.id.rLayoutMail -> sendMailToAuthor()
                R.id.rLayoutAbout -> AboutActivity.startActivity(this@SettingActivity, v)
            }
        }
    }

    private fun checkHaveDownloaded() {
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

    private fun callForService() {
        createDialog(this, -1, getString(R.string.dialog_feedback_service_call_msg, PHONE_NUMBER_SERVICE),
                R.string.dialog_service_call,
                mServicePhoneNoPositiveListener)
    }

    private val mServicePhoneNoPositiveListener = DialogInterface.OnClickListener { dialog, which ->
        dialog.dismiss()
        val _intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + PHONE_NUMBER_SERVICE))
        _intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        if (ActivityCompat.checkSelfPermission(ctx, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            ctx.startActivity(_intent)
        } else {
            showErrorToast(R.string.requires_phone_call_permission)
        }
    }

    private fun sendMailToAuthor() {
        val data = Intent(Intent.ACTION_SENDTO)
        data.data = Uri.parse("mailto:gavinchangcn@163.com")
        data.putExtra(Intent.EXTRA_SUBJECT, "HZBicycle-意见反馈")
        data.putExtra(Intent.EXTRA_TEXT, "意见反馈内容如下：\n")
        startActivity(data)
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