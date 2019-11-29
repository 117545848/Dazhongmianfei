package com.dazhongmianfei.dzmfreader.activity;

import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.widget.TextView;

import com.dazhongmianfei.dzmfreader.utils.MyToash;

public class TestActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView textView = new TextView(this);
        textView.setText(getIntent().getStringExtra("openinstoe"));
        MyToash.ToashSuccess(TestActivity.this,getIntent().getStringExtra("openinstoe"));
        setContentView(textView);

    }
}
