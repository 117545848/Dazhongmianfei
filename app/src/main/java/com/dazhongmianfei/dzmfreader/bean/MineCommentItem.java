package com.dazhongmianfei.dzmfreader.bean;

/**
 * 我的评论的bean
 * Created by scb on 2018/7/13.
 */
public class MineCommentItem {


    /**
     * book_id : 1
     * book_name : 无敌九零后
     * nickname : 大爷来玩啊
     * avatar : http://dazhongmianfei.oss-cn-beijing.aliyuncs.com/cover/ba663db0c7b26fe5734b30def034e448.jpg
     * time : 7天前
     * like_num : 0
     * content : test
     */

    private String book_id;
    private String book_name;
    private String nickname;
    private String avatar;
    private String time;
    private String like_num;
    private String content;

    public String getBook_id() {
        return book_id;
    }

    public void setBook_id(String book_id) {
        this.book_id = book_id;
    }

    public String getBook_name() {
        return book_name;
    }

    public void setBook_name(String book_name) {
        this.book_name = book_name;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLike_num() {
        return like_num;
    }

    public void setLike_num(String like_num) {
        this.like_num = like_num;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
