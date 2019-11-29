package com.dazhongmianfei.dzmfreader.bean;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import java.io.Serializable;

/**
 * 章节目录bean
 */
public class ChapterItem extends LitePalSupport implements Serializable {

    @Column(unique = true, defaultValue = "0")
    String chapter_id;
    String chapter_title;
    String is_vip;
    int display_order;
    String is_preview;
    String update_time;
    String next_chapter_id;
    String pre_chapter_id;

    //存数据库所需字段
    String book_id;
    long chapteritem_begin;
    String chapter_path;
    String charset;
    String book_name;
    String chapter_uid;
    @Column(ignore = true)
    String chaptertab;
    @Column(ignore = true)
    String chaptercolor;

    public String getChaptertab() {
        return chaptertab;
    }

    public void setChaptertab(String chaptertab) {
        this.chaptertab = chaptertab;
    }

    public String getChaptercolor() {
        return chaptercolor;
    }

    public void setChaptercolor(String chaptercolor) {
        this.chaptercolor = chaptercolor;
    }

    public String getChapter_uid() {
        return chapter_uid;
    }

    public void setChapter_uid(String chapter_uid) {
        this.chapter_uid = chapter_uid;
    }

    public boolean equals(Object object) {
        if (object instanceof ChapterItem) {
            ChapterItem p = (ChapterItem) object;
            if (p.getChapter_id() == null || chapter_id == null) {
                return false;
            } else {
                return chapter_id.equalsIgnoreCase(p.getChapter_id());
            }
        }
        return false;
    }

    public int hashCode() {
        return chapter_id.hashCode();

    }



    public long getChapteritem_begin() {
        return chapteritem_begin;
    }

    public void setChapteritem_begin(long chapteritem_begin) {
        this.chapteritem_begin = chapteritem_begin;
    }
    public ChapterItem() {

    }
    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public long getBegin() {
        return chapteritem_begin;
    }

    public void setBegin(long begin) {
        this.chapteritem_begin = begin;
    }

    public String getChapter_path() {
        return chapter_path;
    }

    public void setChapter_path(String chapter_path) {
        this.chapter_path = chapter_path;
    }

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

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public String getIs_preview() {
        return is_preview;
    }

    public void setIs_preview(String is_preview) {
        this.is_preview = is_preview;
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



    public String getIs_vip() {
        return is_vip;
    }

    public void setIs_vip(String is_vip) {
        this.is_vip = is_vip;
    }


    /*  public Tag getTag() {
          return tag;
      }

      public void setTag(Tag tag) {
          this.tag = tag;
      }
  */
    public int getDisplay_order() {
        return display_order;
    }

    public void setDisplay_order(int display_order) {
        this.display_order = display_order;
    }

    public String getNext_chapter_id() {
        return next_chapter_id;
    }

    public void setNext_chapter_id(String next_chapter_id) {
        this.next_chapter_id = next_chapter_id;
    }

    public String getPre_chapter_id() {
        return pre_chapter_id;
    }

    public void setPre_chapter_id(String pre_chapter_id) {
        this.pre_chapter_id = pre_chapter_id;
    }
}
