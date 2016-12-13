package com.gavin.hzbicycle.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.gavin.hzbicycle.R;

/**
 * User: Gavin
 * E-mail: GavinChangCN@163.com
 * Desc:
 * Date: 2016-11-29
 * Time: 01:20
 */
public class CustomDialog extends Dialog {
    protected static final String TAG = "CustomDialog";

    public CustomDialog(Context context) {
        super(context);
    }

    public CustomDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected CustomDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public static class Builder {
        private Context mContext;
        private String mMessage;
        private String mPositiveButtonText;
        private String mNegativeButtonText;
        private String mTitle;
        private boolean mCancelable = false;
        private DialogInterface.OnClickListener mPositiveButtonClickListener;
        private DialogInterface.OnClickListener mNegativeButtonClickListener;

        public Builder(Context context) {
            mContext = context;
        }

        public Builder setMessage(String message) {
            mMessage = message;
            return this;
        }

        public Builder setMessage(int message) {
            mMessage = (String) mContext.getText(message);
            return this;
        }

        public Builder setTitle(String title) {
            mTitle = title;
            return this;
        }

        public Builder setTitle(int title) {
            mTitle = (String) mContext.getText(title);
            return this;
        }

        public Builder setContentView(View v) {
            return this;
        }

        public Builder setCancelable(boolean cancelable) {
            mCancelable = cancelable;
            return this;
        }

        public Builder setPositiveButton(int positiveButtonText,
                                         DialogInterface.OnClickListener listener) {
            mPositiveButtonText = (String) mContext
                    .getText(positiveButtonText);
            mPositiveButtonClickListener = listener;
            return this;
        }

        public Builder setPositiveButton(String positiveButtonText,
                                         DialogInterface.OnClickListener listener) {
            mPositiveButtonText = positiveButtonText;
            mPositiveButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(int negativeButtonText,
                                         DialogInterface.OnClickListener listener) {
            mNegativeButtonText = (String) mContext
                    .getText(negativeButtonText);
            mNegativeButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(String negativeButtonText,
                                         DialogInterface.OnClickListener listener) {
            mNegativeButtonText = negativeButtonText;
            mNegativeButtonClickListener = listener;
            return this;
        }

        public CustomDialog create() {
            LayoutInflater _inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final CustomDialog _dialog = new CustomDialog(mContext, R.style.Dialog);
            View _layout = _inflater.inflate(R.layout.dialog_custom, null);
            ViewGroup _parent = (ViewGroup) _layout.getParent();
            Button _btnPositive = (Button) _layout.findViewById(R.id.dialog_positive);
            Button _btnNegative = (Button) _layout.findViewById(R.id.dialog_negative);
            TextView _title = (TextView) _layout.findViewById(R.id.dialog_title);
            TextView _message = (TextView) _layout.findViewById(R.id.dialog_message);
            if (_parent != null) {
                _parent.removeView(_layout); // 先将view从父容器中清除
            }
            _dialog.addContentView(_layout, new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            //设置title
            if (mTitle != null) {
                _title.setText(Html.fromHtml(mTitle));
            } else {
                _title.setVisibility(View.GONE);
            }

            //设置message
            if (mMessage != null) {
                _message.setText(Html.fromHtml(mMessage));
            } else {
                _message.setText("没有内容");
            }

            //设置按钮
            if (mPositiveButtonText != null) {
                _btnPositive.setText(mPositiveButtonText);
                if (mNegativeButtonText == null) {
                    _btnPositive.setBackgroundResource(R.drawable.custom_dialog_common_button_bg);
                }
                if (mPositiveButtonClickListener != null) {
                    _btnPositive.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            _dialog.dismiss();
                            mPositiveButtonClickListener.onClick(_dialog,
                                    DialogInterface.BUTTON_POSITIVE);
                        }
                    });
                }
            } else {
                // if no confirm button just set the visibility to GONE
                _btnPositive.setVisibility(View.GONE);
                _layout.findViewById(R.id.dialog_line).setVisibility(View.GONE);
            }

            if (mNegativeButtonText != null) {
                _btnNegative.setText(mNegativeButtonText);
                if (mPositiveButtonText == null) {
                    _btnNegative.setBackgroundResource(R.drawable.custom_dialog_common_button_bg);
                }
                if (mNegativeButtonClickListener != null) {
                    _btnNegative.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            _dialog.dismiss();
                            mNegativeButtonClickListener.onClick(_dialog,
                                    DialogInterface.BUTTON_NEGATIVE);
                        }
                    });
                }
            } else {
                // if no confirm button just set the visibility to GONE
                _btnNegative.setVisibility(View.GONE);
                _layout.findViewById(R.id.dialog_line).setVisibility(View.GONE);
            }
            _dialog.setContentView(_layout);

            _dialog.setCancelable(mCancelable);
            _dialog.setCanceledOnTouchOutside(mCancelable); // 设置点击屏幕Dialog不消失,默认为不可取消
//            _dialog.setCancelable(false); // 设置不可 点击弹窗外部或点击返回键 关闭弹窗
            return _dialog;
        }
    }
}
