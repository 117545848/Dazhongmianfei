package com.dazhongmianfei.dzmfreader.bean;

/**
 * 章节内容
 * Created by scb on 2018/7/15.
 */
public class ChapterContent {

    /**
     * chapter_id : 2
     * content : 记着，某月某日我很失落，失落时我总喜欢站在山上看夕阳西下的一瞬间，转过头对今天的辛酸苦辣一笑而过。\n  还记着，某月某日她对我说：你太痴情，一个在乎你的人你不珍惜，偏偏珍惜一个快渐渐遗忘了你的人。\n  我说：或许是吧！也许是我太傻太笨，我已对她立下了誓言，给下了承诺。
     * is_preview : 0
     */
    private String  chapter_title;

    private String content;
    private String is_preview;


    private String  last_chapter;
    private String  next_chapter;
    private String chapter_id;
    private int words;
    private String update_time;

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public int getWords() {
        return words;
    }

    public void setWords(int words) {
        this.words = words;
    }

    public String getLast_chapter() {
        return last_chapter;
    }

    public void setLast_chapter(String last_chapter) {
        this.last_chapter = last_chapter;
    }

    public String getNext_chapter() {
        return next_chapter;
    }

    public void setNext_chapter(String next_chapter) {
        this.next_chapter = next_chapter;
    }

    public String getChapter_title() {
        return chapter_title;
    }

    public void setChapter_title(String chapter_title) {
        this.chapter_title = chapter_title;
    }

    public String getChapter_id() {
        return chapter_id;
    }

    public void setChapter_id(String chapter_id) {
        this.chapter_id = chapter_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getIs_preview() {
        return is_preview;
    }

    @Override
    public String toString() {
        return "ChapterContent{" +
                "chapter_title='" + chapter_title + '\'' +

                ", is_preview='" + is_preview + '\'' +
                ", last_chapter='" + last_chapter + '\'' +
                ", next_chapter='" + next_chapter + '\'' +
                ", chapter_id='" + chapter_id + '\'' +
                ", content='" + content + '\'' +
                '}';
    }

    public void setIs_preview(String is_preview) {
        this.is_preview = is_preview;
    }
}
