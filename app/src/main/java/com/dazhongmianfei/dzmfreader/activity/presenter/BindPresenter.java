package com.dazhongmianfei.dzmfreader.activity.presenter;

import android.app.Activity;
import android.widget.Toast;

import com.dazhongmianfei.dzmfreader.R;
import com.dazhongmianfei.dzmfreader.activity.BindPhoneActivity;
import com.dazhongmianfei.dzmfreader.activity.model.LoginModel;
import com.dazhongmianfei.dzmfreader.activity.view.LoginResultCallback;
import com.dazhongmianfei.dzmfreader.activity.view.LoginView;
;
import com.dazhongmianfei.dzmfreader.eventbus.RefreshBookSelf;
import com.dazhongmianfei.dzmfreader.eventbus.RefreshMine;
import com.dazhongmianfei.dzmfreader.utils.LanguageUtil;
import com.dazhongmianfei.dzmfreader.utils.MyToash;
import org.greenrobot.eventbus.EventBus;
import static com.dazhongmianfei.dzmfreader.config.ReaderConfig.GETPRODUCT_TYPE;


/**
 * 绑定手机号
 * Created by scb on 2018/8/10.
 */
public class BindPresenter {
    private LoginModel mLoginModel;
    private LoginView mLoginView;

    public BindPresenter(LoginView loginView) {
        mLoginView = loginView;
        mLoginModel = new LoginModel((BindPhoneActivity) mLoginView);
    }

    public void getMessage() {


        mLoginModel.countDown(mLoginView.getButtonView());

        mLoginModel.getMessage(mLoginView.getPhoneNum(), new LoginResultCallback() {
            @Override
            public void getResult(final String jsonStr) {
                final Activity activity = (Activity) mLoginView;
                MyToash.ToashSuccess(activity, LanguageUtil.getString(activity, R.string.LoginActivity_getcodeing));
            }
        });
    }

    public void bindPhone() {
        mLoginModel.bindPhone(mLoginView.getPhoneNum(), mLoginView.getMessage(), new LoginResultCallback() {
            @Override
            public void getResult(final String jsonStr) {
                EventBus.getDefault().post(new RefreshMine(null));
                if (GETPRODUCT_TYPE((Activity) mLoginView) != 2) {
                    EventBus.getDefault().post(new RefreshBookSelf(null));
                }

                ((Activity) mLoginView).finish();
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
