package com.dazhongmianfei.dzmfreader.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dazhongmianfei.dzmfreader.R;
import com.dazhongmianfei.dzmfreader.R2;
import com.dazhongmianfei.dzmfreader.activity.SearchActivity;

import com.dazhongmianfei.dzmfreader.adapter.MyFragmentPagerAdapter;
import com.dazhongmianfei.dzmfreader.book.fragment.StoreBookFragment;

import com.dazhongmianfei.dzmfreader.eventbus.StoreEventbus;
import com.dazhongmianfei.dzmfreader.eventbus.ToStore;
import com.dazhongmianfei.dzmfreader.utils.ImageUtil;
import com.dazhongmianfei.dzmfreader.utils.LanguageUtil;
import com.dazhongmianfei.dzmfreader.utils.MyToash;
import com.dazhongmianfei.dzmfreader.utils.NotchScreen;
import com.dazhongmianfei.dzmfreader.utils.ShareUitls;
import com.dazhongmianfei.dzmfreader.view.SizeAnmotionTextview;
import com.dazhongmianfei.dzmfreader.view.UnderlinePageIndicatorHalf;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.dazhongmianfei.dzmfreader.config.ReaderConfig.GETPRODUCT_TYPE;
import static com.dazhongmianfei.dzmfreader.config.ReaderConfig.MANHAU;
import static com.dazhongmianfei.dzmfreader.config.ReaderConfig.MANHAUXIAOSHUO;
import static com.dazhongmianfei.dzmfreader.config.ReaderConfig.REFRESH_HEIGHT;
import static com.dazhongmianfei.dzmfreader.config.ReaderConfig.XIAOSHUO;
import static com.dazhongmianfei.dzmfreader.config.ReaderConfig.XIAOSHUOMAHUA;
import static com.dazhongmianfei.dzmfreader.config.ReaderConfig.fragment_store_manhau_dp;
import static com.dazhongmianfei.dzmfreader.config.ReaderConfig.fragment_store_xiaoshuo_dp;

import static com.dazhongmianfei.dzmfreader.utils.StatusBarUtil.setStatusTextColor;

/**
 * Created by scb on 2018/12/21.
 */


public class StroeNewFragment extends BaseButterKnifeFragment {
    @Override
    public int initContentView() {
        return R.layout.fragment_storenew;
    }

    @BindView(R2.id.fragment_store_viewpage)
    public ViewPager fragment_store_viewpage;

    @BindView(R2.id.fragment_store_top)
    public RelativeLayout fragment_newbookself_top;


    @BindView(R2.id.fragment_store_manorwoman)
    public RelativeLayout fragment_store_manorwoman;


    @BindView(R2.id.fragment_store_indicator)
    public UnderlinePageIndicatorHalf indicator;

    @BindView(R2.id.fragment_store_search_bookname)
    public TextView fragment_store_search_bookname;
    @BindView(R2.id.fragment_store_search_bookname2)
    public TextView fragment_store_search_bookname2;


    @BindView(R2.id.fragment_store_search_img)
    public ImageView fragment_store_search_img;
    @BindView(R2.id.fragment_store_sex)
    public ImageView fragment_store_sex;
    @BindView(R2.id.fragment_store_search)
    public RelativeLayout fragment_store_search;
    @BindView(R2.id.fragment_store_xiaoshuo)
    public SizeAnmotionTextview fragment_store_xiaoshuo;
    @BindView(R2.id.fragment_store_manhau)
    public SizeAnmotionTextview fragment_store_manhau;
    FragmentManager fragmentManager;

