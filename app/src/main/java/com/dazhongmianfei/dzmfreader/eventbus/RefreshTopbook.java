package com.dazhongmianfei.dzmfreader.eventbus;

public class RefreshTopbook {
    public String book_id;
    public String allPercent;
    public boolean flag;
    public String current_chapter_id;

    public RefreshTopbook(String book_id, String allPercent) {//刷新顶部书架
        this.book_id = book_id;
        this.allPercent = allPercent;
    }

    public RefreshTopbook(String book_id, String current_chapter_id,boolean flag ) {//刷新书籍集合的最近阅读章节
        this.book_id = book_id;
        this.current_chapter_id = current_chapter_id;
        this.flag = flag;
    }
}
