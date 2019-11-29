package com.dazhongmianfei.dzmfreader.bean;

import java.util.List;

/**
 * Created by scb on 2018/7/7.
 */
public class RankItem {
    String rank_type;
    String list_name;
    String description;
    List<String> icon;

    public String getRank_type() {
        return rank_type;
    }

    public void setRank_type(String rank_type) {
        this.rank_type = rank_type;
    }

    public String getList_name() {
        return list_name;
    }

    public void setList_name(String list_name) {
        this.list_name = list_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getIcon() {
        return icon;
    }

    public void setIcon(List<String> icon) {
        this.icon = icon;
    }
}
