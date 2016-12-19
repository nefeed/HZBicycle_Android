package com.gavin.hzbicycle.ui.search

import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v7.widget.SearchView
import android.text.TextUtils
import android.view.View
import android.widget.ArrayAdapter
import com.gavin.hzbicycle.R
import com.gavin.hzbicycle.base.BaseActivity
import kotlinx.android.synthetic.main.activity_search.*




/**
 * User: Gavin
 * E-mail: GavinChangCN@163.com
 * Desc:
 * Date: 2016-12-19
 * Time: 12:58
 */
class SearchActivity : BaseActivity() {


    private val mStrs = arrayOf("aaa", "bbb", "ccc", "airsaid")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        listView.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, mStrs)
        listView.isTextFilterEnabled = true

        // 设置搜索文本监听
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            // 当点击搜索按钮时触发该方法
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            // 当搜索内容改变时触发该方法
            override fun onQueryTextChange(newText: String): Boolean {
                if (!TextUtils.isEmpty(newText)) {
                    listView.setFilterText(newText)
                } else {
                    listView.clearTextFilter()
                }
                return false
            }
        })
    }

    companion object {
        fun startActivity(context: Context, view: View) {
            val intent = Intent(context, SearchActivity::class.java)
            if (Build.VERSION.SDK_INT > 21) {
                context.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(context as Activity, view, "search").toBundle())
            } else {
                context.startActivity(intent)
            }
        }

    }
}