package com.dazhongmianfei.dzmfreader.comic.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.dazhongmianfei.dzmfreader.R;
import com.dazhongmianfei.dzmfreader.R2;
import com.dazhongmianfei.dzmfreader.comic.activity.ComicInfoActivity;
import com.dazhongmianfei.dzmfreader.comic.activity.ComicLookActivity;
import com.dazhongmianfei.dzmfreader.comic.adapter.ComicChapterCatalogAdapter;
import com.dazhongmianfei.dzmfreader.comic.been.BaseComic;
import com.dazhongmianfei.dzmfreader.comic.been.ComicChapter;
import com.dazhongmianfei.dzmfreader.comic.been.StroreComicLable;
import com.dazhongmianfei.dzmfreader.comic.config.ComicConfig;
import com.dazhongmianfei.dzmfreader.comic.eventbus.ComicinfoMuluBuy;
import com.dazhongmianfei.dzmfreader.eventbus.RefreshMine;
import com.dazhongmianfei.dzmfreader.fragment.BaseButterKnifeFragment;
import com.dazhongmianfei.dzmfreader.http.ReaderParams;
import com.dazhongmianfei.dzmfreader.utils.HttpUtils;
import com.dazhongmianfei.dzmfreader.utils.ImageUtil;
import com.dazhongmianfei.dzmfreader.utils.LanguageUtil;
import com.dazhongmianfei.dzmfreader.utils.MyToash;
import com.dazhongmianfei.dzmfreader.utils.Utils;
import com.dazhongmianfei.dzmfreader.view.ObservableScrollView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.LitePal;
import org.litepal.crud.callback.FindMultiCallback;
import org.litepal.crud.callback.SaveCallback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;

//.http.RequestParams;
//.view.annotation.ContentView;
//.view.annotation.ViewInject;
//.x;

/**
 * Created by scb on 2018/6/9.
 */

//@ContentView(R.layout.fragment_comicinfo_mulu)
public class ComicinfoMuluFragment extends BaseButterKnifeFragment {
    @Override
    public int initContentView() {
        return R.layout.fragment_comicinfo_mulu;
    }

    BaseComic baseComic;
    String comic_id;
    StroreComicLable.Comic comic;
    boolean shunxu, isHttp;
    @BindView(R2.id.fragment_comicinfo_mulu_zhuangtai)
    public TextView fragment_comicinfo_mulu_zhuangtai;
    @BindView(R2.id.fragment_comicinfo_mulu_xu)
    public TextView fragment_comicinfo_mulu_xu;
    @BindView(R2.id.fragment_comicinfo_mulu_xu_img)
    public ImageView fragment_comicinfo_mulu_xu_img;
    @BindView(R2.id.fragment_comicinfo_mulu_list)
    public ListView fragment_comicinfo_mulu_list;
    @BindView(R2.id.fragment_comicinfo_mulu_layout)
    public RelativeLayout fragment_comicinfo_mulu_layout;
    @BindView(R2.id.activity_book_info_scrollview)
    public ObservableScrollView activity_book_info_scrollview;

    ComicInfoActivity.MuluLorded muluLorded;
    int Current_chapter_displayOrder;

    public ImageView fragment_comicinfo_mulu_zhiding_img;
    public TextView fragment_comicinfo_mulu_zhiding_text;
    boolean orentation;//fasle 滑动方向向下
    int H96;
    Gson gson=new Gson();

    @SuppressLint("ValidFragment")
    public ComicinfoMuluFragment(ComicInfoActivity.MuluLorded muluLorded, ImageView fragment_comicinfo_mulu_zhiding_img, TextView fragment_comicinfo_mulu_zhiding_text) {
        this.muluLorded = muluLorded;
        this.fragment_comicinfo_mulu_zhiding_img = fragment_comicinfo_mulu_zhiding_img;
        this.fragment_comicinfo_mulu_zhiding_text = fragment_comicinfo_mulu_zhiding_text;
    }

    public ComicinfoMuluFragment() {
    }

