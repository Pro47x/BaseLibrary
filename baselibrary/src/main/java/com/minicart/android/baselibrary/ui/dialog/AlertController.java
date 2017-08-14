package com.minicart.android.baselibrary.ui.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.minicart.android.baselibrary.R;

import java.lang.ref.WeakReference;

/**
 * @类名：AlertController
 * @描述：
 * @创建人：54506
 * @创建时间：2017/3/15 15:55
 * @版本：
 */

final class AlertController {

    private LinearLayout ll_buttonPanel;
    private TextView tv_contentText;
    private RelativeLayout rl_alertContent;

    private AlertDialog mDialog;
    private Window mWindow;

    private RelativeLayout rl_topPanel;
    private ImageView iv_icon;
    private int mIconId;
    private Drawable mIcon;
    private TextView tv_title;
    private CharSequence mTitle;
    public boolean mShowTitleView = true;

    private Button btn_positive;
    private CharSequence mButtonPositiveText;
    private Message mButtonPositiveMessage;
    public boolean mPositiveButtonVisibility = true;

    private Button btn_negative;
    private CharSequence mButtonNegativeText;
    private Message mButtonNegativeMessage;
    public boolean mNegativeButtonVisibility = true;

    public View mContentView;
    public CharSequence mMessage;

    Handler mHandler;

    private static final class ButtonHandler extends Handler {
        // Button clicks have Message.what as the BUTTON{1,2,3} constant
        private static final int MSG_DISMISS_DIALOG = 1;

        private WeakReference<DialogInterface> mDialog;

