package com.dazhongmianfei.dzmfreader.comic.been;

import android.content.ContentValues;

import com.dazhongmianfei.dzmfreader.book.been.BaseBook;
import com.dazhongmianfei.dzmfreader.utils.MyToash;

import org.litepal.LitePal;
import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import java.io.Serializable;

public class BaseComic extends LitePalSupport implements Serializable, Comparable<BaseComic> {
    private long id;
    @Column(unique = true, defaultValue = "0")
    private String comic_id;//": 26470, //漫画id
    private String name;//": "一拳超人", //漫画名
    @Column(ignore = true)
    private int finished;//": "完结", //完结状态
    @Column(ignore = true)
    private String flag;//": "限免",   //角标
    //   @Column(ignore = true)
    // private String cover;//": "http://cover.u17i.com/2014/07/4431881_1405044822_6XsM9VnAV7nY.sbig.jpg", //封面
    @Column(ignore = true)
    public String horizontal_cover;//"": "http://i0.hdslb.com/bfs/manga-static/4d2502dd1540470c91b5a147edc7917cacfd79a2.png", //水平封面


    public String vertical_cover;//"": "http://i0.hdslb.com/bfs/manga-static/4761ca39b699bb2738c326ce812c6c56ee3b1458.jpg", //竖封面
    @Column(ignore = true)
    private String last_chapter;//": "番外7", //最新章节
    @Column(ignore = true)
    private String last_chapter_id;//": "853495", //最新章节id
    private int total_chapters;//": 48, //总章节数

    @Column(ignore = true)
    private String fav_time;//": "1554976184", //收藏时间
    @Column(ignore = true)
    private String issue_time;//": "2018年", //发布时间
    @Column(ignore = true)
    private String sort_id;//": 1 //分类id

    private String description;


    private String author;//"": "黎明C", //作者

    @Column(ignore = true)
    private int recentChapter;//最近更新章节数


    private String current_chapter_id;//最近阅读章节ID

    private int current_display_order;//最近阅读章节序号

    private String current_chapter_name;//最近阅读章节name

    private String Chapter_text;//本书的目录数据的MD5

    private String last_chapter_time;

    @Column(defaultValue = "none")
    private String uid;

    private boolean isAddBookSelf;//是否加入书架


    private int down_chapters;//下载过章节数 等于0  表示没有下载过


    public int getDown_chapters() {
        return down_chapters;
    }

    public void setDown_chapters(int down_chapters) {
        this.down_chapters = down_chapters;
    }

    public int getCurrent_display_order() {
        return current_display_order;
    }

