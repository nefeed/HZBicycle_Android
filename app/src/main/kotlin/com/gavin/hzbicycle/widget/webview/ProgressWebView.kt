package com.gavin.hzbicycle.widget.webview

import android.content.Context
import android.support.v4.content.ContextCompat.getDrawable
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.ProgressBar
import com.gavin.hzbicycle.R

class ProgressWebView(context: Context, attrs: AttributeSet) : WebView(context, attrs) {

    private val progressbar: ProgressBar

    init {
        progressbar = ProgressBar(context, null,
                android.R.attr.progressBarStyleHorizontal)
        progressbar.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                10)

        val drawable = getDrawable(context, R.drawable.progress_bar)
        progressbar.progressDrawable = drawable
        addView(progressbar)
        setWebChromeClient(WebChromeClient())
        settings.builtInZoomControls = true
    }

    inner class WebChromeClient : android.webkit.WebChromeClient() {
        override fun onProgressChanged(view: WebView, newProgress: Int) {
            if (newProgress == 100) {
                progressbar.visibility = View.GONE
            } else {
                if (progressbar.visibility == View.GONE)
                    progressbar.visibility = View.VISIBLE
                progressbar.progress = newProgress
            }
            super.onProgressChanged(view, newProgress)
        }

    }

    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        val lp = progressbar.layoutParams
        progressbar.layoutParams = lp
        super.onScrollChanged(l, t, oldl, oldt)
    }
}
