package com.dazhongmianfei.dzmfreader.wxapi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;

import com.google.gson.Gson;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.socialize.weixin.view.WXCallbackActivity;
import com.dazhongmianfei.dzmfreader.R;
import com.dazhongmianfei.dzmfreader.activity.FirstStartActivity;
import com.dazhongmianfei.dzmfreader.activity.LoginActivity;
import com.dazhongmianfei.dzmfreader.activity.UserInfoActivity;
import com.dazhongmianfei.dzmfreader.bean.LoginInfo;
;
import com.dazhongmianfei.dzmfreader.config.ReaderConfig;
import com.dazhongmianfei.dzmfreader.eventbus.BuyLoginSuccess;
import com.dazhongmianfei.dzmfreader.eventbus.RefreshBookSelf;
import com.dazhongmianfei.dzmfreader.eventbus.RefreshMine;
import com.dazhongmianfei.dzmfreader.eventbus.RefreshUserInfo;
import com.dazhongmianfei.dzmfreader.http.ReaderParams;
import com.dazhongmianfei.dzmfreader.utils.HttpUtils;
import com.dazhongmianfei.dzmfreader.utils.MyShare;
import com.dazhongmianfei.dzmfreader.utils.MyToash;
import com.dazhongmianfei.dzmfreader.utils.ShareUitls;

import org.greenrobot.eventbus.EventBus;
//.http.RequestParams;


import static com.dazhongmianfei.dzmfreader.config.ReaderConfig.GETPRODUCT_TYPE;
import static com.dazhongmianfei.dzmfreader.config.ReaderConfig.syncDevice;
import static com.dazhongmianfei.dzmfreader.utils.AppPrefs.*;


/**
 * 微信登录相关
 */
public class  WXEntryActivity extends WXCallbackActivity implements IWXAPIEventHandler {
    private  final int RETURN_MSG_TYPE_LOGIN = 1;
    private  final int RETURN_MSG_TYPE_SHARE = 2;
    public  IWXAPI iwxapi;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (Build.VERSION.SDK_INT != Build.VERSION_CODES.O) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        setContentView(R.layout.login_result);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        hintKeyboard();
        iwxapi = WXAPIFactory.createWXAPI(this, ReaderConfig.WEIXIN_PAY_APPID, true);
        iwxapi.handleIntent(getIntent(), this);
    }

    private void hintKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive() && getCurrentFocus() != null) {
            if (getCurrentFocus().getWindowToken() != null) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {

        //    CustomLog.e("getPlatformInfo="+baseResp.errCode);

        //LoginEvent event = new LoginEvent();
        switch (baseResp.errCode) {
            case BaseResp.ErrCode.ERR_BAN:
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
            case BaseResp.ErrCode.ERR_USER_CANCEL:


                if (RETURN_MSG_TYPE_SHARE == baseResp.getType()) {

                } else {

                }
                break;
            case BaseResp.ErrCode.ERR_OK:
                switch (baseResp.getType()) {
                    case RETURN_MSG_TYPE_LOGIN:
                        String code = ((SendAuth.Resp) baseResp).code;
                        getWeiXinAppUserInfo(this,code);
                        break;

                    case RETURN_MSG_TYPE_SHARE:

                      MyShare.getGold(this);

                        break;
                }
                break;
        }


    }

    //该方法执行umeng登陆的回调的处理
    @Override
    public void a(com.umeng.weixin.umengwx.b b) {
//        super.a(b);
    }

    @Override
    protected void a(Intent intent) {
        super.a(intent);
    }

    //在onResume中处理从微信授权通过以后不会自动跳转的问题，手动结束该页面
    @Override
    protected void onResume() {
        super.onResume();
        finish();
    }



    public void getWeiXinAppUserInfo(Activity activity1, final String code) {
        Activity activity=null;
        String URL;
        boolean  flag= ShareUitls.getBoolean(activity1, "BANGDINGWEIXIN", true);
        if (flag) {
            URL = ReaderConfig.BASE_URL + "/user/app-bind-wechat";
            activity= UserInfoActivity.activity;
        } else {
            URL = ReaderConfig.BASE_URL + "/user/app-login-wechat";
            activity= LoginActivity.activity;
        }
        if (activity==null){
            activity=activity1;
        }
        ReaderParams params = new ReaderParams(activity);
        params.putExtraParams("code", code);
        String json = params.generateParamsJson();

        Activity finalActivity = activity;
        HttpUtils.getInstance(finalActivity).sendRequestRequestParams3(URL, json, false, new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(final String result) {


                        MyToash.Log("BANGDINGWEIXIN--2",code+"  "+URL);
                        LoginInfo loginInfo = new Gson().fromJson(result, LoginInfo.class);
                        if (loginInfo != null) {
                            putSharedString(finalActivity, ReaderConfig.TOKEN, loginInfo.getUser_token());
                            putSharedString(finalActivity, ReaderConfig.UID, String.valueOf(loginInfo.getUid()));
                            EventBus.getDefault().post(new RefreshMine(loginInfo));
                            EventBus.getDefault().post(new RefreshUserInfo(loginInfo));

                            EventBus.getDefault().post(new BuyLoginSuccess());


                            if (GETPRODUCT_TYPE(finalActivity) != 2) {
                                EventBus.getDefault().post(new RefreshBookSelf(null));
                            }

                            syncDevice(finalActivity);
                            FirstStartActivity.save_recommend(finalActivity, new FirstStartActivity.Save_recommend() {
                                @Override
                                public void saveSuccess() {

                                }
                            });
                            if (LoginActivity.activity != null) {
                                LoginActivity.activity.finish();
                            }
                            finish();

                        }

                    }

                    @Override
                    public void onErrorResponse(String ex) {

                        MyToash.Log("BANGDINGWEIXIN",code+"  "+URL);
                        /*if (ex!=null&&!ex.equals("308")) {
                            EventBus.getDefault().post(new RefreshUserInfo(true));
                        }*/
                    }
                }

        );
    }
}
