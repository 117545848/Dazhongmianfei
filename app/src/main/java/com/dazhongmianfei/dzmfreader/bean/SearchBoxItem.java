package com.dazhongmianfei.dzmfreader.bean;

import java.util.List;

/**
 * 分类详情首页的搜索tab的item
 * Created by scb on 2018/6/28.
 */
public class SearchBoxItem {
    String label;
    String field;
    List<Condition> list;

    public static class Condition {
        public Condition(){

        }
        String display;
        String value;

        public String getDisplay() {
            return display;
        }

        public void setDisplay(String display) {
            this.display = display;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public List<Condition> getList() {
        return list;
    }

    public void setList(List<Condition> list) {
        this.list = list;
    }
}
