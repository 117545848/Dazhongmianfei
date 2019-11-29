package com.dazhongmianfei.dzmfreader.comic.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dazhongmianfei.dzmfreader.R;
import com.dazhongmianfei.dzmfreader.R2;
import com.dazhongmianfei.dzmfreader.activity.BaseButterKnifeActivity;
import com.dazhongmianfei.dzmfreader.comic.adapter.ComicChapterCatalogAdapter;
import com.dazhongmianfei.dzmfreader.comic.been.ComicChapter;
import com.dazhongmianfei.dzmfreader.comic.fragment.ComicinfoMuluFragment;
import com.dazhongmianfei.dzmfreader.utils.ImageUtil;
import com.dazhongmianfei.dzmfreader.utils.LanguageUtil;
import com.dazhongmianfei.dzmfreader.utils.MyToash;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by scb on 2018/6/9.
 */

//@ContentView(R.layout.fragment_comicinfo_mulu)
public class ComicinfoMuluActivity extends BaseButterKnifeActivity {
    //BaseComic baseComic;
    String currentChapter_id;
    int Size;
    //  StroreComicLable.Comic comic;
    boolean shunxu, isHttp;

    @BindView(R2.id.titlebar_back)
    public LinearLayout titlebar_back;
    @BindView(R2.id.fragment_comicinfo_mulu_xu)
    public TextView fragment_comicinfo_mulu_xu;
    @BindView(R2.id.fragment_comicinfo_mulu_xu_img)
    public ImageView fragment_comicinfo_mulu_xu_img;
    @BindView(R2.id.fragment_comicinfo_mulu_list)
    public ListView fragment_comicinfo_mulu_list;
    @BindView(R2.id.fragment_comicinfo_mulu_layout)
    public RelativeLayout fragment_comicinfo_mulu_layout;
    @BindView(R2.id.fragment_comicinfo_mulu_zhiding_img)
    public ImageView fragment_comicinfo_mulu_zhiding_img;
    @BindView(R2.id.fragment_comicinfo_mulu_zhiding_text)
    public TextView fragment_comicinfo_mulu_zhiding_text;

    @OnClick(value = {R.id.titlebar_back,
            R.id.fragment_comicinfo_mulu_dangqian, R.id.fragment_comicinfo_mulu_zhiding
            , R.id.fragment_comicinfo_mulu_layout})
    public void getEvent(View view) {
        switch (view.getId()) {
            case R.id.fragment_comicinfo_mulu_layout:
                shunxu = !shunxu;
                if (!shunxu) {
                    fragment_comicinfo_mulu_xu.setText(LanguageUtil.getString(activity, R.string.fragment_comic_info_zhengxu));
                    fragment_comicinfo_mulu_xu_img.setImageResource(R.mipmap.positive_order);
                } else {
                    fragment_comicinfo_mulu_xu.setText(LanguageUtil.getString(activity, R.string.fragment_comic_info_daoxu));
                    fragment_comicinfo_mulu_xu_img.setImageResource(R.mipmap.reverse_order);
                }
                Collections.reverse(comicChapterCatalogs);
                comicChapterCatalogAdapter.notifyDataSetChanged();
                break;
            case R.id.titlebar_back:
                activity.finish();
                break;
            case R.id.fragment_comicinfo_mulu_dangqian://baseComic.getCurrent_chapter_displayOrder()
                MyToash.Log("fraangqian", comicChapterCatalogAdapter.getCurrentPosition());
                fragment_comicinfo_mulu_list.setSelection(comicChapterCatalogAdapter.getCurrentPosition());
                break;
            case R.id.fragment_comicinfo_mulu_zhiding:
                if (!orentation) {
                    fragment_comicinfo_mulu_list.setSelection(Size);
                } else {
                    fragment_comicinfo_mulu_list.setSelection(0);
                }

                break;
        }
    }

    ComicChapterCatalogAdapter comicChapterCatalogAdapter;
    List<ComicChapter> comicChapterCatalogs;
    boolean orentation;

    @Override
    public int initContentView() {
        return R.layout.activity_comicinfo_mulu;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
    }

    int FirstVisibleItem;
    long time;

    public void initViews() {
        comicChapterCatalogs = new ArrayList<>();
        fragment_comicinfo_mulu_list.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if ((firstVisibleItem == 0)) {
                    if (orentation) {
                        orentation = false;
                        fragment_comicinfo_mulu_zhiding_img.setImageResource(R.mipmap.comicdetail_gotobottom);
                        fragment_comicinfo_mulu_zhiding_text.setText(LanguageUtil.getString(activity, R.string.fragment_comic_info_daodi));
                    }
                } else if (firstVisibleItem + visibleItemCount == totalItemCount) {
                    if (!orentation) {
                        orentation = true;
                        fragment_comicinfo_mulu_zhiding_img.setImageResource(R.mipmap.comicdetail_gototop);
                        fragment_comicinfo_mulu_zhiding_text.setText(LanguageUtil.getString(activity, R.string.fragment_comic_info_daoding));
                    }
                } else {
                    if ((firstVisibleItem > FirstVisibleItem)) {
                        if (orentation) {
                            orentation = false;
                            fragment_comicinfo_mulu_zhiding_img.setImageResource(R.mipmap.comicdetail_gotobottom);
                            fragment_comicinfo_mulu_zhiding_text.setText(LanguageUtil.getString(activity, R.string.fragment_comic_info_daodi));
                        }
                    } else if(firstVisibleItem < FirstVisibleItem){
                        if (!orentation) {
                            orentation = true;
                            fragment_comicinfo_mulu_zhiding_img.setImageResource(R.mipmap.comicdetail_gototop);
                            fragment_comicinfo_mulu_zhiding_text.setText(LanguageUtil.getString(activity, R.string.fragment_comic_info_daoding));
                        }
                    }
                }
                FirstVisibleItem = firstVisibleItem;
            }
        });

        fragment_comicinfo_mulu_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ComicChapter comicChapter = comicChapterCatalogs.get(position);
                Intent intent = new Intent();
                intent.putExtra("currentChapter_id", comicChapter.chapter_id);
                setResult(222, intent);
                finish();

            }
        });

        Intent intent = getIntent();
        currentChapter_id = intent.getStringExtra("currentChapter_id");
        //comicChapterCatalogs = (List<ComicChapter>) (intent.getSerializableExtra("comicChapter"));
        String comic_id=intent.getStringExtra("comic_id");

        ComicinfoMuluFragment.GetCOMIC_catalog(activity, comic_id, new ComicinfoMuluFragment.GetCOMIC_catalogList() {
            @Override
            public void GetCOMIC_catalogList(List<ComicChapter> comicChapterList) {
                    comicChapterCatalogs=comicChapterList;
                    if(comicChapterCatalogs!=null) {
                        Size = comicChapterCatalogs.size();
                        for(ComicChapter comicChapter:comicChapterCatalogs){

                            MyToash.Log("currentChapter_idSS",comicChapter.subtitle==null?"null":comicChapter.subtitle);
                        }
                        comicChapterCatalogAdapter = new ComicChapterCatalogAdapter(activity, currentChapter_id, comicChapterCatalogs, ImageUtil.dp2px(activity, 96));
                        fragment_comicinfo_mulu_list.setAdapter(comicChapterCatalogAdapter);
                        fragment_comicinfo_mulu_list.setSelection(comicChapterCatalogAdapter.getCurrentPosition());
                    }

            }
        });

    }

}
