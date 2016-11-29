package com.gavin.hzbicycle.ui

import android.os.Bundle
import android.widget.TextView
import com.gavin.hzbicycle.R
import com.gavin.hzbicycle.ui.base.BaseActivity

/**
 * User: Gavin
 * E-mail: GavinChangCN@163.com
 * Desc:
 * Date: 2016-11-29
 * Time: 00:28
 */
class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        checkPermission()

        // Example of a call to a native method
        val tv = findViewById(R.id.sample_text) as TextView
//        tv.text = stringFromJNI()

    }

//    /**
//     * A native method that is implemented by the 'native-lib' native library,
//     * which is packaged with this application.
//     */
//    external fun stringFromJNI(): String
//
//    companion object {
//
//        // Used to load the 'native-lib' library on application startup.
//        init {
//            System.loadLibrary("native-lib")
//        }
//    }
}