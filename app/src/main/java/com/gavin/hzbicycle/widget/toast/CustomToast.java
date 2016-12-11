package com.gavin.hzbicycle.widget.toast;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gavin.hzbicycle.R;

/**
 * User: Gavin
 * E-mail: GavinChangCN@163.com
 * Desc:
 * Date: 2016-12-11
 * Time: 18:28
 */
public enum CustomToast {
    INSTANCE;

    private Toast mSuccessToast, mErrorToast, mCommonToast, mImgToast;
    private TextView mTvSuccess, mTvError, mTvCommon, mTvImgToast;
    private ImageView mIvToastImg;

    public void showSuccessToast(Context ctx, String content) {
        if (mSuccessToast == null) {
            mSuccessToast = new Toast(ctx);
            mSuccessToast.setGravity(Gravity.CENTER, 0, 0);
            mSuccessToast.setDuration(Toast.LENGTH_SHORT);
            View _root = LayoutInflater.from(ctx).inflate(R.layout.toast_success, null);
            mTvSuccess = (TextView) _root.findViewById(R.id.tvToastContent);
            mSuccessToast.setView(_root);
        }
        mTvSuccess.setText(content);
        mSuccessToast.show();
    }

    public void showSuccessToast(Context ctx, int contentId) {
        showSuccessToast(ctx, ctx.getString(contentId));
    }

    public void showErrorToast(Context ctx, String content) {
        if (mErrorToast == null) {
            mErrorToast = new Toast(ctx);
            mErrorToast.setGravity(Gravity.CENTER, 0, 0);
            mErrorToast.setDuration(Toast.LENGTH_SHORT);
            View _root = LayoutInflater.from(ctx).inflate(R.layout.toast_error, null);
            mTvError = (TextView) _root.findViewById(R.id.tvToastContent);
            mErrorToast.setView(_root);
        }
        mTvError.setText(content);
        mErrorToast.show();
    }

    public void showErrorToast(Context ctx, int contentId) {
        showErrorToast(ctx, ctx.getString(contentId));
    }

    public void showToast(Context ctx, String content) {
        if (mCommonToast == null) {
            mCommonToast = new Toast(ctx);
            mCommonToast.setGravity(Gravity.CENTER, 0, 0);
            mCommonToast.setDuration(Toast.LENGTH_SHORT);
            View _root = LayoutInflater.from(ctx).inflate(R.layout.toast_common, null);
            mTvCommon = (TextView) _root.findViewById(R.id.tvToastContent);
            mCommonToast.setView(_root);
        }
        mTvCommon.setText(content);
        mCommonToast.show();
    }

    public void showToast(Context ctx, int contentId) {
        showToast(ctx, ctx.getString(contentId));
    }

    public void showImgToast(Context ctx, String content, Drawable img) {
        if (mImgToast == null) {
            mImgToast = new Toast(ctx);
            mImgToast.setGravity(Gravity.CENTER, 0, 0);
            mImgToast.setDuration(Toast.LENGTH_SHORT);
            View _root = LayoutInflater.from(ctx).inflate(R.layout.toast_img, null);
            mTvImgToast = (TextView) _root.findViewById(R.id.tvToastContent);
            mIvToastImg = (ImageView) _root.findViewById(R.id.ivToastIcon);
            mImgToast.setView(_root);
        }
        mIvToastImg.setImageDrawable(img);
        mTvImgToast.setText(content);
        mImgToast.show();
    }

    public void showImgToast(Context ctx, int contentId, int imgId) {
        showImgToast(ctx, ctx.getString(contentId), ctx.getResources().getDrawable(imgId));
    }

    public void cancelToast() {
        if (mSuccessToast != null) {
            mSuccessToast.cancel();
        }
        if (mErrorToast != null) {
            mErrorToast.cancel();
        }
        if (mCommonToast != null) {
            mCommonToast.cancel();
        }
        if (mImgToast != null) {
            mImgToast.cancel();
        }
    }
}
