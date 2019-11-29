package com.dazhongmianfei.dzmfreader.read.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.SeekBar;

import com.dazhongmianfei.dzmfreader.R;
import com.dazhongmianfei.dzmfreader.read.ReadActivity;
import com.dazhongmianfei.dzmfreader.read.ReadingConfig;

import butterknife.BindView; import com.dazhongmianfei.dzmfreader.R2;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 自动阅读设置
 */
public class AutoSettingDialog extends Dialog {

  @BindView(R2.id.auto_setting_seekBar)
    SeekBar auto_setting_seekBar;

  @BindView(R2.id.auto_setting_exit)
    View auto_setting_exit;

    private ReadActivity mContext;
    private ReadingConfig mConfig;
    private int mAutoSpeed;
    private boolean isChanged = false;
    private SettingDialog mSettingDialog;

    private AutoSettingDialog(Context context, boolean flag, OnCancelListener listener) {
        super(context, flag, listener);
    }

    public AutoSettingDialog(Context context) {
        this(context, R.style.setting_dialog);
        mContext = (ReadActivity) context;
    }

    public AutoSettingDialog(Context context, int themeResId) {
        super(context, themeResId);

    }

    public void setSettingDialog(SettingDialog dialog) {
        mSettingDialog = dialog;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setGravity(Gravity.BOTTOM);
        setContentView(R.layout.dialog_auto_setting);
        // 初始化View注入
        ButterKnife.bind(this);

        WindowManager m = getWindow().getWindowManager();
        Display d = m.getDefaultDisplay();
        WindowManager.LayoutParams p = getWindow().getAttributes();
        p.width = d.getWidth();
        getWindow().setAttributes(p);


        mConfig = ReadingConfig.getInstance();

        auto_setting_seekBar.setMax(55);

        mAutoSpeed = mConfig.getAutoSpeed();
        auto_setting_seekBar.setProgress(60 - mAutoSpeed);

        auto_setting_seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                isChanged = true;
                if (progress <= 5) {
                    progress = 0;
                } else if (progress <= 10) {
                    progress = 5;
                } else if (progress <= 15) {
                    progress = 10;
                } else if (progress <= 20) {
                    progress = 15;
                } else if (progress <= 25) {
                    progress = 20;
                } else if (progress <= 30) {
                    progress = 25;
                } else if (progress <= 35) {
                    progress = 30;
                } else if (progress <= 40) {
                    progress = 35;
                } else if (progress <= 45) {
                    progress = 40;
                } else if (progress <= 50) {
                    progress = 45;
                } else if (progress <= 52) {
                    progress = 50;
                } else {
                    progress = 55;
                }
                mConfig.setAutoSpeed(60 - progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {

                if (AutoProgress.getInstance().isStop()) {
                    return;
                }

                AutoProgress.getInstance().setTime(ReadingConfig.getInstance().getAutoSpeed() * 1000);

                if (isChanged) {
                    AutoProgress.getInstance().restart();
                } else {
                    AutoProgress.getInstance().goStill();
                }
                isChanged = false;
                hideSystemUI();
            }
        });

    }

    public Boolean isShow() {
        return isShowing();
    }

    @OnClick({R.id.auto_setting_exit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.auto_setting_exit:
                if (AutoProgress.getInstance().isStarted() && !AutoProgress.getInstance().isStop()) {
                    AutoProgress.getInstance().stop();
                    dismiss();
                    hideSystemUI();
                }

                if (mSettingDialog != null && mSettingDialog.isShowing()) {
                    mSettingDialog.dismiss();
                }
                break;

        }
    }

    @Override
    public void show() {
        super.show();
        auto_setting_seekBar.setProgress(60 - mConfig.getAutoSpeed());
    }

    private void hideSystemUI() {
        // Set the IMMERSIVE flag.
        // Set the content to appear under the system bars so that the content
        // doesn't resize when the system bars hide and show.
        mContext.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );
    }


}