package com.dazhongmianfei.dzmfreader.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.dazhongmianfei.dzmfreader.R;
import com.dazhongmianfei.dzmfreader.activity.presenter.LoginPresenter;
import com.dazhongmianfei.dzmfreader.activity.view.LoginView;
import com.dazhongmianfei.dzmfreader.config.ReaderConfig;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

import com.dazhongmianfei.dzmfreader.R2;
import com.dazhongmianfei.dzmfreader.utils.ShareUitls;


/**
 * 用户登录页
 * Created by wudeyan on 2018/7/14.
 */
public class LoginActivity extends BaseActivity implements LoginView {

    @Override
    public int initContentView() {
        return R.layout.activity_login;
    }

    private LoginPresenter mPresenter;

    @BindView(R2.id.activity_login_close)
    LinearLayout activity_login_close;
    @BindView(R2.id.activity_login_title)
    TextView activity_login_title;

    @BindView(R2.id.activity_login_phone_username)
    EditText activity_login_phone_username;
    @BindView(R2.id.activity_login_phone_message)
    EditText activity_login_phone_message;
    @BindView(R2.id.activity_login_phone_get_message_btn)
    Button activity_login_phone_get_message_btn;
    @BindView(R2.id.activity_login_phone_btn)
    Button activity_login_phone_btn;
    @BindView(R2.id.activity_login_phone_clear)
    ImageView activity_login_phone_clear;
    @BindView(R2.id.activity_login_contract)
    TextView activity_login_contract;
    @BindView(R2.id.activity_login_weixin)
    LinearLayout activity_login_weixin;


    @OnClick(value = {R.id.activity_login_phone_get_message_btn,
            R.id.activity_login_phone_btn
            , R.id.activity_login_phone_clear
            , R.id.activity_login_contract,
            R.id.activity_login_weixin
            , R.id.activity_login_close
    })
    public void getEvent(View view) {
        switch (view.getId()) {
            case R.id.activity_login_close:
                finish();
                break;
            case R.id.activity_login_phone_get_message_btn:
                mPresenter.getMessage();
                break;
            case R.id.activity_login_phone_btn:
                mPresenter.loginPhone(new LoginSuccess() {
                    @Override
                    public void success() {

                    }

                    @Override
                    public void cancle() {

                    }
                });
                break;
            case R.id.activity_login_phone_clear:
                activity_login_phone_username.setText("");
                break;

            case R.id.activity_login_contract:
                startActivity(new Intent(LoginActivity.this, AboutActivity.class).putExtra("url", ReaderConfig.privacy).putExtra("flag", "privacy"));
                break;
            case R.id.activity_login_weixin:
                //微信登录
                weixinLogin(activity, true, null);

                break;
        }
    }

