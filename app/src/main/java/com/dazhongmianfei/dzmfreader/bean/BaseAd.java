package com.dazhongmianfei.dzmfreader.bean;

public class BaseAd {
    public String advert_id;//
    public int ad_type;//": 1,   // 广告类型
    public String ad_title;//": "测试一下25",  // 标题
    public String ad_image;//":"http://dazhongmianfei.oss-cn-beijing.aliyuncs.com/comic/banner/f2feec8fb7743d4140cdcb0d6dd21124.jpg?x-oss-process=image%2Fresize%2Cw_600%2Ch_200%2Cm_lfit",    // 广告图
    public String ad_skip_url;//skip_url":"http://www.baidu.com", // 跳转地址
    public int ad_url_type;//'//'":1   // 跳转地址类型（1-内部跳转，2-外部跳转）
    public int ad_width;
    public int ad_height;//":160
    public String ad_android_key;
    public int getAd_width() {
        return ad_width;
    }

    public void setAd_width(int ad_width) {
        this.ad_width = ad_width;
    }

    public int getAd_height() {
        return ad_height;
    }

    public void setAd_height(int ad_height) {
        this.ad_height = ad_height;
    }

    public String getAd_title() {
        return ad_title;
    }

    public void setAd_title(String ad_title) {
        this.ad_title = ad_title;
    }

    public String getAd_image() {
        return ad_image;
    }

    public void setAd_image(String ad_image) {
        this.ad_image = ad_image;
    }

    public String getAd_skip_url() {
        return ad_skip_url;
    }

    public void setAd_skip_url(String ad_skip_url) {
        this.ad_skip_url = ad_skip_url;
    }

    public int getAd_url_type() {
        return ad_url_type;
    }

    public void setAd_url_type(int ad_url_type) {
        this.ad_url_type = ad_url_type;
    }

    public String getAdvert_id() {
        return advert_id;
    }

    public void setAdvert_id(String advert_id) {
        this.advert_id = advert_id;
    }

    public int getAd_type() {
        return ad_type;
    }

    public void setAd_type(int ad_type) {
        this.ad_type = ad_type;
    }





}
