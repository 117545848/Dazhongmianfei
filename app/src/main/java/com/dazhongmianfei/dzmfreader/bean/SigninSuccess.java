package com.dazhongmianfei.dzmfreader.bean;

import java.util.Arrays;
import java.util.List;

/**
 * Created by scb on 2018/10/27.
 */

public class SigninSuccess {
    private String sign_days;//连续签到的天数
    private String award;//获取的奖励
    private String tomorrow_award;//明日签到奖励
    private List<Recommend.RecommendProduc> book;
    public String getSign_days() {
        return sign_days;
    }

    public List<Recommend.RecommendProduc> getBook() {
        return book;
    }

    public void setBook(List<Recommend.RecommendProduc> book) {
        this.book = book;
    }

    public void setSign_days(String sign_days) {
        this.sign_days = sign_days;
    }

    public String getAward() {
        return award;
    }

    public void setAward(String award) {
        this.award = award;
    }

    public String getTomorrow_award() {
        return tomorrow_award;
    }

    public void setTomorrow_award(String tomorrow_award) {
        this.tomorrow_award = tomorrow_award;
    }


}
