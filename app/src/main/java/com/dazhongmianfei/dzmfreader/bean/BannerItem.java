package com.dazhongmianfei.dzmfreader.bean;

/**
 * 首页banner的Item
 */
public class BannerItem {
    /**
     * bookid
     */
    String bookId;
    /**
     * 名称
     */
    String name;
    /**
     * 图片地址
     */
    String image;
    public BannerItem(String bookId, String image) {
        this.bookId = bookId;
        this.image = image;
    }
    public BannerItem() {
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
