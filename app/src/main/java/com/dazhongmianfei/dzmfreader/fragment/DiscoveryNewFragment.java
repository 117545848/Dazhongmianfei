package com.dazhongmianfei.dzmfreader.fragment;

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
import com.dazhongmianfei.dzmfreader.adapter.MyFragmentPagerAdapter;
import com.dazhongmianfei.dzmfreader.book.fragment.DiscoveryBookFragment;

import com.dazhongmianfei.dzmfreader.comic.fragment.DiscoveryComicFragment;

import com.dazhongmianfei.dzmfreader.utils.ImageUtil;
import com.dazhongmianfei.dzmfreader.utils.LanguageUtil;
import com.dazhongmianfei.dzmfreader.utils.NotchScreen;
import com.dazhongmianfei.dzmfreader.utils.ShareUitls;
import com.dazhongmianfei.dzmfreader.view.SizeAnmotionTextview;
import com.dazhongmianfei.dzmfreader.view.UnderlinePageIndicatorHalf;


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

/**
 * Created by scb on 2018/12/21.
 */


public class DiscoveryNewFragment extends BaseButterKnifeFragment {
    @Override
    public int initContentView() {
        return R.layout.fragment_discoverynew;
    }

    @BindView(R2.id.fragment_discovery_viewpage)
    public ViewPager fragment_discovery_viewpage;

    @BindView(R2.id.fragment_discovery_top)
    public RelativeLayout fragment_discovery_top;


    @BindView(R2.id.fragment_discovery_indicator)
    public UnderlinePageIndicatorHalf fragment_discovery_indicator;


    @BindView(R2.id.fragment_discovery_xiaoshuo)
    public SizeAnmotionTextview fragment_discovery_xiaoshuo;

    @BindView(R2.id.fragment_discovery_manhau)
    public SizeAnmotionTextview fragment_discovery_manhau;

    FragmentManager fragmentManager;


    @OnClick(value = {R.id.fragment_discovery_xiaoshuo, R.id.fragment_discovery_manhau})
    public void getEvent(View view) {
        switch (view.getId()) {
            case R.id.fragment_discovery_xiaoshuo:
                if (chooseWho) {
                    fragment_discovery_viewpage.setCurrentItem(0);
                    chooseWho = false;
                }
                break;
            case R.id.fragment_discovery_manhau:
                if (!chooseWho) {
                    fragment_discovery_viewpage.setCurrentItem(1);
                    chooseWho = true;
                }
                break;
        }
    }

    //  boolean position = true;

    List<Fragment> fragmentList;
    MyFragmentPagerAdapter myFragmentPagerAdapter;
    public boolean chooseWho;
    Fragment fragment1, fragment2;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fragmentManager = getChildFragmentManager();

        fragmentList = new ArrayList<>();

        switch (GETPRODUCT_TYPE(activity)) {
            case XIAOSHUO:
                fragment1 = new <Fragment>DiscoveryBookFragment();

                fragment_discovery_manhau.setVisibility(View.GONE);
                fragmentList.add(fragment1);
                break;
            case MANHAU:
                fragment1 = new <Fragment>DiscoveryComicFragment();
                fragment_discovery_manhau.setVisibility(View.GONE);
                fragmentList.add(fragment1);
                break;
            case XIAOSHUOMAHUA:
                fragment1 = new <Fragment>DiscoveryBookFragment();
                fragment2 = new <Fragment>DiscoveryComicFragment();
                fragmentList.add(fragment1);
                fragmentList.add(fragment2);
                break;
            case MANHAUXIAOSHUO:
                fragment2 = new <Fragment>DiscoveryBookFragment();
                fragment1 = new <Fragment>DiscoveryComicFragment();
                fragment_discovery_xiaoshuo.setText(LanguageUtil.getString(activity, R.string.noverfragment_manhua));
                fragment_discovery_manhau.setText(LanguageUtil.getString(activity, R.string.noverfragment_xiaoshuo));
                fragmentList.add(fragment1);
                fragmentList.add(fragment2);

                break;

        }
        myFragmentPagerAdapter = new MyFragmentPagerAdapter(fragmentManager, fragmentList);
        fragment_discovery_viewpage.setAdapter(myFragmentPagerAdapter);
        fragment_discovery_indicator.setViewPager(fragment_discovery_viewpage);
        fragment_discovery_indicator.setFades(false);
        if (GETPRODUCT_TYPE(activity) == XIAOSHUOMAHUA || GETPRODUCT_TYPE(activity) == MANHAUXIAOSHUO) {
            if (NotchScreen.hasNotchScreen(activity)) {
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) (fragment_discovery_top.getLayoutParams());
                layoutParams.height = ImageUtil.dp2px(activity, 90);
                fragment_discovery_top.setLayoutParams(layoutParams);
            }
            int LastFragment = ShareUitls.getTab(activity, "DiscoveryNewFragment", 0);
            if (LastFragment == 1) {
                fragment_discovery_viewpage.setCurrentItem(1);
                chooseWho = true;
            }
            if (LastFragment == 1) {
                fragment_discovery_viewpage.setCurrentItem(1);
                fragment_discovery_xiaoshuo.setTextSize(fragment_store_manhau_dp);
                fragment_discovery_manhau.setTextSize(fragment_store_xiaoshuo_dp);
                chooseWho = true;
            } else {
                fragment_discovery_xiaoshuo.setTextSize(fragment_store_xiaoshuo_dp);
                fragment_discovery_manhau.setTextSize(fragment_store_manhau_dp);
            }
            fragment_discovery_viewpage.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                }

                @Override
                public void onPageSelected(int position) {
                    chooseWho = position == 1;

                    if (GETPRODUCT_TYPE(activity) == XIAOSHUOMAHUA || GETPRODUCT_TYPE(activity) == MANHAUXIAOSHUO) {
                        ShareUitls.putTab(activity, "DiscoveryNewFragment", position);
                    }
                    if (!chooseWho) {
                        fragment_discovery_xiaoshuo.setTextSize(fragment_store_xiaoshuo_dp);
                        fragment_discovery_manhau.setTextSize(fragment_store_manhau_dp);
                    } else {
                        fragment_discovery_xiaoshuo.setTextSize(fragment_store_manhau_dp);
                        fragment_discovery_manhau.setTextSize(fragment_store_xiaoshuo_dp);
                    }

                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        } else {
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) (fragment_discovery_viewpage.getLayoutParams());
            layoutParams.topMargin = ImageUtil.dp2px(activity, 40);
            fragment_discovery_viewpage.setLayoutParams(layoutParams);
            fragment_discovery_top.setVisibility(View.GONE);
        }


    }


}
