package com.dazhongmianfei.dzmfreader.activity.presenter;

import android.app.Activity;

import com.google.gson.Gson;
import com.dazhongmianfei.dzmfreader.R;
import com.dazhongmianfei.dzmfreader.activity.FirstStartActivity;
import com.dazhongmianfei.dzmfreader.activity.LoginActivity;
import com.dazhongmianfei.dzmfreader.activity.model.LoginModel;
import com.dazhongmianfei.dzmfreader.activity.view.LoginResultCallback;
import com.dazhongmianfei.dzmfreader.activity.view.LoginView;
import com.dazhongmianfei.dzmfreader.bean.LoginInfo;
import com.dazhongmianfei.dzmfreader.comic.eventbus.RefreshComic;
import com.dazhongmianfei.dzmfreader.config.ReaderConfig;
import com.dazhongmianfei.dzmfreader.eventbus.BuyLoginSuccess;
import com.dazhongmianfei.dzmfreader.eventbus.RefreshBookSelf;
import com.dazhongmianfei.dzmfreader.eventbus.RefreshMine;
import com.dazhongmianfei.dzmfreader.utils.AppPrefs;
import com.dazhongmianfei.dzmfreader.utils.LanguageUtil;
import com.dazhongmianfei.dzmfreader.utils.MyToash;

import org.greenrobot.eventbus.EventBus;

import static com.dazhongmianfei.dzmfreader.config.ReaderConfig.GETPRODUCT_TYPE;
import static com.dazhongmianfei.dzmfreader.config.ReaderConfig.syncDevice;


/**
 * Created by scb on 2018/7/14.
 */
public class LoginPresenter {
    private LoginModel mLoginModel;
    private LoginView mLoginView;

    public LoginPresenter(LoginView loginView) {
        mLoginView = loginView;
        this.activity = (LoginActivity) mLoginView;
        mLoginModel = new LoginModel((LoginActivity) mLoginView);
    }

    Activity activity;

    public LoginPresenter(LoginView loginView, Activity activity) {
        mLoginView = loginView;
        this.activity = activity;
        mLoginModel = new LoginModel(activity);
    }

    public void getMessage() {

        mLoginModel.countDown(mLoginView.getButtonView());

        mLoginModel.getMessage(mLoginView.getPhoneNum(), new LoginResultCallback() {
            @Override
            public void getResult(final String jsonStr) {
               MyToash.ToashSuccess(activity, LanguageUtil.getString(activity, R.string.LoginActivity_getcodeing));
            }
        });
    }


    public void loginPhone(final LoginActivity.LoginSuccess loginSuccess) {
        mLoginModel.loginPhone(mLoginView.getPhoneNum(), mLoginView.getMessage(), new LoginResultCallback() {
            @Override
            public void getResult(final String loginStr) {
                    Gson gson = new Gson();
                    LoginInfo loginInfo = gson.fromJson(loginStr, LoginInfo.class);
                    if (loginInfo != null) {
                        AppPrefs.putSharedString(activity, ReaderConfig.TOKEN, loginInfo.getUser_token());
                        AppPrefs.putSharedString(activity, ReaderConfig.UID, String.valueOf(loginInfo.getUid()));
                        EventBus.getDefault().post(new BuyLoginSuccess());
                        syncDevice(activity);
                        FirstStartActivity.save_recommend(activity, new FirstStartActivity.Save_recommend() {
                            @Override
                            public void saveSuccess() {
                            }
                        });
                        EventBus.getDefault().post(new RefreshMine(loginInfo));
                        //EventBus.getDefault().post(new RefreshDiscoveryFragment());
                        if (GETPRODUCT_TYPE(activity) != 2) {
                            EventBus.getDefault().post(new RefreshBookSelf(null));
                        }
                        if (GETPRODUCT_TYPE(activity) != 1) {
                            EventBus.getDefault().post(new RefreshComic(null));
                        }
                        loginSuccess.success();
                        activity.finish();
                    }
            }
        });
    }



    /**
     * 取消倒计时
     */
    public void cancelCountDown() {
        mLoginModel.cancel();
    }

}
