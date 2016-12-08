package com.gavin.hzbicycle.ui.permission

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AlertDialog
import com.gavin.hzbicycle.R
import com.gavin.hzbicycle.base.BaseActivity
import com.gavin.hzbicycle.util.LogUtil
import com.gavin.hzbicycle.util.PermissionsChecker
import com.gavin.hzbicycle.util.gsonUtil.GsonUtil

/**
 * User: Gavin
 * E-mail: GavinChangCN@163.com
 * Desc:
 * Date: 2016-05-10
 * Time: 15:23
 */
class PermissionActivity : BaseActivity() {


    private val PERMISSION_REQUEST_CODE = 0 // 系统权限管理页面的参数

    private val PACKAGE_URL_SCHEME = "package:" // 方案

    private val mChecker: PermissionsChecker by lazy { PermissionsChecker(applicationContext) } // 权限检测器
    private val mLackPermissions: Array<String> by lazy { intent.getStringArrayExtra(EXTRA_PERMISSIONS) } // 获取的缺少的权限
    private var isRequireCheck: Boolean = false // 是否需要系统权限检测, 防止和系统提示框重叠
    private var isNeedShowDialog: Boolean = false // 是否需要显示权限未开放窗口

    // 启动当前权限页面的公开接口
    companion object {
        val PERMISSIONS_GRANTED = 0 // 权限授权
        val PERMISSIONS_DENIED = 1 // 权限拒绝
        private val EXTRA_PERMISSIONS = "com.gavin.hzbicycle.permission.extra_permission" // 权限参数
        private val EXTRA_IS_NEED_SHOW_DIALOG = "isNeedShowDialog" // 权限参数

        fun startActivityForResult(activity: Activity, isNeedShowDialog: Boolean, requestCode: Int, permissions: Array<String>) {

            val intent = Intent(activity, PermissionActivity::class.java)
            intent.putExtra(EXTRA_PERMISSIONS, permissions)
            intent.putExtra(EXTRA_IS_NEED_SHOW_DIALOG, isNeedShowDialog)
            ActivityCompat.startActivityForResult(activity, intent, requestCode, null)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (intent == null || !intent.hasExtra(EXTRA_PERMISSIONS)) {
            throw RuntimeException("PermissionsActivity需要使用静态startActivityForResult方法启动!")
        }
        setContentView(R.layout.activity_permissions)

        isRequireCheck = true
        isNeedShowDialog = intent.getBooleanExtra(EXTRA_IS_NEED_SHOW_DIALOG, false)
    }

    override fun onResume() {
        super.onResume()
        if (isRequireCheck) {
            LogUtil.i("得知需要获取的权限：${GsonUtil.INSTANCE.get().toJson(mLackPermissions)}")
            if (mChecker.lackPermissions(mLackPermissions)) {
                requestPermissions(mLackPermissions) // 请求权限
            } else {
                allPermissionsGranted() // 全部权限都已获取
            }
        } else {
            isRequireCheck = true
        }
    }

    // 请求权限兼容低版本
    private fun requestPermissions(permissions: Array<String>) {
        ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST_CODE)
    }

    // 全部权限均已获取
    private fun allPermissionsGranted() {
        var _intent: Intent = Intent()
        setResult(PERMISSIONS_GRANTED, _intent)
        finish()
    }

    /**
     * 用户权限处理,
     * 如果全部获取, 则直接过.
     * 如果权限缺失, 则提示Dialog.

     * @param requestCode  请求码
     * *
     * @param permissions  权限
     * *
     * @param grantResults 结果
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == PERMISSION_REQUEST_CODE && hasAllPermissionsGranted(grantResults)) {
            isRequireCheck = true
            allPermissionsGranted()
        } else {
            isRequireCheck = false
            showMissingPermissionDialog()
        }
    }

    // 含有全部的权限
    private fun hasAllPermissionsGranted(grantResults: IntArray): Boolean {
        return !grantResults.contains(PackageManager.PERMISSION_DENIED)
    }

    // 显示缺失权限提示
    private fun showMissingPermissionDialog() {
        if (isNeedShowDialog) {
            val _builder = AlertDialog.Builder(this)
            _builder.setTitle(R.string.permission_title)
            _builder.setMessage(R.string.permission_content)

            // 拒绝, 关闭弹窗
            _builder.setNegativeButton(R.string.permission_ignore, DialogInterface.OnClickListener { dialog, which ->
                setResult(PERMISSIONS_DENIED)
                finish()
            })

            _builder.setPositiveButton(R.string.permission_settings, DialogInterface.OnClickListener { dialog, which -> startAppSettings() })

            _builder.create().show()
        } else {
            finish()
        }
    }

    // 启动应用的设置
    private fun startAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.data = Uri.parse(PACKAGE_URL_SCHEME + packageName)
        startActivity(intent)
    }
}