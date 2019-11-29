package com.dazhongmianfei.dzmfreader.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 作品详情中的book
 */
public class BookInfoItem  implements Serializable{
    /**
     * 书籍id
     */
    String bookId;
    /**
     * 名称
     */
    String name;
    /**
     * 封面
     */
    String cover;
    /**
     * 描述
     */
    String description;
    /**
     * 作者
     */
    String author;
    /**
     * 连载中
     */
    String displayLabel;
    /**
     * 书本状态：连载中／已完结
     */
    String finished;
    /**
     * 免费...
     */
    String flag;
    /**
     * 类型列表
     */
    List<Tag> tag;
    /**
     * 总字数
     */
    String totalWords;
    /**
     * 总评论数
     */
    String totalComment;
    /**
     * 最新章节更新时间
     */
    String lastChapterTime;
    /**
     * 最新章节名
     */
    String lastChapter;
    private int total_chapter;

    public int getTotal_Chapter() {
        return total_chapter;
    }

    public void setTotal_Chapter(int total_chapter) {
        this.total_chapter = total_chapter;
    }
    public static class Tag {
        /**
         * 类型：浪漫青春
         */
        String tab;
        /**
         * 颜色
         */
        String color;

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

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getFinished() {
        return finished;
    }

    public void setFinished(String finished) {
        this.finished = finished;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public List<Tag> getTag() {
        return tag;
    }

    public void setTag(List<Tag> tag) {
        this.tag = tag;
    }

    public String getDisplayLabel() {
        return displayLabel;
    }

    public void setDisplayLabel(String displayLabel) {
        this.displayLabel = displayLabel;
    }

    public String getTotalWords() {
        return totalWords;
    }

    public void setTotalWords(String totalWords) {
        this.totalWords = totalWords;
    }

    public String getTotalComment() {
        return totalComment;
    }

    public void setTotalComment(String totalComment) {
        this.totalComment = totalComment;
    }

    public String getLastChapterTime() {
        return lastChapterTime;
    }

    public void setLastChapterTime(String lastChapterTime) {
        this.lastChapterTime = lastChapterTime;
    }

    public String getLastChapter() {
        return lastChapter;
    }

    public void setLastChapter(String lastChapter) {
        this.lastChapter = lastChapter;
    }
}
