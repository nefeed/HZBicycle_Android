package com.gavin.hzbicycle.ui

import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import com.gavin.hzbicycle.R
import com.gavin.hzbicycle.base.BaseActivity
import kotlinx.android.synthetic.main.toolbar_normal_layout.*

/**
 * User: Gavin
 * E-mail: GavinChangCN@163.com
 * Desc:
 * Date: 2016-12-12
 * Time: 11:11
 */
class SettingActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_setting)
        setupToolbar(toolbar)
        tvTitle.text = "设置"
        super.onCreate(savedInstanceState)
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