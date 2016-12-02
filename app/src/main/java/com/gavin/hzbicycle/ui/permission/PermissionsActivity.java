package com.gavin.hzbicycle.ui.permission;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import com.gavin.hzbicycle.R;
import com.gavin.hzbicycle.util.PermissionsChecker;
import com.gavin.hzbicycle.util.callback.INoDataCallback;
import com.gavin.hzbicycle.widget.dialog.CustomDialog;
import com.gavin.hzbicycle.widget.dialog.PermissionAdviceDialogFragment;

/**
 * User: Gavin
 * E-mail: GavinChangCN@163.com
 * Desc:
 * Date: 2016-11-29
 * Time: 00:50
 */
public class PermissionsActivity extends AppCompatActivity {

    public static final int PERMISSIONS_GRANTED = 0; // 权限授权
    public static final int PERMISSIONS_DENIED = 1; // 权限拒绝

    private static final String PERMISSION_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final String PERMISSION_PHONE = Manifest.permission.READ_PHONE_STATE;
    private static final String PERMISSION_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;

    private static final int PERMISSION_REQUEST_CODE = 0; // 系统权限管理页面的参数
    private static final String EXTRA_PERMISSIONS =
            "com.lljz.shire.permission.extra_permission"; // 权限参数
    private static final String EXTRA_IS_NEED_SHOW_DIALOG =
            "isNeedShowDialog"; // 权限参数
    private static final String PACKAGE_URL_SCHEME = "package:"; // 方案

    private PermissionsChecker mChecker; // 权限检测器
    private String[] mPermissions; // 需要获取的权限
    private boolean isNeedShowDialog; // 是否需要显示权限未开放窗口

    private boolean isLocationPermision = true;
    private boolean isPhonePermission = true;
    private boolean isStoragePermission = true;

    // 启动当前权限页面的公开接口
    public static void startActivityForResult(Activity activity, boolean isNeedShowDialog, int requestCode, String... permissions) {
        Intent intent = new Intent(activity, PermissionsActivity.class);
        intent.putExtra(EXTRA_PERMISSIONS, permissions);
        intent.putExtra(EXTRA_IS_NEED_SHOW_DIALOG, isNeedShowDialog);
        ActivityCompat.startActivityForResult(activity, intent, requestCode, null);
    }

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() == null || !getIntent().hasExtra(EXTRA_PERMISSIONS)) {
            throw new RuntimeException("PermissionsActivity需要使用静态startActivityForResult方法启动!");
        }
        setContentView(R.layout.activity_permissions);

        mChecker = new PermissionsChecker(this);
        isNeedShowDialog = getIntent().getBooleanExtra(EXTRA_IS_NEED_SHOW_DIALOG, false);

        mPermissions = getPermissions();
        if (isNeedShowDialog) {
            showLackPermissionDialog();
        } else {
            acquirePermissions();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    // 返回传递的权限参数
    private String[] getPermissions() {
        return getIntent().getStringArrayExtra(EXTRA_PERMISSIONS);
    }

    // 请求权限兼容低版本
    private void requestPermissions(String... permissions) {
        ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST_CODE);
    }

    // 全部权限均已获取
    private void allPermissionsGranted() {
        setResult(PERMISSIONS_GRANTED);
        finish();
    }

    // 显示缺少权限的提示框
    private void showLackPermissionDialog() {
        isLocationPermision = !mChecker.lackPermissions(PERMISSION_LOCATION); // 判断地理位置权限
        isPhonePermission = !mChecker.lackPermissions(PERMISSION_PHONE); // 判断获取电话设备信息的权限
        isStoragePermission = !mChecker.lackPermissions(PERMISSION_STORAGE); // 判断外部存储的读写的权限
        PermissionAdviceDialogFragment.showImpl(getSupportFragmentManager(),
                isStoragePermission, isPhonePermission, isLocationPermision,
                new INoDataCallback() {
                    @Override
                    public void call() {
                        acquirePermissions();
                    }
                });
    }

    // 开始提示获取缺少的权限
    private void acquirePermissions() {
        if (mChecker.lackPermissions(mPermissions)) {
            requestPermissions(mPermissions); // 请求权限
        } else {
            allPermissionsGranted(); // 全部权限都已获取
        }
    }

    /**
     * 用户权限处理,
     * 如果全部获取, 则直接过.
     * 如果权限缺失, 则提示Dialog.
     *
     * @param requestCode  请求码
     * @param permissions  权限
     * @param grantResults 结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE && hasAllPermissionsGranted(grantResults)) {
            allPermissionsGranted();
        } else {
            showMissingPermissionDialog();
        }
    }

    // 含有全部的权限
    private boolean hasAllPermissionsGranted(@NonNull int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }

    // 显示缺失权限提示
    private void showMissingPermissionDialog() {
        if (isNeedShowDialog) {
            CustomDialog.Builder _builder = new CustomDialog.Builder(this);
            _builder.setTitle(R.string.permission_title);
            _builder.setMessage(R.string.permission_content);

            // 拒绝, 关闭弹窗
//            _builder.setNegativeButton(R.string.permission_ignore, new DialogInterface.OnClickListener() {
//                @Override public void onClick(DialogInterface dialog, int which) {
//                    setResult(PERMISSIONS_DENIED);
////                    finish();
//                    showLackPermissionDialog();
//                }
//            });

            _builder.setPositiveButton(R.string.permission_settings, new DialogInterface.OnClickListener() {
                @Override public void onClick(DialogInterface dialog, int which) {
//                    startAppSettings();
                    showLackPermissionDialog();
                }
            });

            _builder.create().show();
        } else {
            finish();
        }
    }

    // 启动应用的设置
    private void startAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse(PACKAGE_URL_SCHEME + getPackageName()));
        startActivity(intent);
    }
}
