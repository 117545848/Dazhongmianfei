package com.dazhongmianfei.dzmfreader.bean;

import java.util.List;

public class BaoyueItem {
    public BaoyueUser user;
    public List<OptionBeen> list;

    public BaoyueUser getUser() {
        return user;
    }

    public void setUser(BaoyueUser user) {
        this.user = user;
    }

    public List<OptionBeen> getList() {
        return list;
    }

    public void setList(List<OptionBeen> list) {
        this.list = list;
    }
}
