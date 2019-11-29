package com.dazhongmianfei.dzmfreader.activity;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dazhongmianfei.dzmfreader.R;
import com.dazhongmianfei.dzmfreader.config.ReaderConfig;
import com.dazhongmianfei.dzmfreader.http.ReaderParams;
import com.dazhongmianfei.dzmfreader.utils.HttpUtils;
import com.dazhongmianfei.dzmfreader.utils.LanguageUtil;
import com.dazhongmianfei.dzmfreader.utils.MyToash;
import com.dazhongmianfei.dzmfreader.utils.Utils;

//.http.RequestParams;


/**
 * 个人中心-意见反馈
 * Created by scb on 2018/7/14.
 */
public class FeedBackActivity extends BaseActivity implements ShowTitle {
    private final String TAG = FeedBackActivity.class.getSimpleName();
    /**
     * 意见内容
     */
    private EditText activity_feedback_content;
    /**
     * 字数百分比
     */
    private TextView activity_feedback_percentage;
    /**
     * "提交"外层布局
     */
    private LinearLayout comment_titlebar_add_feedback;
    /**
     * "提交"
     */
    private TextView titlebar_finish;

    @Override
    public int initContentView() {
        return R.layout.activity_feedback;
    }

    @Override
    public void initView() {
        initTitleBarView(LanguageUtil.getString(this, R.string.FeedBackActivity_title));
        titlebar_finish = findViewById(R.id.titlebar_finish);
        titlebar_finish.setText(LanguageUtil.getString(this, R.string.FeedBackActivity_tijiao));
        activity_feedback_content = findViewById(R.id.activity_feedback_content);
        activity_feedback_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String percentage = "%s/200";
                int lastWordsNum = 200 - s.length();
                activity_feedback_percentage.setText(String.format(percentage, lastWordsNum + ""));
            }
        });
        activity_feedback_percentage = findViewById(R.id.activity_feedback_percentage);
        comment_titlebar_add_feedback = findViewById(R.id.comment_titlebar_add_comment);
        comment_titlebar_add_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFeedback();
            }
        });

    }

    @Override
    public void initData() {

    }

    /**
     * 发请求
     */

    public void addFeedback() {
        if (!Utils.isLogin(FeedBackActivity.this)) {
            MyToash.ToashError(FeedBackActivity.this, LanguageUtil.getString(FeedBackActivity.this, R.string.MineNewFragment_nologin));
            Intent intent = new Intent();
            intent.setClass(FeedBackActivity.this, LoginActivity.class);
            startActivity(intent);
            return;
        }
        if (TextUtils.isEmpty(activity_feedback_content.getText())) {
            MyToash.ToashError(FeedBackActivity.this, LanguageUtil.getString(this, R.string.FeedBackActivity_some));
            return;
        }


        ReaderParams params = new ReaderParams(this);
        params.putExtraParams("content", activity_feedback_content.getText().toString() + "");

        String json = params.generateParamsJson();

        HttpUtils.getInstance(this).sendRequestRequestParams3(ReaderConfig.mFeedbackUrl, json, true, new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(final String result) {

                        initInfo(result);
                    }

                    @Override
                    public void onErrorResponse(String ex) {

                    }
                }

        );

    }

    @Override
    public void initInfo(String json) {
        super.initInfo(json);
        MyToash.ToashSuccess(FeedBackActivity.this, "反馈成功");
        finish();

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


}
