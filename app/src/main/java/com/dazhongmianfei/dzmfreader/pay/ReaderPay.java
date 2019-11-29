package com.dazhongmianfei.dzmfreader.pay;

import android.app.Activity;


import com.dazhongmianfei.dzmfreader.http.ReaderParams;
import com.dazhongmianfei.dzmfreader.utils.HttpUtils;

//.http.RequestParams;


/**
 * Created by scb on 2018/8/12.
 */
public abstract class ReaderPay implements GoPay {

    public Activity context;

    public ReaderPay(Activity context) {
        this.context = context;
    }

    public void requestPayOrder(String url, String goodsId) {


        ReaderParams params = new ReaderParams(context);
        params.putExtraParams("goods_id", goodsId);

        String json = params.generateParamsJson();




        HttpUtils.getInstance((Activity) context).sendRequestRequestParams3(url, json, true, new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(final  String result) {

                        handleOrderInfo(result);
                    }

                    @Override
                    public void onErrorResponse(String  ex) {

                    }
                }

        );
    }

    public abstract void startPay(String orderInfo);

    public abstract void handleOrderInfo(String result);
}
