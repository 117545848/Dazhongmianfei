package com.dazhongmianfei.dzmfreader.activity;

import android.graphics.Color;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dazhongmianfei.dzmfreader.R;
import com.dazhongmianfei.dzmfreader.activity.presenter.BindPresenter;
import com.dazhongmianfei.dzmfreader.activity.view.LoginView;
import com.dazhongmianfei.dzmfreader.utils.LanguageUtil;

import butterknife.BindView; import com.dazhongmianfei.dzmfreader.R2;

/**
 * Created by scb on 2018/8/10.
 */
public class BindPhoneActivity extends BaseActivity implements ShowTitle, LoginView, View.OnClickListener {

  @BindView(R2.id.activity_bind_phone_text)
    EditText activity_bind_phone_text;
  @BindView(R2.id.activity_bind_phone_clear)
    ImageView activity_bind_phone_clear;

  @BindView(R2.id.activity_bind_phone_message)
    EditText activity_bind_phone_message;
  @BindView(R2.id.activity_bind_phone_get_message_btn)
    Button activity_bind_phone_get_message_btn;

  @BindView(R2.id.activity_bind_phone_btn)
    Button activity_bind_phone_btn;

    private BindPresenter mPresenter;

    @Override
    public int initContentView() {
        return R.layout.activity_bind_phone;
    }

    @Override
    public void initView() {
        initTitleBarView(LanguageUtil.getString(this, R.string.BindPhoneActivity_title));

        activity_bind_phone_get_message_btn.setEnabled(false);
        activity_bind_phone_btn.setEnabled(false);
        activity_bind_phone_message.setEnabled(false);

        activity_bind_phone_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    activity_bind_phone_get_message_btn.setEnabled(true);
                    activity_bind_phone_get_message_btn.setTextColor(Color.RED);
                    activity_bind_phone_message.setEnabled(true);
                    activity_bind_phone_clear.setVisibility(View.VISIBLE);

                    if (!TextUtils.isEmpty(activity_bind_phone_message.getText().toString())) {
                        activity_bind_phone_btn.setEnabled(true);
                        activity_bind_phone_btn.setBackgroundResource(R.drawable.shape_login_enable_bg);
                        activity_bind_phone_btn.setTextColor(Color.WHITE);
                    } else {
                        activity_bind_phone_btn.setEnabled(false);
                        activity_bind_phone_btn.setBackgroundResource(R.drawable.shape_login_bg);
                        activity_bind_phone_btn.setTextColor(Color.GRAY);
                    }


                } else {
                    activity_bind_phone_get_message_btn.setEnabled(false);
                    activity_bind_phone_get_message_btn.setTextColor(Color.parseColor("#D3D3D3"));
                    activity_bind_phone_message.setEnabled(false);
                    activity_bind_phone_clear.setVisibility(View.GONE);

                    activity_bind_phone_btn.setEnabled(false);
                    activity_bind_phone_btn.setBackgroundResource(R.drawable.shape_login_bg);
                    activity_bind_phone_btn.setTextColor(Color.GRAY);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        activity_bind_phone_message.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    activity_bind_phone_btn.setEnabled(true);
                    activity_bind_phone_btn.setBackgroundResource(R.drawable.shape_login_enable_bg);
                    activity_bind_phone_btn.setTextColor(Color.WHITE);
                } else {
                    activity_bind_phone_btn.setEnabled(false);
                    activity_bind_phone_btn.setBackgroundResource(R.drawable.shape_login_bg);
                    activity_bind_phone_btn.setTextColor(Color.GRAY);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        activity_bind_phone_text.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    activity_bind_phone_clear.setVisibility(View.GONE);
                } else {
                    if (!TextUtils.isEmpty(activity_bind_phone_text.getText().toString())) {
                        activity_bind_phone_clear.setVisibility(View.VISIBLE);
                    } else {
                        activity_bind_phone_clear.setVisibility(View.GONE);
                    }
                }
            }
        });


        activity_bind_phone_get_message_btn.setOnClickListener(this);
        activity_bind_phone_clear.setOnClickListener(this);
        activity_bind_phone_btn.setOnClickListener(this);

    }

    @Override
    public void initData() {
        mPresenter = new BindPresenter(this);
    }

    @Override
    public void initTitleBarView(String text) {
        LinearLayout mBack;
        TextView mTitle;
        mBack = findViewById(R.id.titlebar_back);
        mTitle = findViewById(R.id.titlebar_text);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTitle.setText(text);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_bind_phone_get_message_btn:
                mPresenter.getMessage();
                break;
            case R.id.activity_bind_phone_btn:
                mPresenter.bindPhone();
                break;
            case R.id.activity_bind_phone_clear:
                activity_bind_phone_text.setText("");
                break;


        }
    }

    @Override
    public String getUserName() {
        return null;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getPhoneNum() {
        return activity_bind_phone_text.getText().toString();
    }

    @Override
    public String getMessage() {
        return activity_bind_phone_message.getText().toString();
    }

    @Override
    public View getButtonView() {
        return activity_bind_phone_get_message_btn;
    }

    @Override
    protected void onDestroy() {
        mPresenter.cancelCountDown();
        super.onDestroy();
    }
}
