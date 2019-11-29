package com.dazhongmianfei.dzmfreader.book.been;

import android.app.Activity;

import com.dazhongmianfei.dzmfreader.bean.BaseAd;
import com.dazhongmianfei.dzmfreader.bean.BaseTag;
import com.dazhongmianfei.dzmfreader.bean.LabelListItem;
import com.dazhongmianfei.dzmfreader.book.been.BaseBook;
import com.dazhongmianfei.dzmfreader.book.config.BookConfig;
import com.dazhongmianfei.dzmfreader.config.ReaderConfig;
import com.dazhongmianfei.dzmfreader.eventbus.RefreshReadHistory;
import com.dazhongmianfei.dzmfreader.http.ReaderParams;
import com.dazhongmianfei.dzmfreader.localapp.bean.Book;
import com.dazhongmianfei.dzmfreader.utils.HttpUtils;
import com.dazhongmianfei.dzmfreader.utils.InternetUtils;
import com.dazhongmianfei.dzmfreader.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;
//.http.RequestParams;

import java.util.List;

/**
 * Created by scb on 2019/1/11.
 * <p>
 * "total_page": 1,
 * "current_page": 1,
 * "page_size": 10,
 * "total_count": 1,
 * "list": [
 * {
 * "log_id": 776,  // 记录ID
 * "chapter_id": 6252, // 章节ID
 * "name": "我是真校草",   //书籍名称
 * "chapter_title": "第二章 校草的真正含义", // 章节标题
 * "book_id": 22,  // 书籍ID
 * "cover": "http://dlreader.oss-cn-hangzhou.aliyuncs.com/cover/9dbcb07321a05ac61cac3a8aec1f0067.jpg?x-oss-process=image%2Fresize%2Cw_143%2Ch_200%2Cm_lfit",   // 书籍封面图
 * "total_chapter": 64,    // 更新章节
 * "last_chapter_time": "更新于10天前"  // 更新时间
 * "ad_type": 0    // 0-阅读记录，大于0：1-穿山甲，2-百度，3-腾讯
 * }
 * ]
 */

public class ReadHistory {
    public int total_page;
    public int current_page;
    public int page_size;
    public int total_count;
    public List<ReadHistoryBook> list;
    //public List<Recomment> recomment;

    @Override
    public String toString() {
        return "ReadHistory{" +
                "total_page=" + total_page +
                ", current_page=" + current_page +
                ", page_size=" + page_size +
                ", total_count=" + total_count +
                ", list=" + list +
                '}';
    }

    /*  public static class Recomment {
          public String book_id;
          public String name;
          public String cover;

          @Override
          public String toString() {
              return "Recomment{" +
                      "book_id='" + book_id + '\'' +
                      ", name='" + name + '\'' +
                      ", cover='" + cover + '\'' +
                      ", author='" + author + '\'' +
                      ", description='" + description + '\'' +
                      ", tag=" + tag +
                      '}';
          }

          public String author;
          public String description;
          public List<BaseTag> tag;

      }
  */
    public static class ReadHistoryBook extends BaseAd {
        public String log_id;
        public String chapter_id;
        public String chapter_title;
        public int chapter_index;
        public String book_id;
        public String name;
        public String cover;
        public String description;
        public int total_chapter;
        public String last_chapter_time;

        public int getTotal_chapter() {
            return total_chapter;
        }

        public String getLast_chapter_time() {
            return last_chapter_time;
        }

        public void setLast_chapter_time(String last_chapter_time) {
            this.last_chapter_time = last_chapter_time;
        }

        public int getTotal_Chapter() {
            return total_chapter;
        }

        public void setTotal_chapter(int total_chapter) {
            this.total_chapter = total_chapter;
        }

        public String getLog_id() {
            return log_id;
        }

        public void setLog_id(String log_id) {
            this.log_id = log_id;
        }

        public String getChapter_id() {
            return chapter_id;
        }

        public void setChapter_id(String chapter_id) {
            this.chapter_id = chapter_id;
        }

        public String getChapter_title() {
            return chapter_title;
        }

        public void setChapter_title(String chapter_title) {
            this.chapter_title = chapter_title;
        }

        public int getChapter_index() {
            return chapter_index;
        }

        public void setChapter_index(int chapter_index) {
            this.chapter_index = chapter_index;
        }

        public int getAd_type() {
            return ad_type;
        }

        public void setAd_type(int ad_type) {
            this.ad_type = ad_type;
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

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }

    public static void addReadHistory(boolean FORM_READHISTORY, final Activity activity, final String bookid, String chapterid) {
        if (!InternetUtils.internet(activity) || !Utils.isLogin(activity) || bookid.contains("/")) {
            return;
        }
        ReaderParams params = new ReaderParams(activity);
        params.putExtraParams("book_id", bookid);
        params.putExtraParams("chapter_id", chapterid);
        String json = params.generateParamsJson();
        HttpUtils.getInstance(activity).sendRequestRequestParams3(BookConfig.add_read_log, json, false, new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(String result) {
                        if (FORM_READHISTORY) {
                            EventBus.getDefault().post(new RefreshReadHistory(true));
                        }

                    }

                    @Override
                    public void onErrorResponse(String ex) {

                    }
                }

        );

    }

}
