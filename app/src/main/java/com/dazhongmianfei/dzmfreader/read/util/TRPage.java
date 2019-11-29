package com.dazhongmianfei.dzmfreader.read.util;

import java.util.List;

/**
 * Created by Administrator on 2016/8/11 0011.
 */
public class TRPage {
    private long begin;
    private long end;
    private List<String> lines;
    private String chapter_id;

    public String getChapter_id() {
        return chapter_id;
    }

    public void setChapter_id(String chapter_id) {
        this.chapter_id = chapter_id;
    }

    public long getBegin() {
        return begin;
    }

    public void setBegin(long begin) {
        this.begin = begin;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    public List<String> getLines() {
        return lines;
    }

    public String getLineToString() {
        String text = "";
        if (lines != null) {
            for (String line : lines) {
                text += line;
            }
        }
        return text;
    }

    public void setLines(List<String> lines) {
        this.lines = lines;
    }
}
