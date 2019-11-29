package com.dazhongmianfei.dzmfreader.bean;

/**
 * 作品评论的bean
 * Created by scb on 2018/7/8.
 */
public class CommentItem {

    /**
     * comment_id : 2
     * avatar : http://imgsrc.baidu.com/forum/w=580/sign=1588b7c5d739b6004dce0fbfd9503526/7bec54e736d12f2eb97e1a464dc2d56285356898.jpg
     * uid : 58
     * time : 3分钟前
     * nickname : 路边的小草
     * like_num : 10
     * content : 寂寞的大鹏
     * reply_info :
     */

    private String comment_id;
    private String avatar;
    private String uid;
    private String time;
    private String nickname;
    private String like_num;
    private String content;
    private String reply_info;
    private int is_vip;

    public int getIs_vip() {
        return is_vip;
    }

    public void setIs_vip(int is_vip) {
        this.is_vip = is_vip;
    }

    public String getComment_id() {
        return comment_id;
    }

    public void setComment_id(String comment_id) {
        this.comment_id = comment_id;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
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

    public String getReply_info() {
        return reply_info;
    }

    public void setReply_info(String reply_info) {
        this.reply_info = reply_info;
    }
}
