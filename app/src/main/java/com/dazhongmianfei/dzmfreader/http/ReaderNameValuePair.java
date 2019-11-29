package com.dazhongmianfei.dzmfreader.http;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 封装一层，根据key value转化json
 * Created by scb on 2018/5/27.
 */
public class ReaderNameValuePair {

    List<Map<String, String>> paramsList;
    HashMap<String, String> map;
    Gson gson;

    public ReaderNameValuePair() {
        paramsList = new ArrayList<>();
        map = new HashMap<>();
        gson = new Gson();
    }

    public void put(String key, String value) {
        map.put(key, value);
    }

    public String toJson() {
        paramsList.clear();
        paramsList.add(map);
        String json = gson.toJson(paramsList);
        return json.substring(1, json.length() - 1);
    }

}
