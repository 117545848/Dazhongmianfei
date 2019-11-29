package com.dazhongmianfei.dzmfreader.bean;

import java.util.List;

/**
 * Created by scb on 2018/8/12.
 */
public class RechargeItem {


    /**
     * goods_id : 22
     * title : 600金币
     * price : 6元
     * flag : 送17%
     * note : +100金币
     * apple_id :
     * pay_channel : ["alipay","weixin"]
     */

    private String goods_id;
    private String title;
    private String price;
    private String flag;
    private String note;
    private String apple_id;
    private List<String> pay_channel;
    public boolean choose;

    public String getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getApple_id() {
        return apple_id;
    }

    public void setApple_id(String apple_id) {
        this.apple_id = apple_id;
    }

    public List<String> getPay_channel() {
        return pay_channel;
    }

    public void setPay_channel(List<String> pay_channel) {
        this.pay_channel = pay_channel;
    }
}
