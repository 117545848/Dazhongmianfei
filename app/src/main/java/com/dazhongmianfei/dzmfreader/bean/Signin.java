package com.dazhongmianfei.dzmfreader.bean;

import java.util.Arrays;
import java.util.List;

/**
 * Created by scb on 2018/10/27.
 */

public class Signin {
    private Sign_info sign_info;
    private List<Sign_calendar> sign_calendar;
private  String [] sign_rules;
    public Sign_info getSign_info() {
        return sign_info;
    }

    public void setSign_info(Sign_info sign_info) {
        this.sign_info = sign_info;
    }

    public List<Sign_calendar> getSign_calendar() {
        return sign_calendar;
    }

    public void setSign_calendar(List<Sign_calendar> sign_calendar) {
        this.sign_calendar = sign_calendar;
    }

    public static class Sign_info {


        //public int sign_days;//连续签到的天数
       // public int award;//获取的奖励
        public int sign_status;//签到状态0未签到 1 已签到
        public String tomorrow_award;//明日签到奖励

    }

    public static class Sign_calendar {

       // public String date;//日期
        public int award;//获取的奖励
        public int sign_status;//签到状态0未签到 1 已签到

    }

    public String[] getSign_rules() {
        return sign_rules;
    }

    public void setSign_rules(String[] sign_rules) {
        this.sign_rules = sign_rules;
    }

    @Override
    public String toString() {
        return "Signin{" +
                "sign_info=" + sign_info +
                ", sign_calendar=" + sign_calendar +
                ", sign_rules=" + Arrays.toString(sign_rules) +
                '}';
    }
}
