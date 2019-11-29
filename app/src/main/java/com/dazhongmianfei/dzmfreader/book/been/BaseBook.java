package com.dazhongmianfei.dzmfreader.book.been;


import android.content.ContentValues;

import com.dazhongmianfei.dzmfreader.comic.been.BaseComic;
import com.dazhongmianfei.dzmfreader.utils.MyToash;

import org.litepal.LitePal;
import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import java.io.Serializable;


/**
 * 书架的书籍
 * Created by scb on 2018/7/14.
 */
public class BaseBook extends LitePalSupport implements Serializable, Comparable<BaseBook> {

    /**
     * book_id : 22
     * name : 我是真校草
     * cover : http://xiongzhangzhuishu.oss-cn-beijing.aliyuncs.com/cover/9dbcb07321a05ac61cac3a8aec1f0067.jpg
     * author : 周星星
     */
    //name是唯一的，且默认值为unknown
    @Column(unique = true, defaultValue = "0")
    private String book_id;
    private String name;
    private String cover;
    private String author;
    private int recentChapter;
    private String allPercent;

    private int total_chapter;


    private long id;
    private String current_chapter_id;
    private int current_chapter_displayOrder;

    private String Chapter_text;
    private String last_chapter_time;

    private int bookselfPosition;

    public int getBookselfPosition() {
        return bookselfPosition;
    }

    public void setBookselfPosition(int bookselfPosition) {
        this.bookselfPosition = bookselfPosition;
    }

    public String getLast_chapter_time() {
        return last_chapter_time;
    }

    public void setLast_chapter_time(String last_chapter_time) {
        this.last_chapter_time = last_chapter_time;
    }

    public String getChapter_text() {
        return Chapter_text;
    }

    public void setChapter_text(String chapter_text) {
        Chapter_text = chapter_text;
    }

    public String getAllPercent() {
        return allPercent;
    }

    public void setAllPercent(String allPercent) {
        this.allPercent = allPercent;
    }

    public int getCurrent_chapter_displayOrder() {
        return current_chapter_displayOrder;
    }

    public void setCurrent_chapter_displayOrder(int current_chapter_displayOrder) {
        this.current_chapter_displayOrder = current_chapter_displayOrder;
    }

    @Override
    public boolean equals(Object obj) {

        if (obj instanceof BaseBook) {
            return (book_id.equals(((BaseBook) obj).book_id));
        }
        return super.equals(obj);


    }

    @Override
    public int hashCode() {
        return book_id.hashCode();
    }

    public int getTotal_Chapter() {
        return total_chapter;
    }

    public void setTotal_Chapter(int total_chapter) {
        this.total_chapter = total_chapter;
    }

    public int getRecentChapter() {

        return recentChapter;
    }

    public void setRecentChapter(int recentChapter) {
        this.recentChapter = recentChapter;
    }

    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBook_id() {
        return book_id;
    }

    public void setBook_id(String book_id) {
        this.book_id = book_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }


    /**
     * 用户唯一标识，默认为none
     */
    @Column(defaultValue = "none")
    private String uid;

    public String getCurrent_chapter_id() {
        return current_chapter_id;
    }

    public void setCurrent_chapter_id(String current_chapter_id) {
        this.current_chapter_id = current_chapter_id;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }


    private int isAddBookSelf;

    public int isAddBookSelf() {
        return isAddBookSelf;
    /*    if (isAddBookSelf) {
            return isAddBookSelf;
        } else {
            BaseBook baseBook = LitePal.where("book_id=?", book_id).findFirst(BaseBook.class);
            if (baseBook == null) {
                return false;
            } else {
                return baseBook.isAddBookSelf;
            }
        }*/
    }

    public void setAddBookSelf(int  addBookSelf) {
        isAddBookSelf = addBookSelf;
    }

    @Override
    public boolean isSaved() {
        return super.isSaved();
    }
    /*
    @Override
    public boolean isSaved() {
        if (isAddBookSelf) {
            return true;
        }
        try {
            if (!isSave) {
                BaseBook baseBook = LitePal.where("book_id=?", book_id).findFirst(BaseBook.class);
                if (baseBook == null) {
                    isSave = false;
                } else {
                    isSave = baseBook.isAddBookSelf;
                    isAddBookSelf = baseBook.isAddBookSelf;
                }
            }
        } catch (Exception e) {
            isSave = false;
        }
        return isSave;
    }*/

    @Override
    public boolean save() {

        isAddBookSelf = 1;
        return super.save();

    }

    public boolean saveIsexist(int addbook) {
        MyToash.Log("isAddBookSelf00", isAddBookSelf + "  " + addbook+"  "+id);
        if (id == 0) {
            isAddBookSelf = addbook;
            try {
                return super.save();
            } catch (Exception e) {
                ContentValues values = new ContentValues();
                values.put("isAddBookSelf", 1);
                LitePal.update(BaseBook.class, values, id);
                return true;
            }
        } else {
            if (addbook==1) {
                ContentValues values = new ContentValues();
                values.put("isAddBookSelf", 1);
                LitePal.update(BaseBook.class, values,id);
            }
        }
       // MyToash.Log("isAddBookSelf00", isAddBookSelf + "  " + addbook+"  "+id);
        return isAddBookSelf==1;
    }


    @Override
    public String toString() {
        return "BaseBook{" +
                "book_id='" + book_id + '\'' +
                ", name='" + name + '\'' +
                ", cover='" + cover + '\'' +
                ", author='" + author + '\'' +
                ", recentChapter=" + recentChapter +
                ", allPercent='" + allPercent + '\'' +
                ", total_chapter=" + total_chapter +
                ", id=" + id +
                ", current_chapter_id='" + current_chapter_id + '\'' +
                ", current_chapter_displayOrder=" + current_chapter_displayOrder +
                ", Chapter_text='" + Chapter_text + '\'' +
                ", description='" + description + '\'' +
                ", uid='" + uid + '\'' +

                '}';
    }

    @Override
    public int compareTo(BaseBook baseBook) {
        return baseBook.bookselfPosition - this.bookselfPosition;
    }
}
