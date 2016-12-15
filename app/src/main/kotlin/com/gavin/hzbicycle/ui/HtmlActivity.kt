package com.gavin.hzbicycle.ui

import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.util.TypedValue
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import com.gavin.hzbicycle.R
import com.gavin.hzbicycle.base.BaseActivity
import kotlinx.android.synthetic.main.activity_html.*
import kotlinx.android.synthetic.main.toolbar_normal_layout.*




/**
 * User: Gavin
 * E-mail: GavinChangCN@163.com
 * Desc:
 * Date: 2016-12-02
 * Time: 17:15
 */
class HtmlActivity : BaseActivity() {

    val mIntentUrl: String by lazy { intent.getStringExtra(INTENT_KEY_URL) }
    val mCustomWebViewClient: CustomWebViewClient by lazy { CustomWebViewClient() }
    val mOnMenuItemClickListener: CustomMenuClickListener by lazy { CustomMenuClickListener() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_html)
        setupToolbar(toolbar)
        toolbar.setOnMenuItemClickListener(mOnMenuItemClickListener)
        tvTitle.text = "页面加载中..."

        webView.setWebViewClient(mCustomWebViewClient)
        webView.loadUrl(mIntentUrl)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        //菜单层次从一个指定的xml资源去填充
        menuInflater.inflate(R.menu.menu_webview, //要加载的布局文件的ID;
                menu)  //要填充的菜单;
        return super.onCreateOptionsMenu(menu)
    }

    inner class CustomWebViewClient : WebViewClient() {
        override fun onPageFinished(view: WebView, url: String?) {
            super.onPageFinished(view, url)
            val _title = view.title
            tvTitle.text = _title

            if (_title.length > 12) {
                tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10.toFloat())
            } else if (_title.length > 10) {
                tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12.toFloat())
            } else if (_title.length > 8) {
                tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14.toFloat())
            } else if (_title.length > 6) {
                tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16.toFloat())
            }
        }
    }

    inner class CustomMenuClickListener : Toolbar.OnMenuItemClickListener {
        override fun onMenuItemClick(item: MenuItem?): Boolean {
            when (item?.itemId) {
                R.id.menuOpenOutSide -> {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(mIntentUrl)
                    startActivity(intent)
                }
            }
            return true
        }
    }

    companion object {
        val INTENT_KEY_URL = "url"
        fun startActivity(context: Context, view: View?, url: String) {
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