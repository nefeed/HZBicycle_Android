package com.gavin.hzbicycle.ui.welcome

import android.Manifest
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import com.gavin.hzbicycle.R
import com.gavin.hzbicycle.base.BaseActivity
import com.gavin.hzbicycle.ui.MainActivity
import com.gavin.hzbicycle.ui.permission.PermissionActivity
import com.gavin.hzbicycle.util.LogUtil
import com.gavin.hzbicycle.util.PermissionsChecker
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import kotlinx.android.synthetic.main.activity_welcome.*
import org.jetbrains.anko.activityUiThreadWithContext
import org.jetbrains.anko.async
import org.jetbrains.anko.startActivity
import java.util.*

/**
 * User: Gavin
 * E-mail: GavinChangCN@163.com
 * Desc:
 * Date: 2016-03-23
 * Time: 11:54
 */
class WelcomeActivity : BaseActivity() {

    val mImages: IntArray by lazy { intArrayOf(R.drawable.welcome_bg_01, R.drawable.welcome_bg_02,
            R.drawable.welcome_bg_03, R.drawable.welcome_bg_04,
            R.drawable.welcome_bg_05, R.drawable.welcome_bg_06,
            R.drawable.welcome_bg_07, R.drawable.welcome_bg_08,
            R.drawable.welcome_bg_09, R.drawable.welcome_bg_10) }
    val mScaleAnims: Array<ScaleAnimation> by lazy { arrayOf(ScaleAnimation(1.1f, 1.4f, 1.1f, 1.4f,
            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.4f
            ),
            ScaleAnimation(1.4f, 1.1f, 1.4f, 1.1f,
            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.4f)) }
    var mStartIndex: Int = 0
    val mRandom by lazy { Random() }

    val mInterstitialAd: InterstitialAd by lazy { InterstitialAd(applicationContext) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        checkPermission()

        mInterstitialAd.adUnitId = getString(R.string.interstitial_ad_welcome_id)
        requestNewInterstitial()
        mInterstitialAd.adListener = object: AdListener() {
            override fun onAdClosed() {
                super.onAdClosed()
                requestNewInterstitial()
                startMainActivity()
            }
        }

        var newStartIndex: Int
        do {
            newStartIndex = mImages[mRandom.nextInt(mImages.size)]
        } while (newStartIndex == mStartIndex)
        mStartIndex = newStartIndex
        val drawableId = mImages[mStartIndex % mImages.size]
        wallPaper.setImageResource(drawableId)

    }

    fun startAnimation() {
        if (wallPaper.animation != null) return

        var newStartIndex: Int
        do {
            newStartIndex = mRandom.nextInt(mScaleAnims.size)
        } while (newStartIndex == mStartIndex)
        mStartIndex = newStartIndex
        val _scaleAnim = mScaleAnims[mStartIndex % mImages.size]
        wallPaper.startAnimation(_scaleAnim.apply {
            repeatCount = 1
            repeatMode = Animation.REVERSE
            duration = 1700
        })
    }

    fun requestNewInterstitial() {
        val adRequest: AdRequest = AdRequest.Builder()
                .build()
        mInterstitialAd.loadAd(adRequest)
    }

    fun finishInitialize() {
        startAnimation()

        async() {
            Thread.sleep(1600)

            activityUiThreadWithContext {
                if (mInterstitialAd.isLoaded) {
                    mInterstitialAd.show()
                } else {
                    startMainActivity()
                }
            }
        }

    }

    fun startMainActivity() {
        startActivity<MainActivity>()
        // activity切换的淡入淡出效果
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        finish()
    }

    /*********************
     * Permission begin
     */
    val REQUEST_CODE = 0 // 请求码

    // 所需的全部权限
    val mPermissions by lazy {
        arrayOf(
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    val mPermissionsChecker: PermissionsChecker by lazy { PermissionsChecker(this) } // 权限监测工具

    fun checkPermission() {
        // Android版本号> Android M(6.0) && 缺少需要的权限时，进入权限配置界面
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && mPermissionsChecker.lackPermissions(mPermissions)) {
            LogUtil.d("设备未授予App所需的权限！")
            PermissionActivity.startActivityForResult(this, true, REQUEST_CODE, mPermissions)
        } else {
            finishInitialize()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        // 拒绝时, 关闭页面, 缺少主要权限, 无法运行
        if (requestCode == REQUEST_CODE) {
            if (resultCode == PermissionActivity.PERMISSIONS_GRANTED) {
                // 用户允许了获取权限
                LogUtil.d("用户允许了所有权限。")
            } else if (resultCode == PermissionActivity.PERMISSIONS_DENIED) {
                // 用户拒绝了获取权限
                LogUtil.e("用户拒绝了部分权限！")
            }
            finishInitialize()
        }
    }

    /*********************
     * Permission end
     *********************/
}