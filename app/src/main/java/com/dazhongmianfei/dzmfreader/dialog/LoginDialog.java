package com.dazhongmianfei.dzmfreader.dialog;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.dazhongmianfei.dzmfreader.R;
import com.dazhongmianfei.dzmfreader.activity.AboutActivity;
import com.dazhongmianfei.dzmfreader.activity.LoginActivity;
import com.dazhongmianfei.dzmfreader.activity.presenter.LoginPresenter;
import com.dazhongmianfei.dzmfreader.activity.view.LoginView;
import com.dazhongmianfei.dzmfreader.config.ReaderConfig;
import com.dazhongmianfei.dzmfreader.utils.ScreenSizeUtils;

/**
 * Created by Administrator on 2018/7/6.
 */

public class LoginDialog {

    //  private LoginPresenter mPresenter;
    // Activity activity;

    public LoginDialog(Activity activity) {
        //   this.activity = activity;
    }

    public interface GetEditTextDialogInterface {
        void getText(String text);
    }

    static PopupWindow popupWindow;

    public static void LoginDialog(final Activity activity, View v, final LoginActivity.LoginSuccess loginSuccess) {
        if (true) {
            activity.startActivity(new Intent(activity,LoginActivity.class));
            return;
        }
        if (popupWindow != null && popupWindow.isShowing()) {
            return;
        }
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_login, null);
        popupWindow = new PopupWindow(view, ScreenSizeUtils.getInstance(activity).getScreenWidth(), ScreenSizeUtils.getInstance(activity).getScreenHeight(), true);
        final EditText activity_login_phone_username = view.findViewById(R.id.activity_login_phone_username);
        final EditText activity_login_phone_message = view.findViewById(R.id.activity_login_phone_message);
        final Button activity_login_phone_get_message_btn = view.findViewById(R.id.activity_login_phone_get_message_btn);
        final Button activity_login_phone_btn = view.findViewById(R.id.activity_login_phone_btn);
        final ImageView activity_login_phone_clear = view.findViewById(R.id.activity_login_phone_clear);

        LoginView loginView = new LoginView() {
            @Override
            public String getUserName() {
                return "";
            }

            @Override
            public String getPassword() {
                return "";
            }

            @Override
            public String getPhoneNum() {
                return activity_login_phone_username.getText().toString();
            }

            @Override
            public String getMessage() {
                return activity_login_phone_message.getText().toString();
            }

            @Override
            public View getButtonView() {
                return activity_login_phone_get_message_btn;
            }

        };
        final LoginPresenter mPresenter = new LoginPresenter(loginView, activity) {
        };
        activity_login_phone_get_message_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.getMessage();
            }
        });
        activity_login_phone_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.loginPhone(loginSuccess);
                popupWindow.dismiss();
            }
        });
        activity_login_phone_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity_login_phone_username.setText("");
            }
        });

        activity_login_phone_get_message_btn.setEnabled(false);
        activity_login_phone_btn.setEnabled(false);
        activity_login_phone_message.setEnabled(false);

        activity_login_phone_username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    activity_login_phone_get_message_btn.setEnabled(true);
                    activity_login_phone_get_message_btn.setTextColor(Color.parseColor("#E7554F"));
                    activity_login_phone_message.setEnabled(true);
                    activity_login_phone_clear.setVisibility(View.VISIBLE);

                    if (!TextUtils.isEmpty(activity_login_phone_message.getText().toString())) {
                        activity_login_phone_btn.setEnabled(true);
                        activity_login_phone_btn.setBackgroundResource(R.drawable.shape_login_enable_bg);
                        activity_login_phone_btn.setTextColor(Color.WHITE);
                    } else {
                        activity_login_phone_btn.setEnabled(false);
                        activity_login_phone_btn.setBackgroundResource(R.drawable.shape_login_bg);
                        activity_login_phone_btn.setTextColor(Color.GRAY);
                    }


                } else {
                    activity_login_phone_get_message_btn.setEnabled(false);
                    activity_login_phone_get_message_btn.setTextColor(Color.parseColor("#D3D3D3"));
                    activity_login_phone_message.setEnabled(false);
                    activity_login_phone_clear.setVisibility(View.GONE);

                    activity_login_phone_btn.setEnabled(false);
                    activity_login_phone_btn.setBackgroundResource(R.drawable.shape_login_bg);
                    activity_login_phone_btn.setTextColor(Color.GRAY);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        activity_login_phone_message.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    activity_login_phone_btn.setEnabled(true);
                    activity_login_phone_btn.setBackgroundResource(R.drawable.shape_login_enable_bg);
                    activity_login_phone_btn.setTextColor(Color.WHITE);
                } else {
                    activity_login_phone_btn.setEnabled(false);
                    activity_login_phone_btn.setBackgroundResource(R.drawable.shape_login_bg);
                    activity_login_phone_btn.setTextColor(Color.GRAY);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        ImageView diaolg_login_close = view.findViewById(R.id.diaolg_login_close);
        LinearLayout diaolg_login_weixin = view.findViewById(R.id.diaolg_login_weixin);
        //  LinearLayout diaolg_login_qq = view.findViewById(R.id.diaolg_login_qq);
        view.findViewById(R.id.activity_login_contract).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                activity.startActivity(new Intent(activity, AboutActivity.class).putExtra("url", ReaderConfig.privacy).putExtra("flag", "privacy"));

            }
        });

        diaolg_login_close.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                popupWindow.dismiss();
                loginSuccess.cancle();
            }
        });
        diaolg_login_weixin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
              //  LoginActivity.weixinLogin(activity, false, loginSuccess);
                popupWindow.dismiss();
            }
        });
      /*  diaolg_login_qq.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                LoginActivity.qqLogin(activity, false, loginSuccess);
                popupWindow.dismiss();
            }
        });*/


        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(false);
        popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);


    }

}
