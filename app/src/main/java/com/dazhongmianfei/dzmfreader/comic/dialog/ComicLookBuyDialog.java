package com.dazhongmianfei.dzmfreader.comic.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dazhongmianfei.dzmfreader.R;
import com.dazhongmianfei.dzmfreader.read.util.BrightnessUtil;
import com.dazhongmianfei.dzmfreader.utils.AppPrefs;
import com.dazhongmianfei.dzmfreader.utils.ScreenSizeUtils;
import com.zcw.togglebutton.ToggleButton;

public class ComicLookBuyDialog {

    public static void getLookComicSetDialog(Activity activity) {
        Dialog bottomDialog = new Dialog(activity, R.style.BottomDialog);
        View view = LayoutInflater.from(activity).inflate(R.layout.view_readactivity_buy, null);

       TextView  view_comiclook_buy_surebuy=view.findViewById(R.id.view_comiclook_buy_surebuy);
        TextView  view_comiclook_buy_somebuy=view.findViewById(R.id.view_comiclook_buy_somebuy);

        view_comiclook_buy_surebuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        view_comiclook_buy_somebuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
