package com.dazhongmianfei.dzmfreader.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.core.content.ContextCompat;
import androidx.core.text.TextUtilsCompat;
import androidx.viewpager.widget.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dazhongmianfei.dzmfreader.R;
import com.dazhongmianfei.dzmfreader.R2;
import com.dazhongmianfei.dzmfreader.adapter.MyFragmentPagerAdapter;
import com.dazhongmianfei.dzmfreader.book.fragment.DownMangerBookFragment;
import com.dazhongmianfei.dzmfreader.book.fragment.ReadHistoryBookFragment;

import com.dazhongmianfei.dzmfreader.fragment.LiushuijiluFragment;
import com.dazhongmianfei.dzmfreader.fragment.MyCommentFragment;
import com.dazhongmianfei.dzmfreader.fragment.OptionFragment;
import com.dazhongmianfei.dzmfreader.fragment.RankIndexFragment;
import com.dazhongmianfei.dzmfreader.utils.LanguageUtil;
import com.dazhongmianfei.dzmfreader.view.UnderlinePageIndicatorHalf;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


import static com.dazhongmianfei.dzmfreader.config.ReaderConfig.BAOYUE;
import static com.dazhongmianfei.dzmfreader.config.ReaderConfig.BAOYUE_SEARCH;
import static com.dazhongmianfei.dzmfreader.config.ReaderConfig.DOWN;
import static com.dazhongmianfei.dzmfreader.config.ReaderConfig.GETPRODUCT_TYPE;
import static com.dazhongmianfei.dzmfreader.config.ReaderConfig.LIUSHUIJIELU;
import static com.dazhongmianfei.dzmfreader.config.ReaderConfig.LOOKMORE;
import static com.dazhongmianfei.dzmfreader.config.ReaderConfig.MANHAU;
import static com.dazhongmianfei.dzmfreader.config.ReaderConfig.MANHAUXIAOSHUO;
import static com.dazhongmianfei.dzmfreader.config.ReaderConfig.MIANFEI;
import static com.dazhongmianfei.dzmfreader.config.ReaderConfig.MYCOMMENT;
import static com.dazhongmianfei.dzmfreader.config.ReaderConfig.PAIHANG;
import static com.dazhongmianfei.dzmfreader.config.ReaderConfig.PAIHANGINSEX;
import static com.dazhongmianfei.dzmfreader.config.ReaderConfig.READHISTORY;
import static com.dazhongmianfei.dzmfreader.config.ReaderConfig.SHUKU;
import static com.dazhongmianfei.dzmfreader.config.ReaderConfig.WANBEN;
import static com.dazhongmianfei.dzmfreader.config.ReaderConfig.XIAOSHUO;
import static com.dazhongmianfei.dzmfreader.config.ReaderConfig.XIAOSHUOMAHUA;
import static com.dazhongmianfei.dzmfreader.config.ReaderConfig.getCurrencyUnit;
import static com.dazhongmianfei.dzmfreader.config.ReaderConfig.getSubUnit;

public class BaseOptionActivity extends BaseButterKnifeActivity {
    @Override
    public int initContentView() {
        return R.layout.activity_baseoption;
    }

    @BindView(R2.id.titlebar_text)
    public TextView titlebar_text;
    @BindView(R2.id.channel_bar_male_text)
    public TextView channel_bar_male_text;
    @BindView(R2.id.channel_bar_female_text)
    public TextView channel_bar_female_text;

    @BindView(R2.id.top_channel_layout)
    public LinearLayout top_channel_layout;


    @BindView(R2.id.channel_bar_indicator)
    public UnderlinePageIndicatorHalf channel_bar_indicator;


    @BindView(R2.id.activity_baseoption_viewpage)
    public ViewPager activity_baseoption_viewpage;


    FragmentManager fragmentManager;
    List<Fragment> fragmentList;
    MyFragmentPagerAdapter myFragmentPagerAdapter;
    Fragment baseButterKnifeFragment1, baseButterKnifeFragment2;
    int OPTION;
    boolean PRODUCT;// false 漫画  true  小说


    @OnClick(value = {R.id.titlebar_back, R.id.channel_bar_male_text, R.id.channel_bar_female_text})
    public void getEvent(View view) {
        switch (view.getId()) {
            case R.id.titlebar_back:
                finish();
                break;
            case R.id.channel_bar_male_text:
                activity_baseoption_viewpage.setCurrentItem(0);
                break;
            case R.id.channel_bar_female_text:
                activity_baseoption_viewpage.setCurrentItem(1);
                break;
        }
    }