        public ButtonHandler(DialogInterface dialog) {
            mDialog = new WeakReference<>(dialog);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case DialogInterface.BUTTON_POSITIVE:
                case DialogInterface.BUTTON_NEGATIVE:
                case DialogInterface.BUTTON_NEUTRAL:
                    ((DialogInterface.OnClickListener) msg.obj).onClick(mDialog.get(), msg.what);
                    break;

                case MSG_DISMISS_DIALOG:
                    ((DialogInterface) msg.obj).dismiss();
            }
        }
    }

    private final View.OnClickListener mButtonHandler = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final Message m;
            if (v == btn_positive && mButtonPositiveMessage != null) {
                m = Message.obtain(mButtonPositiveMessage);
            } else if (v == btn_negative && mButtonNegativeMessage != null) {
                m = Message.obtain(mButtonNegativeMessage);
            } else {
                m = null;
            }

            if (m != null) {
                m.sendToTarget();
            }

            // Post a message so we dismiss after the above handlers are executed
            mHandler.obtainMessage(ButtonHandler.MSG_DISMISS_DIALOG, mDialog)
                    .sendToTarget();
        }
    };

    public AlertController(AlertDialog dialog, Window window) {
        this.mDialog = dialog;
        this.mWindow = window;
        this.mHandler = new ButtonHandler(dialog);
    }

    /**
     * @return 获得dialog
     */
    public AlertDialog getDialog() {
        return mDialog;
    }

    /**
     * @return 获取dialog的window
     */
    public Window getWindow() {
        return mWindow;
    }

    /**
     * @param showTitleView 是否显示title
     */
    public void showTitleView(boolean showTitleView) {
        if (rl_topPanel == null) {
            return;
        }
        rl_topPanel.setVisibility(showTitleView ? View.VISIBLE : View.GONE);
    }

    public void setNegativeButtonVisibility(boolean visibility) {
        if (btn_negative == null) {
            return;
        }
        btn_negative.setVisibility(visibility ? View.VISIBLE : View.GONE);
    }

    private void setPositiveButtonVisibility(boolean visibility) {
        if (btn_positive == null) {
            return;
        }
        btn_positive.setVisibility(visibility ? View.VISIBLE : View.GONE);
    }

    /**
     * Sets a click listener or a message to be sent when the button is clicked.
     * You only need to pass one of {@code listener} or {@code msg}.
     *
     * @param whichButton Which button, can be one of
     *                    {@link DialogInterface#BUTTON_POSITIVE},
     *                    {@link DialogInterface#BUTTON_NEGATIVE}, or
     *                    {@link DialogInterface#BUTTON_NEUTRAL}
     * @param text        The text to display in positive button.
     * @param listener    The {@link DialogInterface.OnClickListener} to use.
     * @param msg         The {@link Message} to be sent when clicked.
     */
    public void setButton(int whichButton, CharSequence text,
                          DialogInterface.OnClickListener listener, Message msg) {

        if (msg == null && listener != null) {
            msg = mHandler.obtainMessage(whichButton, listener);
        }

        switch (whichButton) {

            case DialogInterface.BUTTON_POSITIVE:
                mButtonPositiveText = text;
                mButtonPositiveMessage = msg;
                break;

            case DialogInterface.BUTTON_NEGATIVE:
                mButtonNegativeText = text;
                mButtonNegativeMessage = msg;
                break;

            default:
                throw new IllegalArgumentException("Button does not exist");
        }
    }

    public void setMessage(CharSequence message) {
        mMessage = message;
        if (tv_contentText != null) {
            tv_contentText.setText(message);
        }
    }

    public void setAlertContentView(View contentView) {
        mContentView = contentView;
        if (rl_alertContent == null) {
            return;
        }
        rl_alertContent.removeAllViews();
        rl_alertContent.addView(contentView);
    }

    public void setTitle(CharSequence title) {
        mTitle = title;
        if (tv_title != null) {
            tv_title.setText(title);
        }
    }

    public void cancel() {
        if (mDialog == null) {
            return;
        }
        mDialog.cancel();
    }

    private void setupView() {
        final ViewGroup parentPanel = (ViewGroup) mWindow.findViewById(R.id.ll_parentPanel);
        rl_topPanel = (RelativeLayout) parentPanel.findViewById(R.id.rl_topPanel);
        rl_alertContent = (RelativeLayout) parentPanel.findViewById(R.id.rl_alertContent);
        ll_buttonPanel = (LinearLayout) parentPanel.findViewById(R.id.ll_buttonPanel);
        setupContent(rl_alertContent);
        setupButtons(ll_buttonPanel);
        setupTitle(rl_topPanel);
    }

    private void setupTitle(ViewGroup topPanel) {
        topPanel.setVisibility(mShowTitleView ? View.VISIBLE : View.GONE);
        if (!mShowTitleView) {
            return;
        }
        tv_title = (TextView) topPanel.findViewById(R.id.tv_title);
        if (!TextUtils.isEmpty(mTitle)) {
            tv_title.setText(mTitle);
        }

        iv_icon = (ImageView) topPanel.findViewById(R.id.iv_icon);
        if (mIconId != 0) {
            iv_icon.setImageResource(mIconId);
        } else if (mIcon != null) {
            iv_icon.setImageDrawable(mIcon);
        } else {
            iv_icon.setVisibility(View.GONE);
        }
    }

    private void setupButtons(ViewGroup buttonPanel) {
        btn_negative = (Button) buttonPanel.findViewById(R.id.btn_negative);
        btn_negative.setVisibility(mNegativeButtonVisibility ? View.VISIBLE : View.GONE);
        if (mNegativeButtonVisibility) {
            btn_negative.setOnClickListener(mButtonHandler);
            if (!TextUtils.isEmpty(mButtonNegativeText)) {
                btn_negative.setText(mButtonNegativeText);
            }
        }

        btn_positive = (Button) buttonPanel.findViewById(R.id.btn_positive);
        btn_positive.setVisibility(mPositiveButtonVisibility ? View.VISIBLE : View.GONE);
        if (mPositiveButtonVisibility) {
            btn_positive.setOnClickListener(mButtonHandler);
            if (!TextUtils.isEmpty(mButtonPositiveText)) {
                btn_positive.setText(mButtonPositiveText);
            }
        }
        if (!(mNegativeButtonVisibility || mPositiveButtonVisibility)) {
            buttonPanel.setVisibility(View.GONE);
        }
    }

    private void setupContent(ViewGroup contentPanel) {
        tv_contentText = (TextView) contentPanel.findViewById(R.id.tv_contentText);
        if (mContentView != null) {
            tv_contentText.setVisibility(View.GONE);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.CENTER_IN_PARENT);
            rl_alertContent.addView(mContentView, params);
        } else {
            tv_contentText.setVisibility(View.VISIBLE);
            tv_contentText.setText(mMessage);
        }
    }

    public void installContent() {
        final int contentView = selectContentView();
        mDialog.setContentView(contentView);
        setupView();
    }

    private int selectContentView() {
        return R.layout.alert_dialog;
    }

    public static class AlertParams {
        public Context mContext;
        public int mThemeResId;
        /**
         * 点击空白是否能取消
         */
        public boolean mCancelable = true;
        /**
         * 布局View
         */
        public View mContentView;
        /**
         * 宽度
         */
        public int mWidth = ViewGroup.LayoutParams.WRAP_CONTENT;

        public int mHeight = ViewGroup.LayoutParams.WRAP_CONTENT;

        public int mGravity = Gravity.CENTER;
        /**
         * 动画
         */
        public int mAnimations = 0;
        /**
         * 是否显示头布局
         */
        public boolean mShowTitleView = true;
        /**
         * 标题文字
         */
        public CharSequence mTitle;
        /**
         * 内容文字
         */
        public CharSequence mMessage;
        /**
         * 取消的按钮文字
         */
        public CharSequence mButtonNegativeText;
        /**
         * 取消的监听
         */
        public DialogInterface.OnClickListener mNegativeButtonListener;
        /**
         * 取消按钮是否显示
         */
        public boolean mNegativeButtonVisibility = true;
        /**
         * 确认的文字
         */
        public CharSequence mButtonPositiveText;
        /**
         * 确认的监听
         */
        public DialogInterface.OnClickListener mPositiveButtonListener;
        /**
         * 确认按钮是否显示
         */
        public boolean mPositiveButtonVisibility = true;
        /**
         * 显示的监听
         */
        public DialogInterface.OnShowListener mOnShowListener;
        public DialogInterface.OnCancelListener mOnCancelListener;
        public DialogInterface.OnDismissListener mOnDismissListener;
        public DialogInterface.OnKeyListener mOnKeyListener;


        public AlertParams(Context context, int themeResId) {
            this.mContext = context;
            this.mThemeResId = themeResId;
        }

        /**
         * 绑定和设置参数
         *
         * @param alert
         */
        public void apply(final AlertController alert) {
            Window window = alert.getWindow();
            //设置方向
            window.setGravity(mGravity);
            //设置动画
            if (mAnimations != 0) {
                window.setWindowAnimations(mAnimations);
            }
            //设置宽高
            WindowManager.LayoutParams params = window.getAttributes();
            params.width = mWidth;
            params.height = mHeight;
            window.setAttributes(params);
            if (mPositiveButtonListener != null) {
                alert.setButton(DialogInterface.BUTTON_POSITIVE, mButtonPositiveText,
                        mPositiveButtonListener, null);
            }
            if (mNegativeButtonListener != null) {
                alert.setButton(DialogInterface.BUTTON_NEGATIVE, mButtonNegativeText,
                        mNegativeButtonListener, null);
            }
            alert.mNegativeButtonVisibility = mNegativeButtonVisibility;
            alert.mPositiveButtonVisibility = mPositiveButtonVisibility;
//            if (mIcon != null) {
//                alert.setIcon(mIcon);
//            }
//            if (mIconId != 0) {
//                alert.setIcon(mIconId);
//            }
//            if (mIconAttrId != 0) {
//                alert.setIcon(dialog.getIconAttributeResId(mIconAttrId));
//            }

            if (mShowTitleView) {
                if (mTitle != null) {
                    alert.setTitle(mTitle);
                }
            } else {
                alert.showTitleView(false);
            }
            if (mContentView != null) {
                alert.setAlertContentView(mContentView);
            } else {
                if (mMessage != null) {
                    alert.setMessage(mMessage);
                }
            }

        }
    }
}
