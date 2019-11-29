package com.dazhongmianfei.dzmfreader.bean;

/**
 * Created by scb on 2018/11/27.
 */

public class FloatImageView {
    public String result;
    public String msg;
    public Data data;
    public static FloatImageView floatImageView;

    public static  class Data {
        public String img;
        public String url;
        public String call_show;
        public String call_click;
        public String showad;

    }


    /*
    *
    *
    * result	true:成功,false:失败
msg	失败的提示文本
data	返回的数据
img	显示的浮动图片地址
url	点击后打开的img,可在webview或系统浏览器打开
call_show	图片显示成功后异步请求该img,无需关注返回结果
call_click	用户点击图片后异步请求该img,无需关注返回结果

    *
    * */

}
