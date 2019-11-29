package com.dazhongmianfei.dzmfreader.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 个人中心的用户bean
 * Created by scb on 2018/7/13.
 */
public class UserInfoItem implements Serializable{

    /**
     * uid : 20
     * nickname : 书友10020
     * mobile : 15101177625
     * user_token : 61ef67efcc174f758a42b9a88ffb8407
     * is_vip : 0
     * gender : 0
     * avatar :
     * remain : 0
     * unit : 金币
     * score : 50
     * level : Lv0
     * auto_sub : 0
     * bind_list : [{"label":"手机号","action":"bindPhone","status":1,"display":"15101177625"},{"label":"微信","action":"bindWechat","status":0,"display":"未绑定"}]
     */

    private int uid;
    private String nickname;
    private String mobile;
    private String user_token;
    private int is_vip;
    private int gender;
    private String avatar;
    private int goldRemain;
    private int silverRemain;
    private String unit;
    private String subUnit;

    private int score;
    private String level;
    private int auto_sub;

    private int sign_status;

    public int getSign_status() {
        return sign_status;
    }

    public String getSubUnit() {
        return subUnit;
    }

    public void setSubUnit(String subUnit) {
        this.subUnit = subUnit;
    }

    public void setSign_status(int sign_status) {
        this.sign_status = sign_status;
    }

    private List<BindListBean> bind_list;
    private Task_list task_list;

    public Task_list getTask_list() {
        return task_list;
    }

    public void setTask_list(Task_list task_list) {
        this.task_list = task_list;
    }

    public static class Task_list {
        private int mission_num;
        private int finish_num;

        public int getMission_num() {
            return mission_num;
        }

        public void setMission_num(int mission_num) {
            this.mission_num = mission_num;
        }

        public int getFinish_num() {
            return finish_num;
        }

        public void setFinish_num(int finish_num) {
            this.finish_num = finish_num;
        }
    }
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

    public int getGoldRemain() {
        return goldRemain;
    }

    public void setGoldRemain(int goldRemain) {
        this.goldRemain = goldRemain;
    }

    public int getSilverRemain() {
        return silverRemain;
    }

    public void setSilverRemain(int silverRemain) {
        this.silverRemain = silverRemain;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
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
        /**
         * label : 手机号
         * action : bindPhone
         * status : 1
         * display : 15101177625
         */

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
    }
}
