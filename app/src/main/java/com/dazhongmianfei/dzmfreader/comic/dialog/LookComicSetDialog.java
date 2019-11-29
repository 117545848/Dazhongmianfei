package com.dazhongmianfei.dzmfreader.comic.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.dazhongmianfei.dzmfreader.R;
import com.dazhongmianfei.dzmfreader.config.ReaderConfig;
import com.dazhongmianfei.dzmfreader.read.util.BrightnessUtil;
import com.dazhongmianfei.dzmfreader.utils.AppPrefs;
import com.dazhongmianfei.dzmfreader.utils.ScreenSizeUtils;
import com.zcw.togglebutton.ToggleButton;

public class LookComicSetDialog {

    public static void getLookComicSetDialog(Activity activity) {
        Dialog bottomDialog = new Dialog(activity, R.style.BottomDialog);
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_lookcomicset, null);
        ToggleButton dialog_lookcomicset_fanye_ToggleButton = view.findViewById(R.id.dialog_lookcomicset_fanye_ToggleButton);
        ToggleButton dialog_lookcomicset_yejian_ToggleButton = view.findViewById(R.id.dialog_lookcomicset_yejian_ToggleButton);

        if (AppPrefs.getSharedBoolean(activity, "fanye_ToggleButton", true)) {
            dialog_lookcomicset_fanye_ToggleButton.setToggleOn();
        } else {
            dialog_lookcomicset_fanye_ToggleButton.setToggleOff();
        }
        if (AppPrefs.getSharedBoolean(activity, "yejian_ToggleButton", false)) {
            dialog_lookcomicset_yejian_ToggleButton.setToggleOn();
        } else {
            dialog_lookcomicset_yejian_ToggleButton.setToggleOff();
        }
        dialog_lookcomicset_fanye_ToggleButton.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                AppPrefs.putSharedBoolean(activity, "fanye_ToggleButton", on);
            }
        });
        dialog_lookcomicset_yejian_ToggleButton.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                AppPrefs.putSharedBoolean(activity, "yejian_ToggleButton", on);
                if(on){
                    BrightnessUtil.setBrightness(activity, 0);
                }else {
                    BrightnessUtil.setBrightness(activity, 255);
                }
            }
        });
        bottomDialog.setContentView(view);
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        params.width = ScreenSizeUtils.getInstance(activity).getScreenWidth();
        view.setLayoutParams(params);
        bottomDialog.getWindow().setGravity(Gravity.BOTTOM);
        bottomDialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
        bottomDialog.show();
        bottomDialog.onWindowFocusChanged(false);
        bottomDialog.setCanceledOnTouchOutside(true);

    }

}