    public void setCurrent_display_order(int current_display_order) {
        this.current_display_order = current_display_order;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCurrent_chapter_name() {
        return current_chapter_name;
    }

    public void setCurrent_chapter_name(String current_chapter_name) {
        this.current_chapter_name = current_chapter_name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getRecentChapter() {
        return recentChapter;
    }

    public void setRecentChapter(int recentChapter) {
        this.recentChapter = recentChapter;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getCurrent_chapter_id() {
        return current_chapter_id;
    }

    public void setCurrent_chapter_id(String current_chapter_id) {
        this.current_chapter_id = current_chapter_id;
    }


    public String getChapter_text() {
        return Chapter_text;
    }

    public void setChapter_text(String chapter_text) {
        Chapter_text = chapter_text;
    }

    public String getLast_chapter_time() {
        return last_chapter_time;
    }

    public void setLast_chapter_time(String last_chapter_time) {
        this.last_chapter_time = last_chapter_time;
    }

    @Override
    public boolean save() {
        return super.save();

    }
/*
*
* 使用的insert()方法来存储数据时是有返回值的，返回的是插入行对应的id也就是行号。但LitePal中的save()方法返回的是布尔值，那么我们怎样才能拿到存储成功之后这条数据对应的id呢？对此，LitePal使用了一种非常巧妙的做法，还记得我们在每个实体类中都定义了一个id字段吗？当调用save()方法或saveThrows()方法存储成功之后，LitePal会自动将该条数据对应的id赋值到实体类的id字段上。
---------------------

来源：CSDN
原文：https://blog.csdn.net/gpf1320253667/article/details/82819795
版权声明：本文为博主原创文章，转载请附上博文链接！
*
* */

    public boolean saveIsexist(boolean addbook) {

        if (id == 0) {
            isAddBookSelf = addbook;
            MyToash.Log("saveIsexist",id+"  "+isAddBookSelf);
            return super.save();
        } else {
            if (addbook) {
                isAddBookSelf=true;
                ContentValues values = new ContentValues();
                values.put("isAddBookSelf", true);
                LitePal.update(BaseBook.class, values,id);
            }
        }
        MyToash.Log("saveIsexist",id+"  "+isAddBookSelf);
        return  isAddBookSelf;
    }


    public String getComic_id() {
        return comic_id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isAddBookSelf() {
        return isAddBookSelf;
    }

    public void setAddBookSelf(boolean addBookSelf) {
        isAddBookSelf = addBookSelf;
    }

    public void setComic_id(String comic_id) {
        this.comic_id = comic_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getFinished() {
        return finished;
    }

    public void setFinished(int finished) {
        this.finished = finished;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

/*    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }*/

    public String getLast_chapter() {
        return last_chapter;
    }

    public void setLast_chapter(String last_chapter) {
        this.last_chapter = last_chapter;
    }

    public String getLast_chapter_id() {
        return last_chapter_id;
    }

    public void setLast_chapter_id(String last_chapter_id) {
        this.last_chapter_id = last_chapter_id;
    }

    public int getTotal_chapters() {
        return total_chapters;
    }

    public void setTotal_chapters(int total_chapters) {
        this.total_chapters = total_chapters;
    }


    public String getFav_time() {
        return fav_time;
    }

    public void setFav_time(String fav_time) {
        this.fav_time = fav_time;
    }

    public String getIssue_time() {
        return issue_time;
    }

    public void setIssue_time(String issue_time) {
        this.issue_time = issue_time;
    }

    public String getSort_id() {
        return sort_id;
    }

    public void setSort_id(String sort_id) {
        this.sort_id = sort_id;
    }

    private int bookselfPosition;

    public int getBookselfPosition() {
        return bookselfPosition;
    }

    public void setBookselfPosition(int bookselfPosition) {
        this.bookselfPosition = bookselfPosition;
    }


    @Override
    public int compareTo(BaseComic baseCartoon) {
        return baseCartoon.bookselfPosition - this.bookselfPosition;
    }

    public String getHorizontal_cover() {
        return horizontal_cover;
    }

    public void setHorizontal_cover(String horizontal_cover) {
        this.horizontal_cover = horizontal_cover;
    }

    public String getVertical_cover() {
        return vertical_cover;
    }

    public void setVertical_cover(String vertical_cover) {
        this.vertical_cover = vertical_cover;
    }

    @Override
    public boolean equals( Object obj) {
        return this.comic_id.equals(((BaseComic)obj).getComic_id());
    }

    @Override
    public String toString() {
        return "BaseComic{" +
                "id=" + id +
                ", comic_id='" + comic_id + '\'' +
                ", name='" + name + '\'' +
                ", finished=" + finished +
                ", flag='" + flag + '\'' +
                ", horizontal_cover='" + horizontal_cover + '\'' +
                ", vertical_cover='" + vertical_cover + '\'' +
                ", last_chapter='" + last_chapter + '\'' +
                ", last_chapter_id='" + last_chapter_id + '\'' +
                ", total_chapters=" + total_chapters +
                ", fav_time='" + fav_time + '\'' +
                ", issue_time='" + issue_time + '\'' +
                ", sort_id='" + sort_id + '\'' +
                ", description='" + description + '\'' +
                ", author='" + author + '\'' +
                ", recentChapter=" + recentChapter +
                ", current_chapter_id='" + current_chapter_id + '\'' +
                ", current_display_order=" + current_display_order +
                ", current_chapter_name='" + current_chapter_name + '\'' +
                ", Chapter_text='" + Chapter_text + '\'' +
                ", last_chapter_time='" + last_chapter_time + '\'' +
                ", uid='" + uid + '\'' +
                ", isAddBookSelf=" + isAddBookSelf +
                ", down_chapters=" + down_chapters +
                ", bookselfPosition=" + bookselfPosition +
                '}';
    }

    @Override
    public int hashCode() {
        return this.comic_id.hashCode();
    }
}
