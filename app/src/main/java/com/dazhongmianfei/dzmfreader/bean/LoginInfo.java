package com.dazhongmianfei.dzmfreader.bean;

import java.util.List;

/**
 * 登录后返回的用户数据
 * Created by scb on 2018/7/14.
 */
public class LoginInfo extends UserInfoItem {

/*
    *//**
     * uid : 20
     * nickname :
     * mobile : 15101177625
     * user_token : c320d76fff02704826fc82a4059a26c6
     * is_vip : 0
     * gender : 0
     * avatar :
     * remain : 0
     * score : 50
     * level : Lv0
     * auto_sub : 0
     * bind_list : [{"label":"手机号","action":"bindPhone","status":1,"display":"15101177625"},{"label":"微信","action":"bindWechat","status":0,"display":"未绑定"}]
     *//*

    private int uid;
    private String nickname;
    private String mobile;
    private String user_token;
    private int is_vip;
    private int gender;
    private String avatar;
    private int remain;
    private int score;
    private String level;
    private int auto_sub;
    private List<BindListBean> bind_list;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getUser_token() {
        return user_token;
    }

    public void setUser_token(String user_token) {
        this.user_token = user_token;
    }

    public int getIs_vip() {
        return is_vip;
    }

    public void setIs_vip(int is_vip) {
        this.is_vip = is_vip;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getRemain() {
        return remain;
    }

    public void setRemain(int remain) {
        this.remain = remain;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public int getAuto_sub() {
        return auto_sub;
    }

    public void setAuto_sub(int auto_sub) {
        this.auto_sub = auto_sub;
    }

    public List<BindListBean> getBind_list() {
        return bind_list;
    }

    public void setBind_list(List<BindListBean> bind_list) {
        this.bind_list = bind_list;
    }

    public static class BindListBean {
        *//**
         * label : 手机号
         * action : bindPhone
         * status : 1
         * display : 15101177625
         *//*

        private String label;
        private String action;
        private int status;
        private String display;

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public String getAction() {
            return action;
        }

        public void setAction(String action) {
            this.action = action;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getDisplay() {
            return display;
        }

        public void setDisplay(String display) {
            this.display = display;
        }
    }*/
}
