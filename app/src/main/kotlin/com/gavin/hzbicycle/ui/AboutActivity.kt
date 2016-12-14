package com.gavin.hzbicycle.ui

import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import com.gavin.hzbicycle.BuildConfig
import com.gavin.hzbicycle.R
import com.gavin.hzbicycle.base.BaseActivity
import com.gavin.hzbicycle.util.Util
import kotlinx.android.synthetic.main.activity_about.*
import kotlinx.android.synthetic.main.toolbar_normal_layout.*

/**
 * User: Gavin
 * E-mail: GavinChangCN@163.com
 * Desc:
 * Date: 2016-12-02
 * Time: 17:15
 */
class AboutActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        setupToolbar(toolbar)
        tvTitle.text = "关于 杭州公骑君"

        tvVersion.text = Util.getAppVersionName(applicationContext)
        val _buildStr: StringBuffer = StringBuffer("${Util.getAppVersionCode(applicationContext)}")
        _buildStr.append("\t").append(if (BuildConfig.DEBUG) "DEBUG" else "Release")
        tvBuild.text = _buildStr.toString()
    }

    companion object {
        fun startActivity(context: Context, view: View?) {
            val intent = Intent(context, AboutActivity::class.java)
            if (Build.VERSION.SDK_INT > 21) {
                context.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(context as Activity, view, "about").toBundle())
            } else {
                context.startActivity(intent)
            }
        }

    }
}