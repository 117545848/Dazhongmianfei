package com.dazhongmianfei.dzmfreader.book.been;

import com.dazhongmianfei.dzmfreader.bean.CommentItem;

import java.util.List;

public class BookComment {

    public int total_page;//": 4,
    public int current_page;//": 1,
    public int page_size;//": 10,
    public int total_count;//": 36,
    public  List<CommentItem> list;
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