    public static boolean SEX;
    public String hot_word_book[], hot_word_comic[];
    //String SearchName = "";
    int hot_word_bookSize, hot_word_bookPosition, hot_word_comicSize, hot_word_comicPosition;

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                ++hot_word_bookPosition;
                if (hot_word_bookPosition == hot_word_bookSize) {
                    hot_word_bookPosition = 0;
                }
                fragment_store_search_bookname.setText(hot_word_book[hot_word_bookPosition]);
                handler.sendEmptyMessageDelayed(0, 10000);
            } else {
                ++hot_word_comicPosition;
                if (hot_word_comicPosition == hot_word_comicSize) {
                    hot_word_comicPosition = 0;
                }
                fragment_store_search_bookname2.setText(hot_word_comic[hot_word_comicPosition]);
                handler.sendEmptyMessageDelayed(1, 10000);
            }


        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeMessages(0);
        handler.removeMessages(1);
    }

    public interface Hot_word {
        void hot_word(String[] hot_word);
    }

    public class Hot_word_Book implements Hot_word {
        @Override
        public void hot_word(String[] hot_word) {
            if (hot_word != null && hot_word.length > 0) {
                hot_word_book = hot_word;
                hot_word_bookSize = hot_word_book.length;
                handler.sendEmptyMessage(0);
            }
        }
    }

    public class Hot_word_Comic implements Hot_word {
        @Override
        public void hot_word(String[] hot_word) {
            if (hot_word != null && hot_word.length > 0) {
                hot_word_comic = hot_word;
                hot_word_comicSize = hot_word_comic.length;
                handler.sendEmptyMessage(1);
            }
        }
    }

    @OnClick(value = {R.id.fragment_store_manorwoman, R.id.fragment_store_xiaoshuo, R.id.fragment_store_manhau, R.id.fragment_store_search})
    public void getEvent(View view) {
        switch (view.getId()) {
            case R.id.fragment_store_manorwoman:

                break;
            case R.id.fragment_store_xiaoshuo:
                if (chooseWho) {
                    fragment_store_viewpage.setCurrentItem(0);
                    chooseWho = false;
                }
                break;
            case R.id.fragment_store_manhau:
                if (!chooseWho) {
                    fragment_store_viewpage.setCurrentItem(1);
                    chooseWho = true;
                }
                break;
            case R.id.fragment_store_search:
                boolean PRODUCT;
                String name;
                if (fragment_store_search_bookname.getVisibility() == View.VISIBLE) {
                    name = fragment_store_search_bookname.getText().toString();
                    PRODUCT = true;
                } else {
                    name = fragment_store_search_bookname2.getText().toString();
                    PRODUCT = false;
                }
                MyToash.Log("PRODUCT", PRODUCT + "  " + name);
                Intent intent = new Intent(activity, SearchActivity.class).putExtra("PRODUCT", PRODUCT).putExtra("mKeyWord", name);
                startActivity(intent);
                break;

        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void ToStore(ToStore toStore) {


    }
    // boolean position = true;

    List<Fragment> fragmentList;
    MyFragmentPagerAdapter myFragmentPagerAdapter;


    public boolean chooseWho;
    public static boolean IS_NOTOP;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void StoreEventbus(StoreEventbus storeEventbus) {

        float ratio = Math.min(Math.max(storeEventbus.Y, 0), REFRESH_HEIGHT) / REFRESH_HEIGHT;
        float alpha = (int) (ratio * 255);
        fragment_newbookself_top.setBackgroundColor(Color.argb((int) alpha, 255, 255, 255));
        if (storeEventbus.Y > REFRESH_HEIGHT) {
            setBgBlack();
        } else if (storeEventbus.Y <= REFRESH_HEIGHT) {
            setBgWhite();
        }
    }

    private void setBgWhite() {
        if (IS_NOTOP) {
            IS_NOTOP = false;
            fragment_store_xiaoshuo.setTextColor(Color.WHITE);
            fragment_store_manhau.setTextColor(Color.WHITE);
            fragment_store_search_bookname.setTextColor(Color.WHITE);
            fragment_store_search_bookname2.setTextColor(Color.WHITE);
            indicator.setSelectedColor(Color.WHITE);
            fragment_store_search_img.setImageResource(R.mipmap.main_search_white);
            setStatusTextColor(false, activity);
            fragment_store_search.setBackgroundResource(R.drawable.shape_comic_store_search);


        }
    }

    private void setBgBlack() {
        if (!IS_NOTOP) {
            IS_NOTOP = true;
            fragment_store_xiaoshuo.setTextColor(Color.BLACK);
            fragment_store_manhau.setTextColor(Color.BLACK);
            fragment_store_search_bookname.setTextColor(Color.GRAY);
            fragment_store_search_bookname2.setTextColor(Color.GRAY);
            indicator.setSelectedColor(Color.BLACK);
            fragment_store_search_img.setImageResource(R.mipmap.main_search_dark);
            setStatusTextColor(true, activity);
            fragment_store_search.setBackgroundResource(R.drawable.shape_comic_store_search_dark);

        }
    }


    StoreBookFragment fragment1, fragment2;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EventBus.getDefault().register(this);
        fragmentManager = getChildFragmentManager();
        if (NotchScreen.hasNotchScreen(getActivity())) {
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) (fragment_newbookself_top.getLayoutParams());
            layoutParams.height = ImageUtil.dp2px(activity, 90);
            fragment_newbookself_top.setLayoutParams(layoutParams);
        }
        try {
            initOption();
        } catch (Exception e) {
        }
    }

    private void initOption() {
        fragmentList = new ArrayList<>();
        fragment1 = new <Fragment>StoreBookFragment(1, fragment_newbookself_top, fragment_store_sex, new Hot_word_Book());
        fragment2 = new <Fragment>StoreBookFragment(2, fragment_newbookself_top, fragment_store_sex, null);
        fragment_store_manhau.setText(LanguageUtil.getString(activity, R.string.storeFragment_gril));
        fragment_store_xiaoshuo.setText(LanguageUtil.getString(activity, R.string.storeFragment_boy));
        fragment_store_manorwoman.setVisibility(View.GONE);
        fragmentList.add(fragment1);
        fragmentList.add(fragment2);
        myFragmentPagerAdapter = new MyFragmentPagerAdapter(fragmentManager, fragmentList);
        fragment_store_viewpage.setAdapter(myFragmentPagerAdapter);

        fragment_store_xiaoshuo.setTextSize(fragment_store_xiaoshuo_dp);
        fragment_store_manhau.setTextSize(fragment_store_manhau_dp);


        indicator.setViewPager(fragment_store_viewpage);
        indicator.setFades(false);
        fragment_store_viewpage.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                chooseWho = position == 1;
                if (!chooseWho) {
                    fragment_store_xiaoshuo.setTextSize(fragment_store_xiaoshuo_dp);
                    fragment_store_manhau.setTextSize(fragment_store_manhau_dp);
                } else {
                    fragment_store_xiaoshuo.setTextSize(fragment_store_manhau_dp);
                    fragment_store_manhau.setTextSize(fragment_store_xiaoshuo_dp);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

}