    public void OnclickDangqian(boolean dangqian) {
        MyToash.Log("OnclickDangqian", dangqian + "");
        if (dangqian) {
            try {
                //    int cu = baseComic.getCurrent_chapter_displayOrder();
                activity_book_info_scrollview.scrollTo(0, (int) fragment_comicinfo_mulu_list.getChildAt(Current_chapter_displayOrder).getY());
                //svscrollouter.scrollTo(0,tvscrollfour.getTop());
            } catch (Exception e) {
            }
        } else {
            if (orentation) {
                //  activity_book_info_scrollview.scrollTo(0, 0);
                activity_book_info_scrollview.fullScroll(NestedScrollView.FOCUS_UP);
            } else {
                // activity_book_info_scrollview.scrollTo(0, 0);
                activity_book_info_scrollview.fullScroll(NestedScrollView.FOCUS_DOWN);

            }            //fragment_comicinfo_mulu_list.setSelection(0);
        }
    }

    public ComicChapterCatalogAdapter comicChapterCatalogAdapter;
    List<ComicChapter> comicChapterCatalogs;
    List<ComicChapter> ComicChapterCatalogs;


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        EventBus.getDefault().register(this);
        H96 = ImageUtil.dp2px(activity, 96);
        initViews();

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refresh(RefreshMine refreshMine) {
        ReaderParams params = new ReaderParams(activity);
        params.putExtraParams("comic_id", comic_id);
        String json = params.generateParamsJson();
        HttpUtils.getInstance(activity).sendRequestRequestParams3(ComicConfig.COMIC_down_option, json, true, new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(final String result) {

                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            JsonParser jsonParser = new JsonParser();
                            JsonArray jsonElements = jsonParser.parse(jsonObject.getString("down_list")).getAsJsonArray();//获取JsonArray对象
                            int i=0;
                            for (JsonElement jsonElement : jsonElements) {
                                ComicChapter comicChapter = gson.fromJson(jsonElement, ComicChapter.class);
                                comicChapterCatalogs.get(i).is_preview=comicChapter.is_preview;
                                ++i;
                            }
                            comicChapterCatalogAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onErrorResponse(String ex) {


                    }
                }

        );

    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refresh(ComicinfoMuluBuy comicinfoMuluBuy) {
        try {
            int i = 0;
            for (String id : comicinfoMuluBuy.ids) {
                if (i != 0) {
                    ++i;
                    comicChapterCatalogs.get(i).is_preview = 0;
                }else {
                    FLAG:
                    for (ComicChapter comicChapter : comicChapterCatalogs) {
                        if (id.equals(comicChapter.chapter_id)) {
                            i = comicChapter.display_order;
                            comicChapter.is_preview = 0;
                            break FLAG;
                        }

                    }
                }

            }
            comicChapterCatalogAdapter.notifyDataSetChanged();
        } catch (Exception e) {
        }
    }

    public static boolean CHENGGUI_CATALOG;

    public void initViews() {

        comicChapterCatalogs = new ArrayList<>();
        fragment_comicinfo_mulu_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
            }
        });
        fragment_comicinfo_mulu_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ComicChapter comicChapter = comicChapterCatalogs.get(position);
                if (baseComic != null && comicChapter != null) {
                    baseComic.setCurrent_display_order(position);
                    baseComic.saveIsexist(false);
                    Intent intent = new Intent(activity, ComicLookActivity.class);
                    intent.putExtra("baseComic", baseComic);
                    startActivity(intent);
                }

            }
        });
        activity_book_info_scrollview.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                if (scrollY > oldScrollY || scrollY == 0) {//向下滚动 或者是滚动到顶部了
                    orentation = false;
                    fragment_comicinfo_mulu_zhiding_img.setImageResource(R.mipmap.comicdetail_gotobottom);
                    fragment_comicinfo_mulu_zhiding_text.setText(LanguageUtil.getString(activity, R.string.fragment_comic_info_daodi));

                } else if (scrollY < oldScrollY || scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {//向上滚动
                    orentation = true;
                    fragment_comicinfo_mulu_zhiding_img.setImageResource(R.mipmap.comicdetail_gototop);
                    fragment_comicinfo_mulu_zhiding_text.setText(LanguageUtil.getString(activity, R.string.fragment_comic_info_daoding));
                }
            }
        });

    }

    public void senddata(BaseComic baseComic, StroreComicLable.Comic comic) {
        if (activity == null) {
            activity = getActivity();
        }
        this.baseComic = baseComic;
        comic_id = baseComic.getComic_id();
        if (comic != null) {
            this.comic = comic;
            if (comic.is_finish == 1) {
                fragment_comicinfo_mulu_zhuangtai.setText(LanguageUtil.getString(activity, R.string.fragment_comic_info_wanjie) + " (" + comic.flag + ")");
            } else {
                fragment_comicinfo_mulu_zhuangtai.setText(LanguageUtil.getString(activity, R.string.fragment_comic_info_wanjie) + " (" + comic.flag + ")");
            }
        }
        httpData();
    }

    public void httpData() {
        ReaderParams params = new ReaderParams(activity);
        params.putExtraParams("comic_id", comic_id);
        String json = params.generateParamsJson();
        HttpUtils.getInstance(activity).sendRequestRequestParams3(ComicConfig.COMIC_catalog, json, true, new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(final String result) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                ComicChapterCatalogs = LitePal.where("comic_id = ?", comic_id).find(ComicChapter.class);
                                String Chapter_text_Sign = Utils.MD5(result);//章节目录数据的MD5
                                boolean isFirst = ComicChapterCatalogs.isEmpty();
                                if (baseComic.getChapter_text() != null && (Chapter_text_Sign.equals(baseComic.getChapter_text())) && !isFirst) {
                                    isHttp = true;   //baseComic.getCurrent_chapter_id()
                                    comicChapterCatalogs = ComicChapterCatalogs;

                                    MyToash.Log("comicChapterCatalogs", " " + comicChapterCatalogs.size());
                                } else {
                                    try {
                                        JSONObject jsonObject = new JSONObject(result);
                                        JsonParser jsonParser = new JsonParser();
                                        JsonArray jsonElements = jsonParser.parse(jsonObject.getString("chapter_list")).getAsJsonArray();//获取JsonArray对象

                                        for (JsonElement jsonElement : jsonElements) {
                                            ComicChapter comicChapter = gson.fromJson(jsonElement, ComicChapter.class);
                                            comicChapter.comic_id = comic_id;
                                            comicChapterCatalogs.add(comicChapter);
                                        }
                                        if (isFirst) {
                                            //  LitePal.saveAll(comicChapterCatalogs);
                                        } else {
                                            int Size = ComicChapterCatalogs.size();
                                            int i = -1;
                                            for (ComicChapter chapterItem : comicChapterCatalogs) {
                                                ++i;
                                                flag:
                                                if ((i < Size) && ComicChapterCatalogs.get(i).chapter_id.equals(chapterItem.chapter_id)) {//两个集合 元素差距不大一一对应
                                                    ComicChapter chapterItem1 = ComicChapterCatalogs.get(i);
                                                    //  chapterItem.setCurrent_read_img_image_id(chapterItem1.getCurrent_read_img_image_id());
                                                    //  chapterItem.setCurrent_read_img_order(chapterItem1.getCurrent_read_img_order());
                                                    chapterItem.IsRead = chapterItem1.IsRead;
                                                    chapterItem.setImagesText(chapterItem1.ImagesText);
                                                    chapterItem.current_read_img_order = chapterItem1.current_read_img_order;
                                                    LitePal.delete(ComicChapter.class, chapterItem.getId());

                                                    break flag;//跳出内循环
                                                } else {
                                                    for (ComicChapter chapterItem1 : ComicChapterCatalogs) {
                                                        if (chapterItem.getChapter_id().equals(chapterItem1.getChapter_id())) {//只保留本地数据的 章节本地路径 和章节阅读进度数据 其他数据已服务端为准

                                                            chapterItem.setCurrent_read_img_image_id(chapterItem1.getCurrent_read_img_image_id());
                                                            chapterItem.setCurrent_read_img_order(chapterItem1.getCurrent_read_img_order());
                                                            chapterItem.IsRead = chapterItem1.IsRead;

                                                            chapterItem.setImagesText(chapterItem1.ImagesText);
                                                            LitePal.delete(ComicChapter.class, chapterItem1.getId());
                                                            break flag;//跳出内循环
                                                        }
                                                    }
                                                }

                                            }
                                            // LitePal.deleteAll(ComicChapter.class, "comic_id = ?", comic_id);
                                            LitePal.saveAll(comicChapterCatalogs);
                                        }

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    if (baseComic.getId() != 0) {
                                        ContentValues values = new ContentValues();
                                        values.put("Chapter_text", Chapter_text_Sign);
                                        LitePal.update(BaseComic.class, values, baseComic.getId());
                                    }
                                    baseComic.setChapter_text(Chapter_text_Sign);

                                }

                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {


                                        comicChapterCatalogAdapter = new ComicChapterCatalogAdapter(activity, baseComic.getCurrent_chapter_id(), comicChapterCatalogs, H96);
                                        fragment_comicinfo_mulu_list.setAdapter(comicChapterCatalogAdapter);

                                        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) fragment_comicinfo_mulu_list.getLayoutParams();
                                        layoutParams.height = ImageUtil.dp2px(activity, 30) + H96 * comicChapterCatalogs.size();
                                        fragment_comicinfo_mulu_list.setLayoutParams(layoutParams);

                                        muluLorded.getReadChapterItem(comicChapterCatalogs);
                                    }
                                });
                            }
                        }).start();

                    }

                    @Override
                    public void onErrorResponse(String ex) {
                        if (ex != null && ex.equals("nonet")) {
                            MyToash.Log("nonet", "11");

                            ComicChapterCatalogs = LitePal.where("comic_id = ?", comic_id).find(ComicChapter.class);
                            comicChapterCatalogAdapter = new ComicChapterCatalogAdapter(activity, baseComic.getCurrent_chapter_id(), comicChapterCatalogs, H96);
                            fragment_comicinfo_mulu_list.setAdapter(comicChapterCatalogAdapter);
                            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) fragment_comicinfo_mulu_list.getLayoutParams();
                            layoutParams.height = H96 * comicChapterCatalogs.size();
                            fragment_comicinfo_mulu_list.setLayoutParams(layoutParams);
                            muluLorded.getReadChapterItem(comicChapterCatalogs);
                            MyToash.Log("nonet", ComicChapterCatalogs.size());
                        }

                    }
                }

        );
    }

    public interface GetCOMIC_catalogList {
        void GetCOMIC_catalogList(List<ComicChapter> comicChapterList);
    }

    public static void GetCOMIC_catalog(Activity activity, String comic_id, GetCOMIC_catalogList getCOMIC_catalogList) {

        LitePal.where("comic_id = ?", comic_id).findAsync(ComicChapter.class).listen(new FindMultiCallback<ComicChapter>() {
            @Override
            public void onFinish(List<ComicChapter> comicChapterList) {
                if (comicChapterList.isEmpty()) {
               /*     if(comicChapterListold!=null){
                        LitePal.saveAllAsync(comicChapterListold).listen(new SaveCallback() {
                            @Override
                            public void onFinish(boolean success) {
                                getCOMIC_catalogList.GetCOMIC_catalogList(comicChapterListold);

                            }
                        });
                        return;
                    }*/
                    ReaderParams params = new ReaderParams(activity);
                    params.putExtraParams("comic_id", comic_id);
                    String json = params.generateParamsJson();
                    HttpUtils.getInstance(activity).sendRequestRequestParams3(ComicConfig.COMIC_catalog, json, true, new HttpUtils.ResponseListener() {
                                @Override
                                public void onResponse(final String result) {

                                    try {
                                        JSONObject jsonObject = new JSONObject(result);
                                        JsonParser jsonParser = new JsonParser();
                                        JsonArray jsonElements = jsonParser.parse(jsonObject.getString("chapter_list")).getAsJsonArray();//获取JsonArray对象
                                        for (JsonElement jsonElement : jsonElements) {
                                            ComicChapter comicChapter = new Gson().fromJson(jsonElement, ComicChapter.class);
                                            comicChapter.comic_id = comic_id;
                                            comicChapterList.add(comicChapter);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    if (!comicChapterList.isEmpty()) {
                                        LitePal.saveAllAsync(comicChapterList).listen(new SaveCallback() {
                                            @Override
                                            public void onFinish(boolean success) {
                                                getCOMIC_catalogList.GetCOMIC_catalogList(comicChapterList);
                                            }
                                        });
                                    } else {
                                        getCOMIC_catalogList.GetCOMIC_catalogList(null);
                                    }
                                }

                                @Override
                                public void onErrorResponse(String ex) {
                                    getCOMIC_catalogList.GetCOMIC_catalogList(null);
                                }
                            }

                    );

                } else {
                    getCOMIC_catalogList.GetCOMIC_catalogList(comicChapterList);
                }

            }
        });


    }
}
