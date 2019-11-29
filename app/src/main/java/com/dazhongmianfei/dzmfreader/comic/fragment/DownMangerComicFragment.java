package com.dazhongmianfei.dzmfreader.comic.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.dazhongmianfei.dzmfreader.R;
import com.dazhongmianfei.dzmfreader.R2;
import com.dazhongmianfei.dzmfreader.comic.adapter.DownMangerComicAdapter;
import com.dazhongmianfei.dzmfreader.comic.been.BaseComic;
import com.dazhongmianfei.dzmfreader.fragment.BaseButterKnifeFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.LitePal;
//.view.annotation.ViewInject;

import java.util.List;

import butterknife.BindView;

/**
 * Created by abc on 2016/11/4.
 */

public class DownMangerComicFragment extends BaseButterKnifeFragment {
    @Override
    public int initContentView() {
        return R.layout.fragment_downmanger;
    }

    @BindView(R2.id.activity_downmanger_list)
    public ListView activity_downmanger_list;
    @BindView(R2.id.fragment_bookshelf_noresult)
    public LinearLayout fragment_bookshelf_noresult;
    DownMangerComicAdapter downMangerAdapter;
    List<BaseComic> baseComicList;
public  static  boolean  DownMangerComicFragment;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EventBus.getDefault().register(this);
        DownMangerComicFragment=true;
        View temphead = LayoutInflater.from(activity).inflate(R.layout.item_list_head, null);
        activity_downmanger_list.addHeaderView(temphead, null, false);
        activity_downmanger_list.setHeaderDividersEnabled(true);

        baseComicList = LitePal.where("down_chapters != ?", "0").find(BaseComic.class);
        if (!baseComicList.isEmpty()) {
            //   Collections.sort(downoptions);//按bookid 排序
            downMangerAdapter = new DownMangerComicAdapter(activity, baseComicList, fragment_bookshelf_noresult);
            activity_downmanger_list.setAdapter(downMangerAdapter);
        } else {
            fragment_bookshelf_noresult.setVisibility(View.VISIBLE);
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshProcess(BaseComic baseComic) {
        if (!baseComicList.isEmpty()) {
            for (BaseComic baseComic1 : baseComicList) {
                if (baseComic1.getComic_id().equals(baseComic.getComic_id())) {
                    baseComic1.setCurrent_chapter_id(baseComic.getCurrent_chapter_id());
                    baseComic1.setCurrent_display_order(baseComic.getCurrent_display_order());
                    baseComic1.setDown_chapters(baseComic.getDown_chapters());
                    baseComic1.setTotal_chapters(baseComic.getTotal_chapters());
                    downMangerAdapter.notifyDataSetChanged();
                    return;
                }
            }
        } else {
            baseComicList.add(baseComic);
            downMangerAdapter = new DownMangerComicAdapter(activity, baseComicList, fragment_bookshelf_noresult);
            activity_downmanger_list.setAdapter(downMangerAdapter);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        DownMangerComicFragment=false;
        EventBus.getDefault().unregister(activity);
    }

}
