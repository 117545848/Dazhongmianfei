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
import com.dazhongmianfei.dzmfreader.adapter.MyFragmentPagerAdapter;
import com.dazhongmianfei.dzmfreader.book.been.BaseBook;
import com.dazhongmianfei.dzmfreader.comic.been.BaseComic;
import com.dazhongmianfei.dzmfreader.comic.fragment.ComicshelfFragment;
import com.dazhongmianfei.dzmfreader.book.fragment.NovelFragmentNew;
import com.dazhongmianfei.dzmfreader.utils.ImageUtil;
import com.dazhongmianfei.dzmfreader.utils.LanguageUtil;
import com.dazhongmianfei.dzmfreader.utils.MyToash;
import com.dazhongmianfei.dzmfreader.utils.NotchScreen;
import com.dazhongmianfei.dzmfreader.utils.ShareUitls;
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

    @OnClick(value = {R.id.fragment_shelf_xiaoshuo, R.id.fragment_shelf_manhau})
    public void getEvent(View view) {
        switch (view.getId()) {
            case R.id.fragment_shelf_xiaoshuo:
                if (chooseWho) {
                    fragment_newbookself_viewpager.setCurrentItem(0);
                    chooseWho = false;
                }
                break;
            case R.id.fragment_shelf_manhau:
                if (!chooseWho) {
                    fragment_newbookself_viewpager.setCurrentItem(1);
                    chooseWho = true;
                }
                break;


        }
    }



  /*  @BindView(R2.id.fragment_novel_allchoose)
    public LinearLayout fragment_novel_allchoose;
    @BindView(R2.id.fragment_novel_cancle)
    public LinearLayout fragment_novel_cancle;
*/

    LinearLayout shelf_book_delete_btn;

    FragmentManager fragmentManager;
    List<BaseBook> bookLists;
    List<BaseComic> baseComics;

    public BookshelfFragment() {
    }


    @SuppressLint("ValidFragment")
    public BookshelfFragment(List<BaseBook> bookLists, List<BaseComic> baseComics, LinearLayout shelf_book_delete_btn) {
        this.bookLists = bookLists;
        this.baseComics = baseComics;
        this.shelf_book_delete_btn = shelf_book_delete_btn;

        MyToash.Log("shelf_book_delete_btn", (shelf_book_delete_btn == null) + "");

    }

    // ReadHistoryFragment readHistoryFragment;
    boolean position = true;

    List<Fragment> fragmentList;
    MyFragmentPagerAdapter myFragmentPagerAdapter;
    public boolean chooseWho;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fragmentManager = getChildFragmentManager();

        setStatusTextColor(true, activity);
        try {
            initOption();
        } catch (Exception e) {
        }

    }
    NovelFragmentNew novelFragment = null;
    ComicshelfFragment comicshelfFragment = null;

    private void initOption() {
        fragmentList = new ArrayList<>();

        switch (GETPRODUCT_TYPE(activity)) {
            case XIAOSHUO:
                novelFragment = new <Fragment>NovelFragmentNew(bookLists, shelf_book_delete_btn);
                fragmentList.add(novelFragment);
                break;
            case MANHAU:
                comicshelfFragment = new <Fragment>ComicshelfFragment(baseComics, shelf_book_delete_btn);
                fragmentList.add(comicshelfFragment);
                break;
            case XIAOSHUOMAHUA:
                novelFragment = new <Fragment>NovelFragmentNew(bookLists, shelf_book_delete_btn);
                fragmentList.add(novelFragment);
                comicshelfFragment = new <Fragment>ComicshelfFragment(baseComics, shelf_book_delete_btn);
                fragmentList.add(comicshelfFragment);
                break;
            case MANHAUXIAOSHUO:
                comicshelfFragment = new <Fragment>ComicshelfFragment(baseComics, shelf_book_delete_btn);
                fragmentList.add(comicshelfFragment);
                novelFragment = new <Fragment>NovelFragmentNew(bookLists, shelf_book_delete_btn);
                fragmentList.add(novelFragment);
                fragment_shelf_xiaoshuo.setText(LanguageUtil.getString(activity, R.string.noverfragment_manhua));
                fragment_shelf_manhau.setText(LanguageUtil.getString(activity, R.string.noverfragment_xiaoshuo));
                break;
        }

        myFragmentPagerAdapter = new MyFragmentPagerAdapter(fragmentManager, fragmentList);
        fragment_newbookself_viewpager.setAdapter(myFragmentPagerAdapter);

        if (GETPRODUCT_TYPE(activity) == XIAOSHUOMAHUA || GETPRODUCT_TYPE(activity) == MANHAUXIAOSHUO) {
            if (NotchScreen.hasNotchScreen(getActivity()) || android.os.Build.VERSION.SDK_INT <= 23) {
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) fragment_bookself_topbar.getLayoutParams();
                layoutParams.height = ImageUtil.dp2px(activity, 90);
                fragment_bookself_topbar.setLayoutParams(layoutParams);
            }
            int LastFragment = ShareUitls.getTab(activity, "BookshelfFragment", 0);
            if (LastFragment == 1) {
                fragment_newbookself_viewpager.setCurrentItem(1);
                fragment_shelf_xiaoshuo.setTextSize(fragment_store_manhau_dp);
                fragment_shelf_manhau.setTextSize(fragment_store_xiaoshuo_dp);
                chooseWho=true;
            } else {
                fragment_shelf_xiaoshuo.setTextSize(fragment_store_xiaoshuo_dp);
                fragment_shelf_manhau.setTextSize(fragment_store_manhau_dp);
            }
            indicator.setViewPager(fragment_newbookself_viewpager);
            indicator.setFades(false);
            position = true;
            fragment_newbookself_viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                }

                @Override
                public void onPageSelected(int position) {
                    chooseWho = position == 1;
                    if (GETPRODUCT_TYPE(activity) == XIAOSHUOMAHUA || GETPRODUCT_TYPE(activity) == MANHAUXIAOSHUO) {
                        ShareUitls.putTab(activity, "BookshelfFragment", position);
                        novelFragment.AllchooseAndCancleOnclick(false);
                        comicshelfFragment.AllchooseAndCancleOnclick(false);
                    }
                    if (!chooseWho) {
                        fragment_shelf_xiaoshuo.setTextSize(fragment_store_xiaoshuo_dp);
                        fragment_shelf_manhau.setTextSize(fragment_store_manhau_dp);
                    } else {
                        fragment_shelf_xiaoshuo.setTextSize(fragment_store_manhau_dp);
                        fragment_shelf_manhau.setTextSize(fragment_store_xiaoshuo_dp);
                    }

                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        } else {
            fragment_bookself_topbar.setVisibility(View.GONE);

                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) fragment_newbookself_viewpager.getLayoutParams();
                layoutParams.topMargin=ImageUtil.dp2px(activity,40);
               fragment_newbookself_viewpager.setLayoutParams(layoutParams);



           // fragment_shelf_manhau.setVisibility(View.GONE);
        }
    }


    public interface DeleteBook {
        void success();

        void fail();
    }
}
