package com.minicart.android.baselibrary.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.annotation.StyleRes;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import com.minicart.android.baselibrary.R;


/**
 * @类名：AlertDialog
 * @描述：
 * @创建人：54506
 * @创建时间：2017/3/15 16:17
 * @版本：
 */

public class AlertDialog extends Dialog {
    private AlertController mController;

    protected AlertDialog(@NonNull Context context) {
        this(context, R.style.BaseLibrary_Dialog);
    }

    protected AlertDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        mController = new AlertController(this, getWindow());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mController.installContent();
    }

    @Override
    public void setTitle(@Nullable CharSequence title) {
        super.setTitle(title);
        mController.setTitle(title);
    }

    public void setMessage(@Nullable CharSequence message) {
        mController.setMessage(message);
    }

    public static class Builder {
        private final AlertController.AlertParams P;

        /**
         * Creates a builder for an alert dialog that uses the default alert
         * dialog theme.
         * <p>
         * The default alert dialog theme is defined by
         * {@link android.R.attr#alertDialogTheme} within the parent
         * {@code context}'s theme.
         *
         * @param context the parent context
         */
        public Builder(@NonNull Context context) {
            this(context, R.style.BaseLibrary_Dialog);
        }

        public Builder(@NonNull Context context, @StyleRes int themeResId) {
            P = new AlertController.AlertParams(context, themeResId);
        }

        public Builder setContentView(View view) {
            P.mContentView = view;
            return this;
        }

        public Builder setTitle(CharSequence title) {
            P.mTitle = title;
            return this;
        }

        public Builder setContentMessage(CharSequence message) {
            P.mMessage = message;
            return this;
        }

        public Builder showTitleView(boolean showTitleView) {
            P.mShowTitleView = showTitleView;
            return this;
        }

        public Builder setNegativeButton(OnClickListener listener) {
            return setNegativeButton(null, listener);
        }

        public Builder setNegativeButton(@StringRes int textId, OnClickListener listener) {
            return setNegativeButton(P.mContext.getText(textId), listener);
        }

        public Builder setNegativeButton(CharSequence text, OnClickListener listener) {
            P.mButtonNegativeText = text;
            P.mNegativeButtonListener = listener;
            return this;
        }

        public Builder setNegativeButtonVisibility(boolean visibility) {
            P.mNegativeButtonVisibility = visibility;
            return this;
        }

        public Builder setPositiveButton(OnClickListener listener) {
            return setPositiveButton(null, listener);
        }

        public Builder setPositiveButton(@StringRes int textId, OnClickListener listener) {
            return setPositiveButton(P.mContext.getText(textId), listener);
        }

        public Builder setPositiveButton(CharSequence text, OnClickListener listener) {
            P.mButtonPositiveText = text;
            P.mPositiveButtonListener = listener;
            return this;
        }


        public Builder setPositiveButtonVisibility(boolean visibility) {
            P.mPositiveButtonVisibility = visibility;
            return this;
        }

        /**
         * Sets whether the dialog is cancelable or not.  Default is true.
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setCancelable(boolean cancelable) {
            P.mCancelable = cancelable;
            return this;
        }

        public Builder fullWidth() {
            P.mWidth = ViewGroup.LayoutParams.MATCH_PARENT;
            return this;
        }

        /**
         * 从底部弹出
         *
         * @param isAnimation 是否有动画
         * @return
         */
        public Builder fromBottom(boolean isAnimation) {
            if (isAnimation) {
                P.mAnimations = R.style.BaseLibrary_Dialog_Animation_FromBottom;
            }
            P.mGravity = Gravity.BOTTOM;
            return this;
        }

        /**
         * @param width
         * @param height
         * @return
         */
        public Builder setWidthAndHeight(int width, int height) {
            P.mWidth = width;
            P.mHeight = height;
            return this;
        }

        public Builder setDefaultAnimation() {
            P.mAnimations = R.style.BaseLibrary_Dialog_Animation_Default;
            return this;
        }

        public Builder setAnimation(@StyleRes int id) {
            P.mAnimations = id;
            return this;
        }

        /**
         * Sets the callback that will be called if the dialog is canceled.
         * <p>
         * <p>Even in a cancelable dialog, the dialog may be dismissed for reasons other than
         * being canceled or one of the supplied choices being selected.
         * If you are interested in listening for all cases where the dialog is dismissed
         * and not just when it is canceled, see
         * {@link #setOnDismissListener(OnDismissListener) setOnDismissListener}.</p>
         *
         * @return This Builder object to allow for chaining of calls to set methods
         * @see #setCancelable(boolean)
         * @see #setOnDismissListener(OnDismissListener)
         */
        public Builder setOnCancelListener(OnCancelListener onCancelListener) {
            P.mOnCancelListener = onCancelListener;
            return this;
        }

        /**
         * Sets the callback that will be called when the dialog is dismissed for any reason.
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setOnDismissListener(OnDismissListener onDismissListener) {
            P.mOnDismissListener = onDismissListener;
            return this;
        }

        /**
         * Sets the callback that will be called if a key is dispatched to the dialog.
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setOnKeyListener(OnKeyListener onKeyListener) {
            P.mOnKeyListener = onKeyListener;
            return this;
        }

        public Builder setOnShowListener(OnShowListener listener) {
            P.mOnShowListener = listener;
            return this;
        }

        /**
         * Creates an {@link AlertDialog} with the arguments supplied to this
         * builder.
         * <p>
         * Calling this method does not display the dialog. If no additional
         * processing is needed, {@link #show()} may be called instead to both
         * create and display the dialog.
         */
        public AlertDialog create() {
            // Context has already been wrapped with the appropriate theme.
            final AlertDialog dialog = new AlertDialog(P.mContext, P.mThemeResId);
            P.apply(dialog.mController);
            dialog.setCancelable(P.mCancelable);
            if (P.mCancelable) {
                dialog.setCanceledOnTouchOutside(true);
            }
            dialog.setOnShowListener(P.mOnShowListener);
            dialog.setOnCancelListener(P.mOnCancelListener);
            dialog.setOnDismissListener(P.mOnDismissListener);
            if (P.mOnKeyListener != null) {
                dialog.setOnKeyListener(P.mOnKeyListener);
            }
            return dialog;
        }

        /**
         * Creates an {@link AlertDialog} with the arguments supplied to this
         * builder and immediately displays the dialog.
         * <p>
         * Calling this method is functionally identical to:
         * <pre>
         *     AlertDialog dialog = builder.create();
         *     dialog.show();
         * </pre>
         */
        public AlertDialog show() {
            final AlertDialog dialog = create();
            dialog.show();
            return dialog;
        }
    }
}
