package com.gavin.hzbicycle.ui.welcome

import android.Manifest
import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import android.support.annotation.NonNull
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import com.gavin.hzbicycle.R
import com.gavin.hzbicycle.base.BaseActivity
import com.gavin.hzbicycle.ui.main.MainActivity
import com.gavin.hzbicycle.util.LogUtil
import com.gavin.hzbicycle.util.PermissionsChecker
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.PermissionNo
import com.yanzhenjie.permission.PermissionYes
import com.yanzhenjie.permission.RationaleListener
import kotlinx.android.synthetic.main.activity_welcome.*
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

    val mImages: IntArray by lazy {
        intArrayOf(R.drawable.welcome_bg_02,
                R.drawable.welcome_bg_03, R.drawable.welcome_bg_04,
                R.drawable.welcome_bg_05, R.drawable.welcome_bg_06,
                R.drawable.welcome_bg_07,
                R.drawable.welcome_bg_09, R.drawable.welcome_bg_12,
                R.drawable.welcome_bg_13, R.drawable.welcome_bg_14,
                R.drawable.welcome_bg_15)
    }
    val mScaleAnims: ArrayList<ScaleAnimation> by lazy {
        arrayListOf(ScaleAnimation(1.0f, 1.4f, 1.0f, 1.4f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.4f
        ), ScaleAnimation(1.4f, 1.0f, 1.4f, 1.0f,
                        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.4f
        ), ScaleAnimation(1.0f, 1.4f, 1.0f, 1.4f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.4f
        ), ScaleAnimation(1.4f, 1.0f, 1.4f, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.4f))
    }
    var mStartIndex: Int = 0
    val mRandom by lazy { Random() }

//    val mInterstitialAd: InterstitialAd by lazy { InterstitialAd(applicationContext) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        checkPermission()

//        mInterstitialAd.adUnitId = getString(R.string.interstitial_ad_welcome_id)
//        requestNewInterstitial()
//        mInterstitialAd.adListener = object: AdListener() {
//            override fun onAdClosed() {
//                super.onAdClosed()
//                requestNewInterstitial()
//                startMainActivity()
//            }
//        }

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
        val _scaleAnim = mScaleAnims[mStartIndex % mScaleAnims.size]
        _scaleAnim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(p0: Animation?) {

            }

            override fun onAnimationEnd(p0: Animation?) {
                LogUtil.d("首页动画结束了！")
                //                if (mInterstitialAd.isLoaded) {
//                    mInterstitialAd.show()
//                } else {
//                    startMainActivity()
//                }
                startMainActivity()
            }

            override fun onAnimationStart(p0: Animation?) {
                LogUtil.d("首页动画开始了！")
            }
        })
        wallPaper.startAnimation(_scaleAnim.apply {
            repeatCount = 0
            repeatMode = Animation.REVERSE
            fillAfter = true
            duration = 1200
        })
    }

//    fun requestNewInterstitial() {
//        val adRequest: AdRequest = AdRequest.Builder()
//                .build()
//        mInterstitialAd.loadAd(adRequest)
//    }

    fun finishInitialize() {
        startAnimation()
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
    val REQUEST_CODE_PERMISSION = 100 // 请求码

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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            LogUtil.d("设备未授予App所需的权限！")
//            PermissionActivity.startActivityForResult(this, true, REQUEST_CODE, mPermissions)
            AndPermission.with(this)
                    .requestCode(REQUEST_CODE_PERMISSION)
                    .permission(
                            Manifest.permission.CALL_PHONE,
                            Manifest.permission.READ_PHONE_STATE,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .rationale(mRationaleListener)
                    .send()
        } else {
            finishInitialize()
        }
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
//        super.onActivityResult(requestCode, resultCode, data)
//        // 拒绝时, 关闭页面, 缺少主要权限, 无法运行
//        if (requestCode == REQUEST_CODE_PERMISSION) {
//            if (resultCode == PermissionActivity.PERMISSIONS_GRANTED) {
//                // 用户允许了获取权限
//                LogUtil.d("用户允许了所有权限。")
//            } else if (resultCode == PermissionActivity.PERMISSIONS_DENIED) {
//                // 用户拒绝了获取权限
//                LogUtil.e("用户拒绝了部分权限！")
//            }
//            finishInitialize()
//        }
//    }

    override fun onRequestPermissionsResult(requestCode: Int, @NonNull permissions: Array<String>, @NonNull grantResults: IntArray) {
        // 只需要调用这一句，剩下的AndPermission自动完成。
        AndPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults)
    }

    // 成功回调的方法，用注解即可，里面的数字是请求时的requestCode。
    @PermissionYes(100)
    private fun getPermissionsYes() {
        // 申请权限成功，可以去做点什么了。
        LogUtil.i("获取权限成功！")
        finishInitialize()
    }

    // 失败回调的方法，用注解即可，里面的数字是请求时的requestCode。
    @PermissionNo(100)
    private fun getPermissionsNo() {
        // 申请权限失败，可以提醒一下用户。
//        Toast.makeText(this, "获取定位权限失败", Toast.LENGTH_SHORT).show()
        LogUtil.e("获取权限失败！")
        finishInitialize()
    }

    val mRationaleListener = RationaleListener { requestCode, rationale ->
        AlertDialog.Builder(this@WelcomeActivity)
                .setTitle("友好提醒")
                .setMessage("没有定位等权限将不能为您提供服务，请把定位等权限赐给我吧！")
                .setPositiveButton("好，给你") { dialog, which ->
                    dialog.cancel()
                    rationale.resume()// 用户同意继续申请。
                }
                .setNegativeButton("我拒绝") { dialog, which ->
                    dialog.cancel()
                    rationale.cancel() // 用户拒绝申请。
                    this@WelcomeActivity.finish()
                }.show()
    }

    /*********************
     * Permission end
     *********************/
}