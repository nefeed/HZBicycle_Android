package com.gavin.hzbicycle.util

import android.content.Context
import android.content.pm.PackageManager
import android.support.v4.content.ContextCompat

/**
 * User: Gavin
 * E-mail: GavinChangCN@163.com
 * Desc:
 * Date: 2016-05-10
 * Time: 15:25
 */
class PermissionsChecker(context: Context) {
    private var mContext: Context = context

    // 判断权限集合
    fun lackPermissions(permissions: Array<String>): Boolean {
        for (it in permissions) {
            if (lackPermission(it)) {
                LogUtil.d("缺少权限：$it")
                return true
            }
        }
        return false
    }

    // 判断是否缺少权限
    private fun lackPermission(permission: String): Boolean = ContextCompat.checkSelfPermission(mContext, permission) == PackageManager.PERMISSION_DENIED
}