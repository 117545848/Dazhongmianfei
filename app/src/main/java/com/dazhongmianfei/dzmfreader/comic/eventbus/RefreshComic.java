package com.dazhongmianfei.dzmfreader.comic.eventbus;

import com.dazhongmianfei.dzmfreader.comic.been.BaseComic;

import java.util.List;

public class RefreshComic {
    public List<BaseComic> baseComics;
    public BaseComic  baseComic;


    public boolean flag;

    public int  ADD=-1;
    public RefreshComic(List<BaseComic> baseBooks) {
        this.baseComics = baseBooks;
    }

    public RefreshComic(BaseComic baseComic,int  ADD) {//添加 或  取消收藏
        this.baseComic = baseComic;
        this.ADD = ADD;
    }

    public RefreshComic(boolean flag) {
        this.flag = flag;
    }
}
