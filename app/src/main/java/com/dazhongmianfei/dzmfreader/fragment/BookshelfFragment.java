package com.dazhongmianfei.dzmfreader.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.dazhongmianfei.dzmfreader.R;
import com.dazhongmianfei.dzmfreader.R2;
import com.dazhongmianfei.dzmfreader.view.SizeAnmotionTextview;
import com.dazhongmianfei.dzmfreader.view.UnderlinePageIndicator;

//.view.annotation.ContentView;
//.view.annotation.ViewInject;
//.x;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.dazhongmianfei.dzmfreader.config.ReaderConfig.GETPRODUCT_TYPE;
import static com.dazhongmianfei.dzmfreader.config.ReaderConfig.MANHAU;
import static com.dazhongmianfei.dzmfreader.config.ReaderConfig.MANHAUXIAOSHUO;
import static com.dazhongmianfei.dzmfreader.config.ReaderConfig.XIAOSHUO;
import static com.dazhongmianfei.dzmfreader.config.ReaderConfig.XIAOSHUOMAHUA;
import static com.dazhongmianfei.dzmfreader.config.ReaderConfig.fragment_store_manhau_dp;
import static com.dazhongmianfei.dzmfreader.config.ReaderConfig.fragment_store_xiaoshuo_dp;
import static com.dazhongmianfei.dzmfreader.utils.StatusBarUtil.setStatusTextColor;


/**
 * Created by scb on 2018/12/21.
 */


public class BookshelfFragment extends BaseButterKnifeFragment {

    @Override
    public int initContentView() {
        return R.layout.fragment_new_bookshelf;
    }

    @BindView(R2.id.fragment_shelf_viewpage)
    public ViewPager fragment_newbookself_viewpager;
    @BindView(R2.id.fragment_bookself_topbar)
    public RelativeLayout fragment_bookself_topbar;
    @BindView(R2.id.fragment_shelf_indicator)
    public UnderlinePageIndicator indicator;

    @BindView(R2.id.fragment_shelf_xiaoshuo)
    public SizeAnmotionTextview fragment_shelf_xiaoshuo;
    @BindView(R2.id.fragment_shelf_manhau)
    public SizeAnmotionTextview fragment_shelf_manhau;

public  interface  DeleteBook{
    void  success();
    void fail();
}


}
