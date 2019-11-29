package com.dazhongmianfei.dzmfreader.bean;

import com.dazhongmianfei.dzmfreader.book.been.BaseBook;

import java.util.List;

/**
 * "大神推荐" "新书力推" "宅男最爱" "小编最爱"的gridview的bean
 */
public class LabelListItem extends BaseBook {
    /**
     * 描述
     */
   // String description;
    /**
     * 书本状态：连载中／已完结
     */
    String finished;
    /**
     * 免费...
     */
    String flag;
    /**
     * 类型列表
     */
    List<Tag> tag;


    public static class Tag {
        /**
         * 类型：浪漫青春
         */
        String tab;
        /**
         * 颜色
         */
        String color;

        public String getTab() {
            return tab;
        }

        public void setTab(String tab) {
            this.tab = tab;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }
    }


/*    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }*/

    public String getFinished() {
        return finished;
    }

    public void setFinished(String finished) {
        this.finished = finished;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public List<Tag> getTag() {
        return tag;
    }

    public void setTag(List<Tag> tag) {
        this.tag = tag;
    }
}
