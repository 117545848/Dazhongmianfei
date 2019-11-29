package com.dazhongmianfei.dzmfreader.comic.been;

import com.dazhongmianfei.dzmfreader.bean.BaseAd;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import java.util.List;

public class BaseComicImage{
    public String comic_id;//": 26470, //漫画id
    public String chapter_id;//": 837923, //章节id
    public String image_id;//": 3576961, //图片id
    public String total_tucao;//"": 215, //图片吐槽数
    public String update_time;//"": 更新时间
    public int width;//"": "800", //宽带
    public int height;//"": "1123", //高度
    public String image;//"": "http://img6.u17i.com/16/04/121836/4486442_1461897703_bJPoyJjOYQSY.16a6f_svol.jpg", //图片地址
    public List<Tucao> tucao;

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

    public String getImage_id() {
        return image_id;
    }

    public void setImage_id(String image_id) {
        this.image_id = image_id;
    }

    public String getTotal_tucao() {
        return total_tucao;
    }

    public void setTotal_tucao(String total_tucao) {
        this.total_tucao = total_tucao;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public List<Tucao> getTucao() {
        return tucao;
    }

    public void setTucao(List<Tucao> tucao) {
        this.tucao = tucao;
    }

    public static class Tucao {
        public String tucao_id;//": 3576961, //图片id
        public String content;//"": 215, //图片吐槽数

        public String getTucao_id() {
            return tucao_id;
        }

        public void setTucao_id(String tucao_id) {
            this.tucao_id = tucao_id;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public Tucao() {
        }

        public Tucao(String content) {
            this.content = content;
        }

        @Override
        public String toString() {
            return "Tucao{" +
                    "tucao_id='" + tucao_id + '\'' +
                    ", content='" + content + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "BaseComicImage{" +
                "comic_id='" + comic_id + '\'' +
                ", chapter_id='" + chapter_id + '\'' +
                ", image_id='" + image_id + '\'' +
                ", total_tucao='" + total_tucao + '\'' +
                ", update_time='" + update_time + '\'' +
                ", width=" + width +
                ", height=" + height +
                ", image='" + image + '\'' +
                ", tucao=" + tucao +
                '}';
    }
}
