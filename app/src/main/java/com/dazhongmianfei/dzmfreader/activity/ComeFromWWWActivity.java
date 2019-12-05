package com.dazhongmianfei.dzmfreader.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;


import com.alibaba.sdk.android.push.AndroidPopupActivity;
import com.dazhongmianfei.dzmfreader.R;
;

//.view.annotation.ContentView;
//.x;

import java.util.Map;

/**
 * Created by abc on 2016/11/4.
 */
//dazhongmianfei://xiaoshuo.com/openApp
public class ComeFromWWWActivity extends AndroidPopupActivity {
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Intent intent = new Intent();
            if (book_id == null && comic_id == null) {
                intent.setClass(ComeFromWWWActivity.this, SplashActivity.class);
            } else if (book_id != null) {
                intent.setClass(ComeFromWWWActivity.this, BookInfoActivity.class);
                intent.putExtra("book_id", book_id);
            }
            startActivity(intent);
            finish();
        }
    };
    String book_id;
    String comic_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comefromwww);
        try {
            Intent intent = getIntent();
            String action = intent.getAction();
            if (action != null && Intent.ACTION_VIEW.equals(action)) {
                Uri uri = intent.getData();
                if (uri != null) {
                    try {
                        if (uri.getQueryParameter("book_id") != null) {
                            book_id = uri.getQueryParameter("book_id");
                        } else if (uri.getQueryParameter("comic_id") != null) {
                            comic_id = uri.getQueryParameter("comic_id");
                        }
                    } catch (Exception e) {
                    }
                    handler.sendEmptyMessageDelayed(0, 0);
                }
            }
        } catch (Exception e) {
        }

    }

    @Override
    protected void onSysNoticeOpened(String title, String summary, Map<String, String> extraMap) {
        Intent intent = new Intent();
        if (extraMap == null || extraMap.isEmpty()) {
            intent.setClass(ComeFromWWWActivity.this, SplashActivity.class);
        } else if (extraMap.get("skip_type") != null && extraMap.get("content") != null) {
            String content = extraMap.get("content");
            switch (extraMap.get("skip_type")) {
                case "1":
                    intent.setClass(ComeFromWWWActivity.this, BookInfoActivity.class);
                    intent.putExtra("book_id", content);
                    break;
                case "2":
                    intent.setClass(ComeFromWWWActivity.this, AboutActivity.class);
                    intent.putExtra("url", content);
                    intent.putExtra("flag", "skip_url");
                    break;

                default:
                    intent.setClass(ComeFromWWWActivity.this, MainActivity.class);
                    break;
            }
        } else {
            intent.setClass(ComeFromWWWActivity.this, SplashActivity.class);
        }
        startActivity(intent);
        finish();
    }
}
