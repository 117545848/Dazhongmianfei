package com.dazhongmianfei.dzmfreader.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.dazhongmianfei.dzmfreader.activity.PayActivity;
import com.dazhongmianfei.dzmfreader.activity.RechargeActivity;
import com.dazhongmianfei.dzmfreader.eventbus.RefreshMine;
import com.dazhongmianfei.dzmfreader.utils.LanguageUtil;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.dazhongmianfei.dzmfreader.R;
import com.dazhongmianfei.dzmfreader.config.ReaderConfig;
import com.dazhongmianfei.dzmfreader.utils.MyToash;
import com.dazhongmianfei.dzmfreader.utils.ShareUitls;
import com.dazhongmianfei.dzmfreader.utils.Utils;
import com.dazhongmianfei.dzmfreader.wxpay.WXPayResult;

import org.greenrobot.eventbus.EventBus;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 微信支付结果显示
 */
public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {
    private static final String TAG = WXPayEntryActivity.class.getSimpleName();

    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_result);

        api = WXAPIFactory.createWXAPI(this, ReaderConfig.WEIXIN_PAY_APPID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        //  Utils.showLoadingDialog(this);

        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            if (resp.errCode == 0) {
                //支付成功
                WXPayResult.WXPAY_CODE = 0;

                MyToash.ToashSuccess(WXPayEntryActivity.this, LanguageUtil.getString(WXPayEntryActivity.this, R.string.PayActivity_zhifuok));

                if (PayActivity.activity != null) {
                    PayActivity.activity.finish();
                }

                if (RechargeActivity.activity != null) {
                    RechargeActivity.activity.finish();
                }
                EventBus.getDefault().post(new RefreshMine(null));
            } else if (resp.errCode == -1) {

                WXPayResult.WXPAY_CODE = -1;

                MyToash.ToashError(WXPayEntryActivity.this, LanguageUtil.getString(WXPayEntryActivity.this, R.string.PayActivity_zhifucuowu));

            } else if (resp.errCode == -2) {
                WXPayResult.WXPAY_CODE = -2;

                MyToash.ToashError(WXPayEntryActivity.this, LanguageUtil.getString(WXPayEntryActivity.this, R.string.PayActivity_zhifucancle));

            }
            finish();


        }
    }
}