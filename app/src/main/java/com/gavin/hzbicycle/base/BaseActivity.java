package com.gavin.hzbicycle.base;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;

import com.gavin.hzbicycle.R;
import com.gavin.hzbicycle.ui.permission.PermissionsActivity;
import com.gavin.hzbicycle.util.LogUtil;
import com.gavin.hzbicycle.util.PermissionsChecker;
import com.gavin.hzbicycle.util.slideBack.SlideWindowHelper;
import com.umeng.analytics.MobclickAgent;

/**
 * User: Gavin
 * E-mail: GavinChangCN@163.com
 * Desc:
 * Date: 2016-11-29
 * Time: 00:43
 */
public class BaseActivity extends AppCompatActivity {
    protected static final String TAG = "BaseActivity";

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    /*********************
     * Toolbar begin
     *********************/
    public void setupToolbar(Toolbar toolbar) {
        toolbar.setTitle("");
        toolbar.setNavigationIcon(R.drawable.icon_back);
        setSupportActionBar(toolbar);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    /*********************
     * Toolbar end
     *********************/

    /*********************
     * 滑动返回 begin
     *********************/
    private SlideWindowHelper mSwipeWindowHelper;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if(!supportSlideBack()) {
            return super.dispatchTouchEvent(ev);
        }

        if(mSwipeWindowHelper == null) {
            mSwipeWindowHelper = new SlideWindowHelper(getWindow());
        }
        return mSwipeWindowHelper.processTouchEvent(ev) || super.dispatchTouchEvent(ev);
    }

    /**
     * 是否支持滑动返回
     *
     * @return boolean
     */
    public boolean supportSlideBack() {
        return true;
    }
    /*********************
     * 滑动返回 end
     *********************/

    /*********************
     * Permission begin
     *********************/
    protected static final int REQUEST_CODE = 0; // 请求码

    // 所需的全部权限
    protected static String[] mPermissions = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.CALL_PHONE
    };

    protected PermissionsChecker mPermissionsChecker; // 权限监测工具

    protected void checkPermission() {
        mPermissionsChecker = new PermissionsChecker(this);
        // Android版本号> Android M(6.0) && 缺少需要的权限时，进入权限配置界面
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && mPermissionsChecker.lackPermissions(mPermissions)) {
            LogUtil.d("设备未授予App所需的权限！");
            PermissionsActivity.startActivityForResult(this, true, REQUEST_CODE, mPermissions);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 拒绝时, 关闭页面, 缺少主要权限, 无法运行
        if (requestCode == REQUEST_CODE) {
            if (resultCode == PermissionsActivity.PERMISSIONS_GRANTED) {
                // 用户允许了获取权限
                LogUtil.d("用户允许了所有权限。");
            } else if (resultCode == PermissionsActivity.PERMISSIONS_DENIED) {
                // 用户拒绝了获取权限
                LogUtil.e("用户拒绝了部分权限！");
            }
        }
    }

    /*********************
     * Permission end
     *********************/

}
