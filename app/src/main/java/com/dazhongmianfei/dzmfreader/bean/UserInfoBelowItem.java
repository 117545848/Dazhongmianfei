package com.dazhongmianfei.dzmfreader.bean;

import android.content.Intent;

/**
 * 个人中心用户信息下方的每一条目
 * Created by scb on 2018/7/13.
 */
public class UserInfoBelowItem {
    /**
     * 图片资源id
     */
    private int id;
    /**
     * 条目名称
     */
    private String title;
    /**
     * 点击时要跳转的某一个activity
     */
    private Class gotoClass;
    /**
     * 跳转时传参数的intent
     */
    private Intent intent;

    private String settingTag;

    public UserInfoBelowItem(int id, String title, Intent intent) {
        this.id = id;
        this.title = title;
        this.gotoClass = gotoClass;
        this.intent = intent;
        this.settingTag = "";
    }

    public Intent getIntent() {
        return intent;
    }

    public void setIntent(Intent intent) {
        this.intent = intent;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Class getGotoClass() {
        return gotoClass;
    }

    public void setGotoClass(Class gotoClass) {
        this.gotoClass = gotoClass;
    }

    public String getSettingTag() {
        return settingTag;
    }

    public void setSettingTag(String settingTag) {
        this.settingTag = settingTag;
    }
}