    public static Activity activity;
    public IWXAPI iwxapi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        iwxapi = WXAPIFactory.createWXAPI(this, ReaderConfig.WEIXIN_PAY_APPID, true);
        iwxapi.registerApp(ReaderConfig.WEIXIN_PAY_APPID);
    }

    class ASD {
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refresh(ASD asd) {


    }

    @Override
    public void initView() {
        activity = this;
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

        activity_login_phone_username.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    activity_login_phone_clear.setVisibility(View.GONE);
                } else {
                    if (!TextUtils.isEmpty(activity_login_phone_username.getText().toString())) {
                        activity_login_phone_clear.setVisibility(View.VISIBLE);
                    } else {
                        activity_login_phone_clear.setVisibility(View.GONE);
                    }
                }
            }
        });
        mPresenter = new LoginPresenter(this);


    }

    @Override
    public void initData() {

    }

    @Override
    public void initInfo(String json) {
        super.initInfo(json);
    }


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

    public interface LoginSuccess {
        void success();

        void cancle();
    }

    public void weixinLogin(Activity activity, final boolean isfinsh, LoginSuccess loginSuccess) {
        ShareUitls.putBoolean(activity,"BANGDINGWEIXIN",false);
        UMShareAPI.get(activity).deleteOauth(activity, SHARE_MEDIA.WEIXIN, authListener);

    }

    UMAuthListener authListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
            // Toast.makeText(LoginActivity.this, "开始", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            if (iwxapi == null) {
                iwxapi = WXAPIFactory.createWXAPI(activity, ReaderConfig.WEIXIN_PAY_APPID, true);
            }
            if (!iwxapi.isWXAppInstalled()) {
                //  ToastUtils.toast("您手机尚未安装微信，请安装后再登录");
                return;
            }
            iwxapi.registerApp(ReaderConfig.WEIXIN_PAY_APPID);
            SendAuth.Req req = new SendAuth.Req();
            req.scope = "snsapi_userinfo";
            req.state = "wechat_sdk_xb_live_state";//官方说明：用于保持请求和回调的状态，授权请求后原样带回给第三方。该参数可用于防止csrf攻击（跨站请求伪造攻击），建议第三方带上该参数，可设置为简单的随机数加session进行校验

            iwxapi.sendReq(req);
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            //  Toast.makeText(LoginActivity.this, "失败：" + t.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            //   Toast.makeText(LoginActivity.this, "取消了", Toast.LENGTH_LONG).show();
        }
    };


/*
    public static void qqLogin(Activity activity, final boolean isfinsh,LoginSuccess loginSuccess) {
        Isfinsh = isfinsh;
        ACtivity = activity;
        LoginSuccess=loginSuccess;
        UMShareAPI.get(activity).getPlatformInfo(activity, SHARE_MEDIA.QQ, authListener);
    }

    static UMAuthListener authListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
            // Toast.makeText(LoginActivity.this, "开始", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            String unionid = data.get("unionid");
            MyToash.Log("SHARE_MEDIA   " + unionid);
            //    getAccessToken(unionid);

            String access_token = data.get("access_token");
            String openid = data.get("openid");

            getWeiXinAppUserInfoOPENID(ACtivity, openid, access_token, Isfinsh);
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            //  Toast.makeText(LoginActivity.this, "失败：" + t.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            //   Toast.makeText(LoginActivity.this, "取消了", Toast.LENGTH_LONG).show();
        }
    };


    public static void getWeiXinUserInfo(final Activity activity, final String openid, final String access_token, final boolean isfinsh) {
        String userinfo_url = "https://api.weixin.qq.com/sns/userinfo?access_token=" + access_token + "&openid=" + openid;
        RequestParams requestParams = new RequestParams(userinfo_url);
        HttpUtils.getInstance(activity).sendRequestRequestParamsGet("", requestParams, false, new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(final String result) {
                        Log.i("getWeiXinAppUserInfo2", result);
                        if (!result.isEmpty()) {
                            if (!result.contains("errcode")) {
                                getWeiXinAppUserInfo(activity, result, isfinsh);
                            }
                        }
                    }

                    @Override
                    public void onErrorResponse(String  ex) {
                    }
                }

        );

    }


    public static void getWeiXinAppUserInfo(final Activity activity, final String str, final boolean isfinsh) {
        RequestParams requestParams = null;
        ReaderParams params = new ReaderParams(activity);
        requestParams = new RequestParams(ReaderConfig.first_mWeiXinLoginUrl);
        params.putExtraParams("info", str);

        String json = params.generateParamsJson();
        requestParams.addBodyParameter("", json);
        HttpUtils.getInstance(activity).sendRequestRequestParams("", requestParams, true, new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(final String result) {
                        Log.i("getWeiXinAppUserInfo3", result);
                        try {
                            JSONObject obj = new JSONObject(result);
                            int code = Integer.parseInt(obj.getString("code"));
                            if (code == 0) {
                                Gson gson = new Gson();
                                String loginStr = obj.getJSONObject("data").toString();
                                LoginInfo loginInfo = gson.fromJson(loginStr, LoginInfo.class);
                                if (loginInfo != null && loginInfo.getUser_token() != null && loginInfo.getUser_token().length() > 0) {
                                    AppPrefs.putSharedString(activity, ReaderConfig.TOKEN, loginInfo.getUser_token());
                                    AppPrefs.putSharedString(activity, ReaderConfig.UID, String.valueOf(loginInfo.getUid()));
                                    EventBus.getDefault().post(loginInfo);
                                    EventBus.getDefault().post(new RefreshMine(loginInfo));
                                    EventBus.getDefault().post(new RefreshReadHistory());
                                    EventBus.getDefault().post(new RefreshBookSelf(null));
                                    //EventBus.getDefault().post(new RefreshDiscoveryFragment());
                                    SplashActivity.syncDevice(activity);
                                    FirstStartActivity.save_recommend(activity, new FirstStartActivity.Save_recommend() {
                                        @Override
                                        public void saveSuccess() {

                                        }
                                    });
                                    if (isfinsh) {
                                        activity.finish();
                                    }
                                }
                            }
                        } catch (JSONException e) {

                        }
                    }

                    @Override
                    public void onErrorResponse(String  ex) {

                    }
                }

        );
    }*/
