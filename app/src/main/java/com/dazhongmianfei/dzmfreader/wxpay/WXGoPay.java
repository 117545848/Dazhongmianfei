package com.dazhongmianfei.dzmfreader.wxpay;

import android.app.Activity;
import android.content.Context;

import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.dazhongmianfei.dzmfreader.activity.PayActivity;
import com.dazhongmianfei.dzmfreader.config.ReaderConfig;
import com.dazhongmianfei.dzmfreader.pay.ReaderPay;
import com.dazhongmianfei.dzmfreader.utils.Utils;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Created by scb on 2018/8/12.
 */
public class WXGoPay extends ReaderPay {

    private Activity mActivity;

    public WXGoPay(Activity context) {
        super(context);
        mActivity = context;
    }

    /**
     * 调起微信支付
     *
     *
     *
     * "appid": "wx7ea57c51219f0e1b",
     *         "partnerid": "1500768901",
     *         "prepayid": "wx151204298190898399f754190974515304",
     *         "package": "Sign=WXPay",
     *         "noncestr": "d94ef65bb2ebf2a65d2744668453daee",
     *         "timestamp": 1555301069,
     *         "paySign": "5EB541CA369DC9B215802AF979736DDF",
     *         "prepay_id": "wx151204298190898399f754190974515304"
     *
     *
     */

    class  WeixinPay{
        public  String appid;
        public  String partnerid;
        public  String prepayid;
       // public  String package;
        public  String noncestr;
        public  String timestamp;
        public  String paySign;
        public  String prepay_id;
    }
    @Override
    public void startPay(String prepayId) {

        try {

            JSONObject jsonObject=new JSONObject(prepayId);


            IWXAPI msgApi = WXAPIFactory.createWXAPI(mActivity, jsonObject.getString("appid"), false);
            msgApi.registerApp(jsonObject.getString("appid"));
            PayReq req = new PayReq();
            req.appId = jsonObject.getString("appid");
            req.partnerId = jsonObject.getString("partnerid");
            req.packageValue = jsonObject.getString("package");
            req.prepayId =jsonObject.getString("prepayid");
            req.nonceStr = jsonObject.getString("noncestr");
            req.timeStamp = jsonObject.getString("timestamp");
            req.sign = jsonObject.getString("paySign");

          /*  List<NameValuePair> signParams = new LinkedList<NameValuePair>();
            signParams.add(new BasicNameValuePair("appid", req.appId));
            signParams.add(new BasicNameValuePair("noncestr", req.nonceStr));
            signParams.add(new BasicNameValuePair("package", req.packageValue));
            signParams.add(new BasicNameValuePair("partnerid", req.partnerId));
            signParams.add(new BasicNameValuePair("prepayid", req.prepayId));
            signParams.add(new BasicNameValuePair("timestamp", req.timeStamp));
            req.sign = genAppSign(signParams);*/
            msgApi.sendReq(req);
        } catch (Exception E) {
        }
    }

    @Override
    public void handleOrderInfo(String result) {
        startPay(result);

    }


}
