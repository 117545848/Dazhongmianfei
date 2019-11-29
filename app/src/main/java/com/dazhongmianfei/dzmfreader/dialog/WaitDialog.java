package com.dazhongmianfei.dzmfreader.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;


import com.wang.avi.AVLoadingIndicatorView;
import com.wang.avi.indicators.BallSpinFadeLoaderIndicator;
import com.wang.avi.indicators.LineSpinFadeLoaderIndicator;
import com.dazhongmianfei.dzmfreader.R;

import java.io.Serializable;


public class WaitDialog extends Dialog implements Serializable {

    private Dialog waitDialog = null;
    private TextView tv;
/*
    pd = new ProgressDialog(this);
    pd.setTitle("登录中...");
    pd.setMessage("请稍等...");
//        pd.setCancelable(false);
    pd.setCanceledOnTouchOutside(false);*/


    public WaitDialog(Context context) {
        super(context);
        if (waitDialog == null) {
            waitDialog = new Dialog(context, R.style.progress_dialog);
        }
        waitDialog.setContentView(R.layout.dialog_httping);
        waitDialog.setCancelable(true);
        waitDialog.setCanceledOnTouchOutside(false);
        waitDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        tv = waitDialog.findViewById(R.id.id_tv_loadingmsg);
        AVLoadingIndicatorView avLoadingIndicatorView= waitDialog.findViewById(R.id.dialog_httping_AVLoadingIndicatorView);
        avLoadingIndicatorView.setIndicator(new BallSpinFadeLoaderIndicator());
        avLoadingIndicatorView.setVisibility(View.VISIBLE);
    }

    public WaitDialog(Context context, boolean flag) {
        super(context);
        if (waitDialog == null) {
            waitDialog = new Dialog(context, R.style.progress_dialog);
        }
        waitDialog.setContentView(R.layout.dialog_httping);

        waitDialog.setCanceledOnTouchOutside(false);
        waitDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        tv = waitDialog.findViewById(R.id.id_tv_loadingmsg);
        waitDialog.setCancelable(flag);

    }



    public void setMessage(String message) {
        if (tv == null) {
            tv = waitDialog.findViewById(R.id.id_tv_loadingmsg);
        }
        tv.setText(message);
    }

    public void showDailog() {
        if (tv != null) {
            if (TextUtils.equals(tv.getText().toString(), "") || TextUtils.equals(tv.getText().toString(), null)) {
                tv.setVisibility(View.GONE);
            }
        }
        try {
            waitDialog.show();
        } catch (Exception e) {
        }

    }

    public void dismissDialog() {
        if (null != waitDialog && waitDialog.isShowing()) {
            try {//如果该对话框依附的Activity已经消失 调用dismiss(); 会参数异常
                waitDialog.dismiss();

            } catch (Exception E) {
            }
        }
        waitDialog=null;
    }

    public void ShowDialog(boolean isShow) {
        if (isShow) {
            this.showDailog();
        } else {
            if (waitDialog != null) {
                this.dismissDialog();
            }
        }
    }

    public void setCancleable(boolean enable) {
        waitDialog.setCancelable(enable);
    }

}
