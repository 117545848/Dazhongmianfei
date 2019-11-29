package com.dazhongmianfei.dzmfreader.eventbus;

import android.content.ContentProvider;

import com.dazhongmianfei.dzmfreader.bean.UserInfoItem;

/**
 * Created by scb on 2018/8/8.
 */
public class RefreshUserInfo {
    public UserInfoItem UserInfo;

    public RefreshUserInfo(UserInfoItem userInfo) {
        UserInfo = userInfo;
    }
}
