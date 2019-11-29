package com.dazhongmianfei.dzmfreader.activity;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import com.dazhongmianfei.dzmfreader.R;
import com.dazhongmianfei.dzmfreader.adapter.RechargeAdapter;
import com.dazhongmianfei.dzmfreader.alipay.AlipayGoPay;
import com.dazhongmianfei.dzmfreader.bean.RechargeItem;
import com.dazhongmianfei.dzmfreader.config.ReaderConfig;
import com.dazhongmianfei.dzmfreader.http.ReaderParams;
import com.dazhongmianfei.dzmfreader.utils.HttpUtils;
import com.dazhongmianfei.dzmfreader.utils.LanguageUtil;
import com.dazhongmianfei.dzmfreader.utils.Utils;
import com.dazhongmianfei.dzmfreader.view.GridViewForScrollView;
import com.dazhongmianfei.dzmfreader.wxpay.WXGoPay;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
//.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView; import com.dazhongmianfei.dzmfreader.R2;

import static com.dazhongmianfei.dzmfreader.config.ReaderConfig.getCurrencyUnit;

/**
 * 充值页面
 * Created by scb on 2018/8/12.
 */
public class RechargeActivity extends BaseActivity implements ShowTitle, AdapterView.OnItemClickListener, View.OnClickListener {
    private final String TAG = RechargeActivity.class.getSimpleName();

    @BindView(R2.id.activity_recharge_tips)
    TextView activity_recharge_tips;
    @BindView(R2.id.activity_recharge_keyong)
    TextView activity_recharge_keyong;
    @BindView(R2.id.activity_recharge_keyong_unit)
    TextView activity_recharge_keyong_unit;

    @BindView(R2.id.activity_recharge_gridview)
    GridViewForScrollView activity_recharge_gridview;
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

    List<RechargeItem> mList;
    public static Activity activity;
    /**
     * 0：微信支付 1：支付宝支付
     */
    private int mPayType = 0;

    private String mGoodsId;
    private String mPrice;
    RechargeAdapter adapter;

    @Override
    public int initContentView() {
        return R.layout.activity_rechargenew;
    }

    @Override
    public void initView() {
        activity = this;
        weixin_paytype_img.setImageResource(R.mipmap.pay_selected);
        alipay_paytype_img.setImageResource(R.mipmap.pay_unselected);
        weixin_pay_layout.setOnClickListener(this);
        alipay_pay_layout.setOnClickListener(this);
        pay_confirm_btn.setOnClickListener(this);
    }

    @Override
    public void initData() {
        ReaderParams params = new ReaderParams(this);
        String json = params.generateParamsJson();
        HttpUtils.getInstance(this).sendRequestRequestParams3(ReaderConfig.mPayRechargeCenterUrl,json, true, new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(final String result) {

                        initInfo(result);
                    }

                    @Override
                    public void onErrorResponse(String  ex) {

                    }
                }

        );
    }

    @Override
    public void initInfo(String json) {
        super.initInfo(json);
        Utils.hideLoadingDialog();
        try {
            Gson gson = new Gson();
            JSONObject obj = new JSONObject(json);
            String title = obj.getString("title");
            String tips = obj.getString("tips");
            initTitleBarView(title);

            activity_recharge_tips.setText(tips);
            activity_recharge_keyong.setText(obj.getString("goldRemain"));
            activity_recharge_keyong_unit.setText(getCurrencyUnit(activity));
            mList = new ArrayList<>();
            JSONArray array = obj.getJSONArray("items");
            for (int i = 0; i < array.length(); i++) {
                RechargeItem item = gson.fromJson(array.getString(i), RechargeItem.class);
                if (i == 0) {
                    item.choose = true;
                    mPrice = item.getPrice();
                    mGoodsId = item.getGoods_id();
                    pay_confirm_btn.setText(LanguageUtil.getString(activity, R.string.PayActivity_querenzhifu) + mPrice);
                }
                mList.add(item);
            }
            adapter = new RechargeAdapter(this, mList, mList.size());
            activity_recharge_gridview.setAdapter(adapter);

            activity_recharge_gridview.setOnItemClickListener(this);

        } catch (JSONException e) {
            e.printStackTrace();
        }
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        RechargeItem item = mList.get(position);
        for (int i = 0; i < mList.size(); i++) {
            if (i != position) {
                mList.get(i).choose = false;
            } else {
                mList.get(i).choose = true;

            }
        }
        adapter.notifyDataSetChanged();
        mPrice = item.getPrice();
        mGoodsId = item.getGoods_id();
        pay_confirm_btn.setText(LanguageUtil.getString(activity, R.string.PayActivity_querenzhifu) + mPrice);

  /*      if (!Utils.isLogin(this)) {
            startActivity(new Intent(this, LoginActivity.class));
            return;
        }
        Intent intent = new Intent(this, PayActivity.class);
        intent.putExtra("goods_id", mList.get(position).getGoods_id());
        String price = mList.get(position).getPrice();
        if (price.contains("元")) {
            price = price.substring(0, price.lastIndexOf("元"));
        }
        intent.putExtra("price", price);
        startActivity(intent);*/
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
}
