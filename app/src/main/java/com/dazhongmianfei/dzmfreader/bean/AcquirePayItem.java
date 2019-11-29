package com.dazhongmianfei.dzmfreader.bean;

import java.util.List;

/**
 * Created by scb on 2018/8/12.
 */
public class AcquirePayItem {


    /**
     * goods_id : 11
     * title : 12个月
     * price : 150
     * apple_id :
     * pay_channel : ["alipay","weixin"]
     * note : 省66元，12.5元/每月
     * tag : [{"tab":"年费vip","color":"#ff0000"}]
     */

    private String goods_id;
    private String title;
    private String price;
    private String apple_id;
    private String note;
    private List<String> pay_channel;
    private List<TagBean> tag;

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

    public String getApple_id() {
        return apple_id;
    }

    public void setApple_id(String apple_id) {
        this.apple_id = apple_id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public List<String> getPay_channel() {
        return pay_channel;
    }

    public void setPay_channel(List<String> pay_channel) {
        this.pay_channel = pay_channel;
    }

    public List<TagBean> getTag() {
        return tag;
    }

    public void setTag(List<TagBean> tag) {
        this.tag = tag;
    }

    public static class TagBean {
        /**
         * tab : 年费vip
         * color : #ff0000
         */

        private String tab;
        private String color;

        public String getTab() {
            return tab;
        }

        public void setTab(String tab) {
            this.tab = tab;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }
    }
}
