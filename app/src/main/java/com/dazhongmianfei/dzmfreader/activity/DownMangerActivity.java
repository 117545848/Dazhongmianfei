package com.dazhongmianfei.dzmfreader.activity;

import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.dazhongmianfei.dzmfreader.R;
import com.dazhongmianfei.dzmfreader.R2;
import com.dazhongmianfei.dzmfreader.adapter.DownMangerAdapter;
import com.dazhongmianfei.dzmfreader.bean.Downoption;
import com.dazhongmianfei.dzmfreader.utils.LanguageUtil;
import com.dazhongmianfei.dzmfreader.utils.MyToash;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.LitePal;
//.http.RequestParams;
//.view.annotation.ContentView;
//.view.annotation.Event;
//.view.annotation.ViewInject;
//.x;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by abc on 2016/11/4.
 */

public class DownMangerActivity extends BaseButterKnifeActivity {
    @Override
    public int initContentView() {
        return R.layout.activity_downmanger;
    }

    @BindView(R2.id.titlebar_back)
    public LinearLayout titlebar_back;
    @BindView(R2.id.titlebar_text)
    public TextView titlebar_text;
    @BindView(R2.id.activity_downmanger_list)
    public ListView activity_downmanger_list;
    @BindView(R2.id.fragment_bookshelf_noresult)
    public LinearLayout fragment_bookshelf_noresult;


    DownMangerAdapter downMangerAdapter;
    List<Downoption> downoptions;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);


        View temphead = LayoutInflater.from(this).inflate(R.layout.item_list_head, null);
        activity_downmanger_list.addHeaderView(temphead, null, false);
        activity_downmanger_list.setHeaderDividersEnabled(true);

        titlebar_text.setText(LanguageUtil.getString(this, R.string.BookInfoActivity_down_manger));
        downoptions = LitePal.where().find(Downoption.class);
        List<String> list = new ArrayList<>();
        if (downoptions.size() != 0) {
            Collections.sort(downoptions);//按bookid 排序

            for (Downoption downoption : downoptions) {
                if (list.contains(downoption.book_id)) {
                    downoption.showHead = false;
                } else {
                    downoption.showHead = true;
                    list.add(downoption.book_id);
                }
            }
            downMangerAdapter = new DownMangerAdapter(this, downoptions, fragment_bookshelf_noresult);
            activity_downmanger_list.setAdapter(downMangerAdapter);
        } else {
            fragment_bookshelf_noresult.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(value = {R.id.titlebar_back})
    public void getEvent(View view) {
        switch (view.getId()) {
            case R.id.titlebar_back:
                finish();
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshProcess(Downoption downoption) {

        for (Downoption downoption1 : downoptions) {
            if (downoption1.file_name.equals(downoption.file_name)) {
                MyToash.Log("refreshProcess--", downoption.toString());
                downoption1.down_cunrrent_num = downoption.down_cunrrent_num;
                downoption1.downoption_size = downoption.downoption_size;
                downoption1.isdown = downoption.isdown;
                downMangerAdapter.notifyDataSetChanged();
                return;
            }

        }
        MyToash.Log("refreshProcess", downoption.toString());
        downoptions.add(downoption);
        downMangerAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
