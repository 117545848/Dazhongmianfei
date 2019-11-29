package com.dazhongmianfei.dzmfreader.book.been;

import com.dazhongmianfei.dzmfreader.bean.BaseAd;
import com.dazhongmianfei.dzmfreader.bean.BaseTag;
import com.dazhongmianfei.dzmfreader.localapp.bean.Book;

import java.util.List;

public class StroreBookcLable extends BaseAd {
    public String recommend_id;//": 29, //推荐位id
    public String label;//": "超人气作品", //名称
    public int style;//": 1, //展示风格
    public  int expire_time;
    public  boolean  can_more;
    public  boolean  can_refresh;

   // public String can_more;//": true, //是否有更多 true有，false没有
  //  public int total;//": 15, //漫画数
    public List<Book> list;

    public String getRecommend_id() {
        return recommend_id;
    }

    public void setRecommend_id(String recommend_id) {
        this.recommend_id = recommend_id;
    }

    public boolean isCan_refresh() {
        return can_refresh;
    }

    public void setCan_refresh(boolean can_refresh) {
        this.can_refresh = can_refresh;
    }

    public String getLabel() {
        return label;
    }

    public boolean isCan_more() {
        return can_more;
    }

    public int getExpire_time() {
        return expire_time;
    }

    public void setExpire_time(int expire_time) {
        this.expire_time = expire_time;
    }

    public void setCan_more(boolean can_more) {
        this.can_more = can_more;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getStyle() {
        return style;
    }

    public void setStyle(int style) {
        this.style = style;
    }



    public List<Book> getList() {
        return list;
    }

    public void setList(List<Book> list) {
        this.list = list;
    }

    public static class Book {
        public String book_id;//": 10086, //漫画id
        public String name;//"": "乔乔的奇妙冒险", //漫画名称
        public String cover;//"": "http://i0.hdslb.com/bfs/manga-static/4761ca39b699bb2738c326ce812c6c56ee3b1458.jpg", //竖封面

        public String author;//"": "黎明C", //作者
        public String description;//"": "美貌千金与帅气王爷", //描述
        public String is_finished;//"": 1, //是否完结 1已完结 0连载中
         public String flag;//"": "更新至32话", //角标
        public List<BaseTag> tag;
        // public String hot_num;
        // public String total_comment;
        //   public int total_chapters;


        public String getFlag() {
            return flag;
        }

        public void setFlag(String flag) {
            this.flag = flag;
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

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getIs_finished() {
            return is_finished;
        }

        public void setIs_finished(String is_finished) {
            this.is_finished = is_finished;
        }

        public List<BaseTag> getTag() {
            return tag;
        }

        public void setTag(List<BaseTag> tag) {
            this.tag = tag;
        }
    }
}
