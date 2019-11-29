package com.dazhongmianfei.dzmfreader.bean;

/**
 * 作品详情中的book评论
 */
public class BookInfoComment {
    String comment_id;
    String avatar;
    String uid;
    String time;
    String nickname;
    String like_num;
    String content;
    String reply_info;
    String book_id;

    @Override
    public String toString() {
        return "BookInfoComment{" +
                "comment_id='" + comment_id + '\'' +
                ", avatar='" + avatar + '\'' +
                ", uid='" + uid + '\'' +
                ", time='" + time + '\'' +
                ", nickname='" + nickname + '\'' +
                ", like_num='" + like_num + '\'' +
                ", content='" + content + '\'' +
                ", reply_info='" + reply_info + '\'' +
                ", book_id='" + book_id + '\'' +
                '}';
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

    public String getBook_id() {
        return book_id;
    }

    public void setBook_id(String book_id) {
        this.book_id = book_id;
    }
}