    Intent IntentFrom;
int  colroSChoose,colroNoSChoose;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentManager = getSupportFragmentManager();
        IntentFrom = getIntent();
        colroSChoose= ContextCompat.getColor(activity,R.color.mainColor);
        colroNoSChoose= Color.BLACK;
        OPTION = IntentFrom.getIntExtra("OPTION", 0);
        PRODUCT = IntentFrom.getBooleanExtra("PRODUCT", false);
        if (OPTION != LOOKMORE) {
            String title = IntentFrom.getStringExtra("title");
            titlebar_text.setText(title);
        }
        activity_baseoption_viewpage.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (fragmentList.size() == 2) {
                    if (0 == position) {
                        channel_bar_male_text.setTextColor(colroSChoose);
                        channel_bar_female_text.setTextColor(colroNoSChoose);
                    }else {
                        channel_bar_male_text.setTextColor(colroNoSChoose);
                        channel_bar_female_text.setTextColor(colroSChoose);
                    }

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        init();
    }


    private void init() {
        fragmentList = new ArrayList<>();
        switch (OPTION) {
            case MIANFEI:
            case WANBEN:

                baseButterKnifeFragment1 = new OptionFragment(PRODUCT, OPTION, 1);
                baseButterKnifeFragment2 = new OptionFragment(PRODUCT, OPTION, 2);
            case PAIHANG:
                int SEX = IntentFrom.getIntExtra("SEX", 1);
                String rank_type = IntentFrom.getStringExtra("rank_type");
                baseButterKnifeFragment1 = new OptionFragment(PRODUCT, OPTION, rank_type, SEX);
                break;
            case PAIHANGINSEX:
                baseButterKnifeFragment1 = new RankIndexFragment(PRODUCT, OPTION, 1);
                baseButterKnifeFragment2 = new RankIndexFragment(PRODUCT, OPTION, 2);
                break;
            case BAOYUE_SEARCH:
            case SHUKU:
                baseButterKnifeFragment1 = new OptionFragment(PRODUCT, OPTION, 1);
                break;
            case BAOYUE:
                baseButterKnifeFragment1 = new OptionFragment(PRODUCT, OPTION, 1);
                break;
            case DOWN:
                baseButterKnifeFragment1 = new DownMangerBookFragment();

                break;
            case READHISTORY:
                baseButterKnifeFragment1 = new ReadHistoryBookFragment();

                break;
            case LIUSHUIJIELU:

                channel_bar_male_text.setText(getCurrencyUnit(activity));
                channel_bar_female_text.setText(getSubUnit(activity));
                baseButterKnifeFragment1 = new LiushuijiluFragment("currencyUnit");
                baseButterKnifeFragment2 = new LiushuijiluFragment("subUnit");
                break;
            case LOOKMORE:
                String recommend_id = IntentFrom.getStringExtra("recommend_id");
                if (recommend_id.equals("-1")) {
                    titlebar_text.setText(LanguageUtil.getString(activity, R.string.StoreFragment_xianshimianfei));
                    baseButterKnifeFragment1 = new OptionFragment(PRODUCT, OPTION, 1);
                    baseButterKnifeFragment2 = new OptionFragment(PRODUCT, OPTION, 2);
                } else {
                    baseButterKnifeFragment1 = new OptionFragment(PRODUCT, OPTION, recommend_id, titlebar_text);
                }
                break;

            case MYCOMMENT:

                switch (GETPRODUCT_TYPE(activity)) {
                    case XIAOSHUO:
                        baseButterKnifeFragment1 = new MyCommentFragment(true);
                        break;
                    case MANHAU:
                        baseButterKnifeFragment1 = new MyCommentFragment(false);
                        break;
                    case XIAOSHUOMAHUA:
                        baseButterKnifeFragment1 = new MyCommentFragment(true);
                        baseButterKnifeFragment2 = new MyCommentFragment(false);
                        channel_bar_male_text.setText(LanguageUtil.getString(activity, R.string.noverfragment_xiaoshuo));
                        channel_bar_female_text.setText(LanguageUtil.getString(activity, R.string.noverfragment_manhua));
                        break;
                    case MANHAUXIAOSHUO:
                        baseButterKnifeFragment2 = new MyCommentFragment(true);
                        baseButterKnifeFragment1 = new MyCommentFragment(false);
                        channel_bar_female_text.setText(LanguageUtil.getString(activity, R.string.noverfragment_xiaoshuo));
                        channel_bar_male_text.setText(LanguageUtil.getString(activity, R.string.noverfragment_manhua));
                        break;

                }
                break;

        }
        fragmentList.add(baseButterKnifeFragment1);
        if (baseButterKnifeFragment2 != null) {

            fragmentList.add(baseButterKnifeFragment2);
        } else {
            top_channel_layout.setVisibility(View.GONE);
        }
        myFragmentPagerAdapter = new MyFragmentPagerAdapter(fragmentManager, fragmentList);
        activity_baseoption_viewpage.setAdapter(myFragmentPagerAdapter);
        if (OPTION == LIUSHUIJIELU) {
            boolean Extra = IntentFrom.getBooleanExtra("Extra", false);
            if (Extra) {
                activity_baseoption_viewpage.setCurrentItem(1);
            }
        }
        if (baseButterKnifeFragment2 != null) {
            channel_bar_indicator.setViewPager(activity_baseoption_viewpage);
            channel_bar_indicator.setFades(false);
        }
    }


}
