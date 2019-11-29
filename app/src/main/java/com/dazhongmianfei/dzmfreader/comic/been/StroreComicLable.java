package com.dazhongmianfei.dzmfreader.comic.been;

import com.dazhongmianfei.dzmfreader.bean.BaseAd;
import com.dazhongmianfei.dzmfreader.bean.BaseTag;

import java.util.List;

public class StroreComicLable extends BaseAd {
    public String recommend_id;//": 29, //推荐位id
    public String label;//": "超人气作品", //名称
    public int style;//": 1, //展示风格
    public String can_more;//": true, //是否有更多 true有，false没有
    public  String  can_refresh;
    public int total;//": 15, //漫画数
    public List<Comic> list;

    public String getRecommend_id() {
        return recommend_id;
    }

    public void setRecommend_id(String recommend_id) {
        this.recommend_id = recommend_id;
    }

    public String getLabel() {
        return label;
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

    public String getCan_more() {
        return can_more;
    }

    public void setCan_more(String can_more) {
        this.can_more = can_more;
    }

    public String isCan_refresh() {
        return can_refresh;
    }

    public void setCan_refresh(String can_refresh) {
        this.can_refresh = can_refresh;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<Comic> getList() {
        return list;
    }

    public void setList(List<Comic> list) {
        this.list = list;
    }

    public static class Comic {
        public String comic_id;//": 10086, //漫画id
        public String name;//"": "乔乔的奇妙冒险", //漫画名称
        public String horizontal_cover;//"": "http://i0.hdslb.com/bfs/manga-static/4d2502dd1540470c91b5a147edc7917cacfd79a2.png", //水平封面
        public String vertical_cover;//"": "http://i0.hdslb.com/bfs/manga-static/4761ca39b699bb2738c326ce812c6c56ee3b1458.jpg", //竖封面

        public String author;//"": "黎明C", //作者
        public String description;//"": "美貌千金与帅气王爷", //描述
        public int is_finish;//"": 1, //是否完结 1已完结 0连载中
        public String flag;//"": "更新至32话", //角标
        public List<BaseTag> tag;
        public String hot_num;
        public String total_favors;
        public int total_comment;
        public int total_chapters;


        @Override
        public String toString() {
            return "Comic{" +
                    "comic_id='" + comic_id + '\'' +
                    ", name='" + name + '\'' +
                    ", horizontal_cover='" + horizontal_cover + '\'' +
                    ", vertical_cover='" + vertical_cover + '\'' +
                    ", author='" + author + '\'' +
                    ", description='" + description + '\'' +
                    ", is_finish=" + is_finish +
                    ", flag='" + flag + '\'' +
                    ", tag=" + tag +
                    ", hot_num='" + hot_num + '\'' +
                    '}';
        }
    }
}
