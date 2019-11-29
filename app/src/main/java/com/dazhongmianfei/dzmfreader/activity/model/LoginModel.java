package com.dazhongmianfei.dzmfreader.activity.model;

import android.app.Activity;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;


import com.dazhongmianfei.dzmfreader.R;
import com.dazhongmianfei.dzmfreader.activity.view.LoginResultCallback;
import com.dazhongmianfei.dzmfreader.config.ReaderConfig;
import com.dazhongmianfei.dzmfreader.http.ReaderParams;
import com.dazhongmianfei.dzmfreader.utils.HttpUtils;
import com.dazhongmianfei.dzmfreader.utils.LanguageUtil;
import com.dazhongmianfei.dzmfreader.utils.MyToash;

//.http.RequestParams;


/**
 * Created by scb on 2018/7/14.
 */
public class LoginModel {
    public String TAG = LoginModel.class.getSimpleName();
    public Button mButtonView;
    public TimeCount time;
    private Activity mActivity;

    public LoginModel(Activity activity) {
        mActivity = activity;
        time = new TimeCount(60000, 1000);
    }

    public void countDown(View buttonView) {
        mButtonView = (Button) buttonView;
    }

    public void cancel() {
        time.cancel();
    }

    /**
     * 发送请求获取验证码
     */
    public void getMessage(String phoneNum, final LoginResultCallback callback) {



        if (!isMobileNO(phoneNum))
            return;

        //开启倒计时
        time.start();
        ReaderParams params = new ReaderParams(mActivity);

        String formattedPhoneNum = phoneNum.replaceAll(" ", "");
        params.putExtraParams("mobile", formattedPhoneNum);

        String json = params.generateParamsJson();
        HttpUtils.getInstance(mActivity).sendRequestRequestParams3(ReaderConfig.mMessageUrl, json, true, new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(String result) {

                        if (callback != null) {
                            callback.getResult(result);
                        }
                    }

                    @Override
                    public void onErrorResponse(String  ex) {

                    }
                }

        );


    }

    public void loginPhone(String userName, String message, final LoginResultCallback callback) {

        if (!isMobileNO(userName) || isEmpty(message))
            return;




        ReaderParams params = new ReaderParams(mActivity);
        String phoneNum = userName.replaceAll(" ", "");
        params.putExtraParams("mobile", phoneNum);

        params.putExtraParams("code", message);

        String json = params.generateParamsJson();

        HttpUtils.getInstance(mActivity).sendRequestRequestParams3(ReaderConfig.mMobileLoginUrl,json, true, new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(String result) {

                        if (callback != null) {
                            callback.getResult(result);
                        }
                    }

                    @Override
                    public void onErrorResponse(String  ex) {

                    }
                }

        );

    }

    public void loginUser(String userName, String password, final LoginResultCallback callback) {


        ReaderParams params = new ReaderParams(mActivity);

        params.putExtraParams("user_name", userName);

        params.putExtraParams("password", password);
        String json = params.generateParamsJson();

        HttpUtils.getInstance(mActivity).sendRequestRequestParams3(ReaderConfig.mUsernameLoginUrl,json, true, new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(String result) {
                        if (callback != null) {
                            callback.getResult(result);
                        }
                    }

                    @Override
                    public void onErrorResponse(String  ex) {

                    }
                }

        );


    }

    /**
     * 绑定手机号
     */
    public void bindPhone(String phoneNum, String msg, final LoginResultCallback callback) {


        ReaderParams params = new ReaderParams(mActivity);

        params.putExtraParams("mobile", phoneNum);

        params.putExtraParams("code", msg);
        if(msg==null||msg.length()==0){

            return;
        }
        String json = params.generateParamsJson();

        HttpUtils.getInstance(mActivity).sendRequestRequestParams3(ReaderConfig.mUserBindPhoneUrl, json, true, new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(String result) {

                        if (callback != null) {
                            callback.getResult(result);
                        }
                    }

                    @Override
                    public void onErrorResponse(String  ex) {

                    }
                }

        );
    }

    /**
     * 判断手机格式是否正确
     *
     * @param mobiles
     * @return
     */
    public boolean isMobileNO(String mobiles) {

        String str = mobiles.replaceAll(" ", "");
        //"[1]"代表第1位为数字1，"[3578]"代表第二位可以为3、5、7、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        String telRegex = "[1][34578]\\d{9}";
        if (str.matches(telRegex)) {
            return true;
        } else {
            MyToash.ToashError(mActivity, LanguageUtil.getString(mActivity, R.string.LoginActivity_phoneerrpr));
            return false;
        }
    }

    /**
     * 判断验证码是否为空
     *
     * @param msg
     * @return
     */
    public boolean isEmpty(String msg) {

        String message = msg.replaceAll(" ", "");
        if (TextUtils.isEmpty(message)) {
            MyToash.ToashError(mActivity, LanguageUtil.getString(mActivity, R.string.LoginActivity_codenull));
            return true;
        }
        return false;
    }

    class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            mButtonView.setClickable(false);
            mButtonView.setText("(" + millisUntilFinished / 1000 + ") "+LanguageUtil.getString(mActivity,R.string.LoginActivity_againsend));
        }

        @Override
        public void onFinish() {
            mButtonView.setText(LanguageUtil.getString(mActivity,R.string.LoginActivity_againgetcode));
            mButtonView.setClickable(true);

        }
    }

}
