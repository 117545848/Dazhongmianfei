package com.dazhongmianfei.dzmfreader.bean;

/**
 * apk更新
 */
public class AppUpdate {
    public int update;
    public String msg;
    public String url;
    //public SettingBean setting;
    public Startpage start_page;
    public int ad_switch;
    public int vip_send_switch;
    public Unit_tag unit_tag;
    public String share_icon;
    public String share_read_url;
    public  class Unit_tag {
        @Override
        public String toString() {
            return "Unit_tag{" +
                    "currencyUnit='" + currencyUnit + '\'' +
                    ", subUnit='" + subUnit + '\'' +
                    '}';
        }

        private String currencyUnit;
        private String subUnit;

        public String getCurrencyUnit() {
            return currencyUnit;
        }

        public void setCurrencyUnit(String currencyUnit) {
            this.currencyUnit = currencyUnit;
        }

        public String getSubUnit() {
            return subUnit;
        }

        public void setSubUnit(String subUnit) {
            this.subUnit = subUnit;
        }
    }





    public  class Startpage {
        //skip_type":1,"image":"http://dazhongmianfei.oss-cn-beijing.aliyuncs.com/cover/8a7c2ea9e60dfc137ce32d8d751e52a9.jpg?x-oss-process=image%2Fresize%2Cw_1242%2Ch_2208%2Cm_lfit","title":"收到","content":"1132"

        public String image;
        public String content;
        public int skip_type;
        public String title;

        @Override
        public String toString() {
            return "Startpage{" +
                    "skip_type=" + skip_type +
                    ", image='" + image + '\'' +
                    '}';
        }
    }

    public int getUpdate() {
        return update;
    }

    public void setUpdate(int update) {
        this.update = update;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Startpage getStart_page() {
        return start_page;
    }

    public void setStart_page(Startpage start_page) {
        this.start_page = start_page;
    }

    public int getAd_switch() {
        return ad_switch;
    }

    public void setAd_switch(int ad_switch) {
        this.ad_switch = ad_switch;
    }

    public int getVip_send_switch() {
        return vip_send_switch;
    }

    public void setVip_send_switch(int vip_send_switch) {
        this.vip_send_switch = vip_send_switch;
    }

    public Unit_tag getUnit_tag() {
        return unit_tag;
    }

    public void setUnit_tag(Unit_tag unit_tag) {
        this.unit_tag = unit_tag;
    }

    public String getShare_icon() {
        return share_icon;
    }

    public void setShare_icon(String share_icon) {
        this.share_icon = share_icon;
    }

    public String getShare_read_url() {
        return share_read_url;
    }

    public void setShare_read_url(String share_read_url) {
        this.share_read_url = share_read_url;
    }

    @Override
    public String toString() {
        return "AppUpdate{" +
                "update=" + update +
                ", msg='" + msg + '\'' +
                ", url='" + url + '\'' +
                ", start_page=" + start_page +
                ", ad_switch=" + ad_switch +
                ", vip_send_switch=" + vip_send_switch +
                ", unit_tag=" + unit_tag +
                ", share_icon='" + share_icon + '\'' +
                ", share_read_url='" + share_read_url + '\'' +
                '}';
    }
}




