package com.dazhongmianfei.dzmfreader.bean;

import com.dazhongmianfei.dzmfreader.book.been.StroreBookcLable;
import com.dazhongmianfei.dzmfreader.comic.been.ComicComment;

import java.util.List;

public class InfoBookItem {
    public  InfoBook book;
    public   List<BookInfoComment> comment;
    public   List<  StroreBookcLable > label;
    public  BaseAd advert;

    public BaseAd getAdvert() {
        return advert;
    }

    public void setAdvert(BaseAd advert) {
        this.advert = advert;
    }

    public InfoBook getBook() {
        return book;
    }

    public void setBook(InfoBook book) {
        this.book = book;
    }

    public List<BookInfoComment> getComment() {
        return comment;
    }

    public void setComment(List<BookInfoComment> comment) {
        this.comment = comment;
    }

    public List<StroreBookcLable> getLabel() {
        return label;
    }

    public void setLabel(List<StroreBookcLable> label) {
        this.label = label;
    }
}
