package com.gavin.hzbicycle.data.bean

/**
 * User: Gavin
 * E-mail: GavinChangCN@163.com
 * Desc:
 * Date: 2016-12-11
 * Time: 18:23
 */
interface BaseView {
    fun isNetworkAvailable(): Boolean
    fun isNetworkAvailable(withTip: Boolean): Boolean
    fun showToast(msg: Int)
    fun showToast(msg: String)
    fun showSuccessToast(msg: Int)
    fun showSuccessToast(msg: String)
    fun showErrorToast(msg: Int)
    fun showErrorToast(msg: String)
}