package com.dazhongmianfei.dzmfreader.activity.view;

import android.view.View;

/**
 * Created by scb on 2018/7/14.
 */
public interface LoginView {
    /**
     * 获取用户名
     *
     * @return
     */
    String getUserName();

    /**
     * 获取密码
     *
     * @return
     */
    String getPassword();

    /**
     * 获取手机号
     *
     * @return
     */
    String getPhoneNum();

    /**
     * 获取验证码
     *
     * @return
     */
    String getMessage();

    /**
     * "获取验证码"的button
     *
     * @return
     */
    View getButtonView();

}
