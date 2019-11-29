package com.dazhongmianfei.dzmfreader.comic.been;

import java.util.List;

public class ComicComment {

    public int total_page;//": 4,
    public int current_page;//": 1,
    public int page_size;//": 10,
    public int total_count;//": 36,
    public  List<Comment> list;

    public static class Comment {
        public String comment_id;//": 7951, // 评论ID
        public String uid;//": 973, // 用户ID
        public String nickname;//": "风风雨雨", // 用户名称
        public String avatar;//": "http://dazhongmianfei.oss-cn-beijing.aliyuncs.com/avatar/f6ace92cdfc36802ea719106e10067b2.jpeg?x-oss-process=image%2Fresize%2Cw_100%2Ch_100%2Cm_lfit%2Fresize%2Cw_100%2Ch_100%2Cm_lfit",  // 用户头像
        public String time;//": "1分钟前", // 评论时间
        public String content;//": "支霄飞",   // 评论内容
        public String reply_info;//": "回复风风雨雨：评论一下下，嘿嘿",    // 回复内容
        public String is_vip;//": 1     // 用户是否是VIP（1-是）

        @Override
        public String toString() {
            return "Comment{" +
                    "comment_id='" + comment_id + '\'' +
                    ", uid='" + uid + '\'' +
                    ", nickname='" + nickname + '\'' +
                    ", avatar='" + avatar + '\'' +
                    ", time='" + time + '\'' +
                    ", content='" + content + '\'' +
                    ", reply_info='" + reply_info + '\'' +
                    ", is_vip='" + is_vip + '\'' +
                    '}';
        }

        public String getComment_id() {
            return comment_id;
        }

        public void setComment_id(String comment_id) {
            this.comment_id = comment_id;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
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

        public String getIs_vip() {
            return is_vip;
        }

        public void setIs_vip(String is_vip) {
            this.is_vip = is_vip;
        }
    }

    @Override
    public String toString() {
        return "ComicComment{" +
                "total_page=" + total_page +
                ", current_page=" + current_page +
                ", page_size=" + page_size +
                ", total_count=" + total_count +
                ", list=" + list +
                '}';
    }
}
