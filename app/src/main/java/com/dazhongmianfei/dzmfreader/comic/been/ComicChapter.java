package com.dazhongmianfei.dzmfreader.comic.been;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import java.io.Serializable;

public class ComicChapter extends LitePalSupport implements Serializable, Comparable<ComicChapter> {
    private long id;
    @Column(unique = true, defaultValue = "0")
    public String chapter_id;//": 837923, //章节id
    public String chapter_title;//": "0.预告", //章节名
    @Column(ignore = true)
    public String subtitle;//
    @Column(ignore = true)
    public int total_images;//"": 24, //章节总图片数
    @Column(ignore = true)
    public int vip_images;//"": 0, //收费图片数
    @Column(ignore = true)
    public String free_image_num;//"": 24, //免费图片数
    public String small_cover;//"": "http://cover2.u17i.com/chapter/2019/04/19/comic_chapter_1555645445_968.square.jpg", //小封面
    //@Column(ignore = true)
    public int display_order;//"": 1, //序号
    public int is_vip;//"": 0, //是否收费章节 1是 0不是
    //public int can_read;//"": 1, //当前用户是否可读 1可读 0不可读
    public int is_preview;//"": 0 //是否预览章节 1是 0不是
    public String updated_at;//最近更新时间
    public String last_chapter;//
    public String next_chapter;//

    public String display_label;

    public String getDisplay_labe() {
        return display_label;
    }

    public void setDisplay_labe(String display_labe) {
        this.display_label = display_labe;
    }

    //以下是数据库需要字段
    public String ComicChapterPath;
    public boolean IsRead;//是否阅读过
    public String comic_id;
    public String ImagesText;//该章节对应的 图片接口数据
    //public String ImagesTextMD5;//该章节对应的 图片接口数据的MD5
    public int current_read_img_order;//最近阅读的图片序号
  //  public int current_read_img_Y;//最近阅读的图片的Y 坐标值
    public String current_read_img_image_id;//最近阅读的图片ID


    public int getISDown() {
        return ISDown;
    }

    public int ISDown;//是否下载过

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getLast_chapter() {
        return last_chapter;
    }

    public void setLast_chapter(String last_chapter) {
        this.last_chapter = last_chapter;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNext_chapter() {
        return next_chapter;
    }

    public int isISDown() {
        return ISDown;
    }

    public void setISDown(int ISDown) {
        this.ISDown = ISDown;
    }

    public void setNext_chapter(String next_chapter) {
        this.next_chapter = next_chapter;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

/*    public String getImagesTextMD5() {
        return ImagesTextMD5;
    }

    public void setImagesTextMD5(String imagesTextMD5) {
        ImagesTextMD5 = imagesTextMD5;
    }*/

    public String getUpdate_time() {
        return updated_at;
    }

    public void setUpdate_time(String update_time) {
        this.updated_at = update_time;
    }

    public String getImagesText() {
        return ImagesText;
    }

    public void setImagesText(String imagesText) {
        ImagesText = imagesText;
    }

    public int getCurrent_read_img_order() {
        return current_read_img_order;
    }

    public void setCurrent_read_img_order(int current_read_img_order) {
        this.current_read_img_order = current_read_img_order;
    }

    public String getCurrent_read_img_image_id() {
        return current_read_img_image_id;
    }

    public void setCurrent_read_img_image_id(String current_read_img_image_id) {
        this.current_read_img_image_id = current_read_img_image_id;
    }

    public String getComic_chapter_id() {
        return chapter_id;
    }

    public void setComic_chapter_id(String comic_chapter_id) {
        this.chapter_id = comic_chapter_id;
    }

    public String getComic_id() {
        return comic_id;
    }

    public void setComic_id(String comic_id) {
        this.comic_id = comic_id;
    }


    public String getChapter_id() {
        return chapter_id;
    }

    public void setChapter_id(String chapter_id) {
        this.chapter_id = chapter_id;
    }

    public String getChapter_title() {
        return chapter_title;
    }

    public void setChapter_title(String chapter_title) {
        this.chapter_title = chapter_title;
    }

    public int getTotal_images() {
        return total_images;
    }

    public void setTotal_images(int total_images) {
        this.total_images = total_images;
    }

    public int getVip_images() {
        return vip_images;
    }

    public void setVip_images(int vip_images) {
        this.vip_images = vip_images;
    }

    public String getFree_image_num() {
        return free_image_num;
    }

    public void setFree_image_num(String free_image_num) {
        this.free_image_num = free_image_num;
    }

    public String getSmall_cover() {
        return small_cover;
    }

    public void setSmall_cover(String small_cover) {
        this.small_cover = small_cover;
    }

    public int getDisplay_order() {
        return display_order;
    }

    public void setDisplay_order(int display_order) {
        this.display_order = display_order;
    }

    public int getIs_vip() {
        return is_vip;
    }

    public void setIs_vip(int is_vip) {
        this.is_vip = is_vip;
    }

    public int getIs_preview() {
        return is_preview;
    }

    public void setIs_preview(int is_preview) {
        this.is_preview = is_preview;
    }

    public String getComicChapterPath() {
        return ComicChapterPath;
    }

    public void setComicChapterPath(String comicChapterPath) {
        ComicChapterPath = comicChapterPath;
    }

    public boolean isRead() {
        return IsRead;
    }

    public void setRead(boolean read) {
        IsRead = read;
    }

    @Override
    public String toString() {
        return "ComicChapter{" +
                "id=" + id +
                ", chapter_id='" + chapter_id + '\'' +
                ", chapter_title='" + chapter_title + '\'' +
                ", subtitle='" + subtitle + '\'' +
                ", total_images=" + total_images +
                ", vip_images=" + vip_images +
                ", free_image_num='" + free_image_num + '\'' +
                ", small_cover='" + small_cover + '\'' +
                ", display_order=" + display_order +
                ", is_vip=" + is_vip +
                ", is_preview=" + is_preview +
                ", updated_at='" + updated_at + '\'' +
                ", last_chapter='" + last_chapter + '\'' +
                ", next_chapter='" + next_chapter + '\'' +
                ", ComicChapterPath='" + ComicChapterPath + '\'' +
                ", IsRead=" + IsRead +
                ", comic_id='" + comic_id + '\'' +
                ", ImagesText='" + ImagesText + '\'' +
                ", current_read_img_order=" + current_read_img_order +
                ", current_read_img_image_id='" + current_read_img_image_id + '\'' +
                ", ISDown=" + ISDown +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        return this.chapter_id.equals(((ComicChapter) (obj)).chapter_id);
    }

    @Override
    public int hashCode() {
        return this.chapter_id.hashCode();
    }

    @Override
    public int compareTo(ComicChapter o) {
        return this.display_order - o.display_order;
    }
}
