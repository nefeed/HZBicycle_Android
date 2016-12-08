package com.gavin.hzbicycle.ui

import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import com.gavin.hzbicycle.R
import com.gavin.hzbicycle.base.BaseActivity
import kotlinx.android.synthetic.main.activity_html.*

/**
 * User: Gavin
 * E-mail: GavinChangCN@163.com
 * Desc:
 * Date: 2016-12-02
 * Time: 17:15
 */
class HtmlActivity : BaseActivity() {

    val mIntentUrl: String by lazy { intent.getStringExtra(INTENT_KEY_URL) }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.activity_html)
        setupToolbar(toolbar)
        tv_title.text = ""

        webView.loadUrl(mIntentUrl)
    }

    companion object {
        val INTENT_KEY_URL = "url"
        fun startActivity(context: Context, view: View, url: String) {
            val intent = Intent(context, HtmlActivity::class.java)
            intent.putExtra(INTENT_KEY_URL, url)
            if (Build.VERSION.SDK_INT > 21) {
                context.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(context as Activity, view, "html").toBundle())
            } else {
                context.startActivity(intent)
            }
        }

    }
}