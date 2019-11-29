package com.dazhongmianfei.dzmfreader.eventbus;

import com.dazhongmianfei.dzmfreader.book.been.BaseBook;

import java.util.List;

/**
 * Created by scb on 2018/8/8.
 */
public class RefreshList {

    public List<BaseBook> baseBooks;
    public RefreshList(List<BaseBook> baseBooks) {
        this.baseBooks = baseBooks;
    }
}
