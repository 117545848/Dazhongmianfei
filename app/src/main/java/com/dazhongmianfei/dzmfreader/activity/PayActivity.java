package com.dazhongmianfei.dzmfreader.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dazhongmianfei.dzmfreader.R;
import com.dazhongmianfei.dzmfreader.alipay.AlipayGoPay;
import com.dazhongmianfei.dzmfreader.config.ReaderConfig;
import com.dazhongmianfei.dzmfreader.utils.LanguageUtil;
import com.dazhongmianfei.dzmfreader.utils.Utils;
import com.dazhongmianfei.dzmfreader.wxpay.WXGoPay;
import com.dazhongmianfei.dzmfreader.wxpay.WXPayResult;

import butterknife.BindView; import com.dazhongmianfei.dzmfreader.R2;

/**
 * 支付页面，可选择微信支付和支付宝支付
 * Created by scb on 2018/8/9.
 */
public class PayActivity extends BaseActivity implements ShowTitle, View.OnClickListener {
    private final String TAG = PayActivity.class.getSimpleName();
  @BindView(R2.id.weixin_pay_layout)
    RelativeLayout weixin_pay_layout;
  @BindView(R2.id.alipay_pay_layout)
    RelativeLayout alipay_pay_layout;
  @BindView(R2.id.pay_confirm_btn)
    Button pay_confirm_btn;
  @BindView(R2.id.weixin_paytype_img)
    ImageView weixin_paytype_img;
  @BindView(R2.id.alipay_paytype_img)
    ImageView alipay_paytype_img;
    /**
     * 0：微信支付 1：支付宝支付
     */
    private int mPayType = 0;

    private String mGoodsId;
    private String mPrice;
    public  static Activity activity;
    @Override
    public int initContentView() {
        return R.layout.activity_pay;
    }

    @Override
    public void initView() {
        activity=this;
        initTitleBarView(LanguageUtil.getString(activity, R.string.PayActivity_title));
        weixin_paytype_img.setImageResource(R.mipmap.pay_selected);
        alipay_paytype_img.setImageResource(R.mipmap.pay_unselected);
        weixin_pay_layout.setOnClickListener(this);
        alipay_pay_layout.setOnClickListener(this);
        pay_confirm_btn.setOnClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //此处先设置一下微信的支付结果标识
        WXPayResult.WXPAY_CODE = 1;
    }

    @Override
    public void initData() {
        mGoodsId = getIntent().getStringExtra("goods_id");
        mPrice = getIntent().getStringExtra("price");
        pay_confirm_btn.setText(LanguageUtil.getString(activity, R.string.PayActivity_querenzhifu)+ mPrice);
    }

    @Override
    public void initInfo(String json) {
        super.initInfo(json);
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
            case R.id.weixin_pay_layout:
                mPayType = 0;
                weixin_paytype_img.setImageResource(R.mipmap.pay_selected);
                alipay_paytype_img.setImageResource(R.mipmap.pay_unselected);
                break;
            case R.id.alipay_pay_layout:
                mPayType = 1;
                weixin_paytype_img.setImageResource(R.mipmap.pay_unselected);
                alipay_paytype_img.setImageResource(R.mipmap.pay_selected);
                break;
            case R.id.pay_confirm_btn:
                if (!Utils.isLogin(this)) {
                    startActivity(new Intent(this, LoginActivity.class));
                    return;
                }
                if (mPayType == 0) {
                    WXGoPay wxGoPay = new WXGoPay(this);
                    wxGoPay.requestPayOrder(ReaderConfig.mWXPayUrl, mGoodsId);
                } else if (mPayType == 1) {
                    AlipayGoPay alipayGoPay = new AlipayGoPay(this);
                    alipayGoPay.requestPayOrder(ReaderConfig.mAlipayUrl, mGoodsId);
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (WXPayResult.WXPAY_CODE == 0) {
//            backToHome();
        }
    }
}
