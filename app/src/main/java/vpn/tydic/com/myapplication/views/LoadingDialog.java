package vpn.tydic.com.myapplication.views;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import vpn.tydic.com.myapplication.R;


public class LoadingDialog extends Dialog {
    protected View contentView;
    protected TextView textView;

    /**
     * @param context
     * @param cancelable
     * @param cancelListener
     */
    public LoadingDialog(Context context, boolean cancelable,
                         OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init(context, null);
    }

    /**
     * @param context
     * @param theme
     */
    public LoadingDialog(Context context, int theme) {
        super(context, theme);
        init(context, null);
    }

    /**
     * @param context
     */
    public LoadingDialog(Context context) {
        super(context, R.style.LoadingDialogStyle);
        init(context, null);
    }

    /**
     * @param context
     */
    public LoadingDialog(Context context, String msg) {
        super(context, R.style.LoadingDialogStyle);
        init(context, msg);
    }

    public LoadingDialog(Context context, View contentView) {
        super(context, R.style.LoadingDialogStyle);
        this.contentView = contentView;
        init(context, null);
    }

    public LoadingDialog(Context context, int theme, View contentView) {
        super(context, theme);
        this.contentView = contentView;
        init(context, null);
    }

    public void init(Context context, String text) {
        setCancelable(true);// 不可以用“返回键”取消
        if (contentView == null)
            contentView = LayoutInflater.from(context).inflate(
                    R.layout.dialog_loading, null);
        setContentView(contentView, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局
        textView = (TextView) findViewById(R.id.tv_loading_text);
        if (!TextUtils.isEmpty(text)) {
            textView.setText(text);
            textView.setVisibility(View.VISIBLE);
        }
        setBackgroundColor(context.getResources().getColor(R.color.trans60));
    }

    public void setBackgroundColor(int color) {
        GradientDrawable myGrad = (GradientDrawable) contentView
                .getBackground();
        if (myGrad != null)
            myGrad.setColor(color);
    }

    public LoadingDialog setText(String text) {
        textView.setText(text);
        textView.setVisibility(View.VISIBLE);
        return this;
    }

    public LoadingDialog setText(int strId) {
        textView.setText(this.getContext().getResources().getString(strId));
        return this;
    }

    public View getContentView() {
        return contentView;
    }

    public TextView getTextView() {
        return textView;
    }

}
