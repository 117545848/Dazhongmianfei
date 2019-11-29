package com.dazhongmianfei.dzmfreader.comic.eventbus;

import com.dazhongmianfei.dzmfreader.comic.been.ComicChapter;

public class ComicChapterEventbus {
    public int Flag;
    public ComicChapter comicChapter;

    public ComicChapterEventbus(int flag, ComicChapter comicChapter) {
        Flag = flag;
        this.comicChapter = comicChapter;
    }
}
