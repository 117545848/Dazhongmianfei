package com.dazhongmianfei.dzmfreader.comic.been;

import java.util.List;

public class ComicChapterItem {
    public String comic_id;//": 1007, //漫画id
    public String chapter_id;//": 486380, //章节id
    public String chapter_title;//": 章节title
    public String next_chapter;//": 621914, //下一章id
    public String last_chapter;//": 0, //上一章id
    public int   total_comment;
    public int is_preview;//": 1, //是否预览章节 1是 0不是
    public  int  display_order;
    public List<BaseComicImage> image_list;

    public String getComic_id() {
        return comic_id;
    }

    public void setComic_id(String comic_id) {
        this.comic_id = comic_id;
    }

    public String getChapter_id() {
        return chapter_id;
    }

    public void setChapter_id(String chapter_id) {
        this.chapter_id = chapter_id;
    }

    public String getChapter_title() {
        return chapter_title;
    }

    public void setChapter_title(String chapter_title) {
        this.chapter_title = chapter_title;
    }

    public String getNext_chapter() {
        return next_chapter;
    }

    public void setNext_chapter(String next_chapter) {
        this.next_chapter = next_chapter;
    }

    public String getLast_chapter() {
        return last_chapter;
    }

    public void setLast_chapter(String last_chapter) {
        this.last_chapter = last_chapter;
    }

    public int getTotal_comment() {
        return total_comment;
    }

    public void setTotal_comment(int total_comment) {
        this.total_comment = total_comment;
    }

    public int getIs_preview() {
        return is_preview;
    }

    public void setIs_preview(int is_preview) {
        this.is_preview = is_preview;
    }

    public int getDisplay_order() {
        return display_order;
    }

    public void setDisplay_order(int display_order) {
        this.display_order = display_order;
    }

    public List<BaseComicImage> getImage_list() {
        return image_list;
    }

    public void setImage_list(List<BaseComicImage> image_list) {
        this.image_list = image_list;
    }

    @Override
    public String toString() {
        return "ComicChapterItem{" +
                "comic_id='" + comic_id + '\'' +
                ", chapter_id='" + chapter_id + '\'' +
                ", next_chapter='" + next_chapter + '\'' +
                ", last_chapter='" + last_chapter + '\'' +
                ", is_preview=" + is_preview +
                ", image_list=" + image_list +
                '}';
    }
}
