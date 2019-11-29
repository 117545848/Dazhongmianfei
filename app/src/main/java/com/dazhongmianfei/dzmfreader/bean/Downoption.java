package com.dazhongmianfei.dzmfreader.bean;

import com.dazhongmianfei.dzmfreader.book.been.BaseBook;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import java.io.Serializable;

public class Downoption extends LitePalSupport implements Serializable, Comparable<Downoption> {
    @Override
    public String toString() {
        return "Downoption{" +
                "label='" + label + '\'' +
                ", tag='" + tag + '\'' +
                ", s_chapter='" + s_chapter + '\'' +
                ", down_num=" + down_num +
                ", file_name='" + file_name + '\'' +
                ", isdown=" + isdown +
                ", book_id='" + book_id + '\'' +
                ", cover='" + cover + '\'' +
                ", bookname='" + bookname + '\'' +
                ", downoption_size='" + downoption_size + '\'' +
                ", downoption_date='" + downoption_date + '\'' +
                ", start_order=" + start_order +
                ", end_order=" + end_order +
                ", down_cunrrent_num=" + down_cunrrent_num +
                '}';
    }

    public String label;
    public String tag;//": "免费", //标签 值为空时不展示
    public String s_chapter;//'": "50238", //开始章节id 调用下载接口时传参使用
    public int down_num;//": 18 //下载章节数 调用下载接口时传参使用
    public int down_cunrrent_num;//当前以及下载的章节
    @Column(unique = true, defaultValue = "0")
    public String file_name;//唯一ID
    public boolean isdown;//是否下载

    //关联的书籍
    public String book_id;
    public String cover;
    public String bookname;
    public String description;


    public String downoption_size;
    public String downoption_date;
    public int start_order;
    public int end_order;
    public boolean showHead;

    @Override
    public boolean equals(Object obj) {

        if (obj instanceof BaseBook) {
            return (file_name.equals(((Downoption) obj).file_name));
        }
        return super.equals(obj);


    }

    @Override
    public int hashCode() {
        return file_name.hashCode();
    }

    @Override
    public int compareTo(Downoption downoption) {
/*        int flag1=downoption.book_id.compareTo(this.book_id);
        int flag2=downoption.start_order-this.start_order;
        int flag3=downoption.end_order-this.end_order;*/

        int flag1 = this.book_id.compareTo(downoption.book_id);
        int flag2 = this.start_order - downoption.start_order;
        int flag3 = this.end_order - downoption.end_order;
        int flag4 =this.downoption_date.compareTo(downoption.downoption_date);
        return (flag1 != 0) ? flag4 : ((flag2 != 0) ? flag2 : ((flag3 != 0) ? flag3 : 0));
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getS_chapter() {
        return s_chapter;
    }

    public void setS_chapter(String s_chapter) {
        this.s_chapter = s_chapter;
    }

    public int getDown_num() {
        return down_num;
    }

    public void setDown_num(int down_num) {
        this.down_num = down_num;
    }

    public int getDown_cunrrent_num() {
        return down_cunrrent_num;
    }

    public void setDown_cunrrent_num(int down_cunrrent_num) {
        this.down_cunrrent_num = down_cunrrent_num;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public boolean isIsdown() {
        return isdown;
    }

    public void setIsdown(boolean isdown) {
        this.isdown = isdown;
    }

    public String getBook_id() {
        return book_id;
    }

    public void setBook_id(String book_id) {
        this.book_id = book_id;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getBookname() {
        return bookname;
    }

    public void setName(String bookname) {
        this.bookname = bookname;
    }

    public String getDownoption_size() {
        return downoption_size;
    }

    public void setDownoption_size(String downoption_size) {
        this.downoption_size = downoption_size;
    }

    public String getDownoption_date() {
        return downoption_date;
    }

    public void setDownoption_date(String downoption_date) {
        this.downoption_date = downoption_date;
    }

    public int getStart_order() {
        return start_order;
    }

    public void setStart_order(int start_order) {
        this.start_order = start_order;
    }

    public int getEnd_order() {
        return end_order;
    }

    public void setEnd_order(int end_order) {
        this.end_order = end_order;
    }

    public boolean isShowHead() {
        return showHead;
    }

    public void setShowHead(boolean showHead) {
        this.showHead = showHead;
    }
}
