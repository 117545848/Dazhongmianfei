package com.dazhongmianfei.dzmfreader.book.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.dazhongmianfei.dzmfreader.R;
import com.dazhongmianfei.dzmfreader.R2;

import com.dazhongmianfei.dzmfreader.adapter.DownMangerAdapter;
import com.dazhongmianfei.dzmfreader.bean.Downoption;
import com.dazhongmianfei.dzmfreader.fragment.BaseButterKnifeFragment;
import com.dazhongmianfei.dzmfreader.utils.MyToash;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.LitePal;
//.view.annotation.ContentView;
//.view.annotation.Event;
//.view.annotation.ViewInject;
//.x;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;

/**
 * Created by abc on 2016/11/4.
 */

public class DownMangerBookFragment extends BaseButterKnifeFragment {
    @Override
    public int initContentView() {
        return R.layout.fragment_downmanger;
    }
    @BindView(R2.id.activity_downmanger_list)
    public ListView activity_downmanger_list;
    @BindView(R2.id.fragment_bookshelf_noresult)
    public LinearLayout fragment_bookshelf_noresult;
    DownMangerAdapter downMangerAdapter;
    List<Downoption> downoptions;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EventBus.getDefault().register(this);
        View temphead = LayoutInflater.from(activity).inflate(R.layout.item_list_head, null);
        activity_downmanger_list.addHeaderView(temphead, null, false);
        activity_downmanger_list.setHeaderDividersEnabled(true);
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
            downMangerAdapter = new DownMangerAdapter(activity, downoptions, fragment_bookshelf_noresult);
            activity_downmanger_list.setAdapter(downMangerAdapter);
        } else {
            fragment_bookshelf_noresult.setVisibility(View.VISIBLE);
        }

    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshProcess(Downoption downoption) {

        if(!downoptions.isEmpty()) {
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
        }else {
            downoptions.add(downoption);
            downMangerAdapter = new DownMangerAdapter(activity, downoptions, fragment_bookshelf_noresult);
            activity_downmanger_list.setAdapter(downMangerAdapter);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(activity);
    }
    
}
