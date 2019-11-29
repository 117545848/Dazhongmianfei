package com.dazhongmianfei.dzmfreader.eventbus;

import com.dazhongmianfei.dzmfreader.book.been.BaseBook;

import java.util.List;

public class RefreshBookSelf {
    public List<BaseBook> baseBooks;
    public boolean flag;
    public RefreshBookSelf(List<BaseBook> baseBooks) {
        this.baseBooks = baseBooks;
    }

    public RefreshBookSelf(boolean flag) {
        this.flag = flag;
    }
}