/*

    public static void getWeiXinAppUserInfoOPENID(final Activity activity, final String openid, final String access_token, final boolean isFinsh) {
        RequestParams requestParams = null;
        ReaderParams params = new ReaderParams(activity);
        requestParams = new RequestParams(ReaderConfig.nofirst_mWeiXinLoginUrl);
        params.putExtraParams("open_id", openid);


        String json = params.generateParamsJson();
        requestParams.addBodyParameter("", json);
        HttpUtils.getInstance(activity).sendRequestRequestParams("", requestParams, true, new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(final String result) {
                        Log.i("getWeiXinAppUserInfo3", result);
                        try {
                            JSONObject obj = new JSONObject(result);
                            int code = Integer.parseInt(obj.getString("code"));
                            if (code == 0) {
                                Gson gson = new Gson();
                                String loginStr = obj.getJSONObject("data").toString();
                                LoginInfo loginInfo = gson.fromJson(loginStr, LoginInfo.class);
                                if (loginInfo != null && loginInfo.getUser_token() != null && loginInfo.getUser_token().length() > 0) {
                                    AppPrefs.putSharedString(activity, ReaderConfig.TOKEN, loginInfo.getUser_token());
                                    AppPrefs.putSharedString(activity, ReaderConfig.UID, String.valueOf(loginInfo.getUid()));

                                    EventBus.getDefault().post(new RefreshMine(loginInfo));
                                    EventBus.getDefault().post(new RefreshReadHistory());
                                    EventBus.getDefault().post(new RefreshBookSelf(null));
                                    //EventBus.getDefault().post(new RefreshDiscoveryFragment());
                                    SplashActivity.syncDevice(activity);
                                    FirstStartActivity.save_recommend(activity, new FirstStartActivity.Save_recommend() {
                                        @Override
                                        public void saveSuccess() {

                                        }
                                    });
                                    if (isFinsh) {
                                        activity.finish();
                                    }else {
                                        if(LoginSuccess!=null){
                                            LoginSuccess.success();
                                        }
                                    }
                                }
                            } else {
                                getWeiXinUserInfo(activity, openid, access_token, isFinsh);
                            }

                        } catch (JSONException e) {
                            getWeiXinUserInfo(activity, openid, access_token, isFinsh);
                        }
                    }

                    @Override
                    public void onErrorResponse(String  ex) {
                        getWeiXinUserInfo(activity, openid, access_token, isFinsh);
                    }
                }

        );
    }
*/

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this); // 基础指标统计，不能遗漏
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this); // 基础指标统计，不能遗漏
    }

    @Override
    protected void onDestroy() {
        mPresenter.cancelCountDown();
        activity=null;
        super.onDestroy();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }
}
