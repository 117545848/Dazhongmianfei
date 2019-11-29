package com.dazhongmianfei.dzmfreader.bean;

import java.util.List;

public class OptionItem {
    public int total_page;//": 3,
    public int current_page;//": 2,
    public int page_size;//": 2,
    public int total_count;//,
    public List<OptionBeen> list;

    public int getTotal_page() {
        return total_page;
    }

    public void setTotal_page(int total_page) {
        this.total_page = total_page;
    }

    public int getCurrent_page() {
        return current_page;
    }

    public void setCurrent_page(int current_page) {
        this.current_page = current_page;
    }

    public int getPage_size() {
        return page_size;
    }

    public void setPage_size(int page_size) {
        this.page_size = page_size;
    }

    public int getTotal_count() {
        return total_count;
    }

    public void setTotal_count(int total_count) {
        this.total_count = total_count;
    }

    public List<OptionBeen> getList() {
        return list;
    }

    public void setList(List<OptionBeen> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "DiscoveryItem{" +
                "total_page=" + total_page +
                ", current_page=" + current_page +
                ", page_size=" + page_size +
                ", total_count=" + total_count +
                ", list=" + list +
                '}';
    }
}
