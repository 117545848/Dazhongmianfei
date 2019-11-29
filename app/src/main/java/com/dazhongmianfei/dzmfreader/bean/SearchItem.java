package com.dazhongmianfei.dzmfreader.bean;

/**
 * 搜索页下方的图书列表的bean
 * Created by scb on 2018/6/28.
 */
public class SearchItem {


    /**
     * book_id : 100
     * name : 重生学霸千金：首席校草，别犯规
     * cover : https://qidian.qpic.cn/qdbimg/349573/c_8587887904118103/180
     * flag : 推荐
     */

    public String book_id;
    public String comic_id;
    public String name;
    public String cover;
    public String flag;

    public String getComic_id() {
        return comic_id;
    }

    public void setComic_id(String comic_id) {
        this.comic_id = comic_id;
    }

    public String getBook_id() {
        return book_id;
    }

    public void setBook_id(String book_id) {
        this.book_id = book_id;
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

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }
}
