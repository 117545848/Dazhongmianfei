package com.dazhongmianfei.dzmfreader.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.core.widget.NestedScrollView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dazhongmianfei.dzmfreader.bean.ChapterItem;
import com.dazhongmianfei.dzmfreader.bean.InfoBook;
import com.dazhongmianfei.dzmfreader.bean.InfoBookItem;
import com.dazhongmianfei.dzmfreader.read.manager.ChapterManager;
import com.dazhongmianfei.dzmfreader.utils.FileManager;
import com.fm.openinstall.OpenInstall;
import com.fm.openinstall.listener.AppInstallAdapter;
import com.fm.openinstall.model.AppData;
import com.google.gson.Gson;
import com.dazhongmianfei.dzmfreader.R;
import com.dazhongmianfei.dzmfreader.R2;
import com.dazhongmianfei.dzmfreader.book.been.BaseBook;
import com.dazhongmianfei.dzmfreader.bean.Recommend;
import com.dazhongmianfei.dzmfreader.comic.been.BaseComic;
import com.dazhongmianfei.dzmfreader.config.ReaderConfig;
import com.dazhongmianfei.dzmfreader.dialog.WaitDialog;
import com.dazhongmianfei.dzmfreader.http.ReaderParams;
import com.dazhongmianfei.dzmfreader.utils.HttpUtils;
import com.dazhongmianfei.dzmfreader.utils.ImageUtil;
import com.dazhongmianfei.dzmfreader.utils.LanguageUtil;
import com.dazhongmianfei.dzmfreader.utils.MyPicasso;
import com.dazhongmianfei.dzmfreader.utils.MyToash;
import com.dazhongmianfei.dzmfreader.utils.ScreenSizeUtils;
import com.dazhongmianfei.dzmfreader.utils.ShareUitls;
import com.dazhongmianfei.dzmfreader.utils.Utils;

//.http.RequestParams;
//.view.annotation.ContentView;
//.view.annotation.ViewInject;
//.x;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.LitePal;
import org.litepal.crud.callback.SaveCallback;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

//Updated upstream:app/src/main/java/com.dazhongmianfei.dzmfreader/activity/FirstStartActivity.java
///src/main/java/com/haozan/novelapp/activity/FirstStartActivity.java

/**
 * Created by abc on 2016/11/4.
 */

public class FirstStartActivity extends BaseButterKnifeActivity {
    @Override
    public int initContentView() {
        return R.layout.activity_firststart;
    }

    @BindView(R2.id.activity_home_book_layout)
    public LinearLayout activity_home_viewpager_book_ScrollView;
    @BindView(R2.id.activity_home_sex_layout)
    public LinearLayout activity_home_sex_layout;
    @BindView(R2.id.activity_home_viewpager_sex_next)
    public TextView activity_home_viewpager_sex_next;
    @BindView(R2.id.activity_home_viewpager_sex_boy)
    public ImageView activity_home_viewpager_sex_boy;
    @BindView(R2.id.activity_home_viewpager_sex_gril)
    public ImageView activity_home_viewpager_sex_gril;
    @BindView(R2.id.activity_home_viewpager_sex_gril_choose)
    public ImageView activity_home_viewpager_sex_gril_choose;
    @BindView(R2.id.activity_home_viewpager_sex_boy_choose)
    public ImageView activity_home_viewpager_sex_boy_choose;

    @BindView(R2.id.activity_home_viewpager_sex_ok)
    public Button activity_home_viewpager_sex_ok;
    @BindView(R2.id.activity_home_viewpager_book_next)
    public TextView activity_home_viewpager_book_next;
    @BindView(R2.id.activity_home_viewpager_book_ok)
    public Button activity_home_viewpager_book_ok;
    @BindView(R2.id.activity_home_viewpager_book_GridView)
    public GridView activity_home_viewpager_book_GridView;
    int WIDTH, HEIGHT;
    private WaitDialog waitDialog;

    @OnClick(value = {R.id.activity_home_viewpager_sex_boy,
            R.id.activity_home_viewpager_sex_gril,
            R.id.activity_home_viewpager_sex_next,
            R.id.activity_home_viewpager_book_next,
            R.id.activity_home_viewpager_sex_ok,
            R.id.activity_home_viewpager_book_ok
    })
    public void getEvent(View view) {
        switch (view.getId()) {
            case R.id.activity_home_viewpager_sex_boy:
                chooseBoy = !chooseBoy;
                if (chooseBoy) {
                    chooseGril = false;
                    ShareUitls.putString(activity, "sex", "boy");
                    ShareUitls.putString(activity, "sextemp", "boy");
                    activity_home_viewpager_sex_boy.setImageResource(R.mipmap.boy_yes);
                    activity_home_viewpager_sex_gril.setImageResource(R.mipmap.gril_no);
                    activity_home_viewpager_sex_gril_choose.setVisibility(View.GONE);
                    activity_home_viewpager_sex_boy_choose.setVisibility(View.VISIBLE);
                } else {
                    ShareUitls.putString(activity, "sex", "");
                    ShareUitls.putString(activity, "sextemp", "");
                    activity_home_viewpager_sex_boy.setImageResource(R.mipmap.boy_no);
                    activity_home_viewpager_sex_boy_choose.setVisibility(View.GONE);
                }
                break;
            case R.id.activity_home_viewpager_sex_gril:
                chooseGril = !chooseGril;
                if (chooseGril) {
                    chooseBoy = false;
                    ShareUitls.putString(activity, "sex", "gril");
                    ShareUitls.putString(activity, "sextemp", "gril");
                    activity_home_viewpager_sex_boy.setImageResource(R.mipmap.boy_no);
                    activity_home_viewpager_sex_gril.setImageResource(R.mipmap.gril_yes);
                    activity_home_viewpager_sex_gril_choose.setVisibility(View.VISIBLE);
                    activity_home_viewpager_sex_boy_choose.setVisibility(View.GONE);
                } else {
                    ShareUitls.putString(activity, "sex", "");
                    ShareUitls.putString(activity, "sextemp", "");
                    activity_home_viewpager_sex_gril.setImageResource(R.mipmap.gril_no);
                    activity_home_viewpager_sex_gril_choose.setVisibility(View.GONE);
                }
                break;
            case R.id.activity_home_viewpager_sex_next:
            case R.id.activity_home_viewpager_book_next:
                ShareUitls.putString(activity, "isfirst", "no");
                startMainActivity(activity);
                break;
            case R.id.activity_home_viewpager_sex_ok:
                if (!chooseBoy && !chooseGril) {
                    MyToash.ToashError(activity, LanguageUtil.getString(activity, R.string.firstStartactivity_choosesex));
                } else {
                    getRecommend(chooseBoy ? 1 : 2);
                }
                break;

            case R.id.activity_home_viewpager_book_ok:
                if (addrecommendProducs.isEmpty()) {
                    MyToash.ToashError(activity, LanguageUtil.getString(activity, R.string.firstStartactivity_choosebooks));
                } else {
                    activity_home_viewpager_book_ok.setClickable(false);
                    waitDialog.showDailog();
                    List<BaseBook> list = new ArrayList<>();
                    List<BaseComic> comics = new ArrayList<>();
                    for (Recommend.RecommendProduc addrecommendProducs : addrecommendProducs) {

                        if (addrecommendProducs.book_id != null) {
                            BaseBook mBaseBook = new BaseBook();
                            mBaseBook.setBook_id(addrecommendProducs.book_id);
                            mBaseBook.setName(addrecommendProducs.name);
                            mBaseBook.setCover(addrecommendProducs.cover);
                            mBaseBook.setRecentChapter(addrecommendProducs.total_chapter);
                            mBaseBook.setTotal_Chapter(addrecommendProducs.total_chapter);
                            mBaseBook.setDescription(addrecommendProducs.description);
                            mBaseBook.setName(addrecommendProducs.name);
                            mBaseBook.setAddBookSelf(1);
                            // mBaseBook.saveIsexist(1);
                            list.add(mBaseBook);
                        } else {
                            BaseComic baseComic = new BaseComic();
                            baseComic.setComic_id(addrecommendProducs.comic_id);
                            baseComic.setName(addrecommendProducs.name);
                            baseComic.setVertical_cover(addrecommendProducs.cover);
                            baseComic.setRecentChapter(addrecommendProducs.total_chapter);
                            baseComic.setTotal_chapters(addrecommendProducs.total_chapter);
                            baseComic.setDescription(addrecommendProducs.description);
                            baseComic.setAddBookSelf(true);
                            // baseComic.saveIsexist(true);
                            comics.add(baseComic);
                        }
                    }

                    LitePal.saveAllAsync(list).listen(new SaveCallback() {
                        @Override
                        public void onFinish(boolean success) {
                            bookadd = true;
                            //  if (comicadd) {
                            bookadd = false;
                            Intent intent = new Intent(activity, MainActivity.class);
                            intent.putExtra("mBaseBooks", (ArrayList<? extends Serializable>) list);
                            // intent.putExtra("mBaseComics", (ArrayList<? extends Serializable>) comics);
                            startActivity(intent);
                            waitDialog.dismissDialog();
                            if (updateList != null) {
                                ChapterManager.getInstance(activity).openBook(baseBook, book_id, chapter_id, updateList);
                            }


                            //finish();
                            //   }
                        }
                    });

                   /* LitePal.saveAllAsync(list).listen(new SaveCallback() {
                        @Override
                        public void onFinish(boolean success) {
                            bookadd = true;
                            if (comicadd) {
                                bookadd = false;
                                Intent intent = new Intent(activity, MainActivity.class);
                                intent.putExtra("mBaseBooks", (ArrayList<? extends Serializable>) list);
                                intent.putExtra("mBaseComics", (ArrayList<? extends Serializable>) comics);
                                startActivity(intent);
                                waitDialog.dismissDialog();
                                finish();
                            }
                        }
                    });
                    LitePal.saveAllAsync(comics).listen(new SaveCallback() {
                        @Override
                        public void onFinish(boolean success) {
                            comicadd = true;
                            if (bookadd) {
                                comicadd = false;
                                Intent intent = new Intent(activity, MainActivity.class);
                                intent.putExtra("mBaseBooks", (ArrayList<? extends Serializable>) list);
                                intent.putExtra("mBaseComics", (ArrayList<? extends Serializable>) comics);
                                startActivity(intent);
                                waitDialog.dismissDialog();
                                finish();
                            }
                        }
                    });*/

                }
                break;
        }
    }

    Recommend recommend;
    boolean bookadd = false, comicadd = false;
    boolean chooseBoy;
    boolean chooseGril;
    List<Recommend.RecommendProduc> recommendProducs = new ArrayList<>();
    List<Recommend.RecommendProduc> addrecommendProducs = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WIDTH = ScreenSizeUtils.getInstance(activity).getScreenWidth();
        WIDTH = (WIDTH - ImageUtil.dp2px(activity, 30)) / 3;
        HEIGHT = WIDTH * 4 / 3;
        waitDialog = new WaitDialog(activity);
        waitDialog.setCancleable(true);
        OpenInstall.getInstall(new AppInstallAdapter() {
            @Override
            public void onInstall(AppData appData) {
                String channelCode = appData.getChannel();
                //获取自定义数据
                String bindData = appData.getData();
                try {
                    MyToash.Log("bindData",bindData);
                    JSONObject jsonObject=new JSONObject(bindData);
                    if (jsonObject.has("book_id")) {
                        book_id =jsonObject.getString("book_id");
                    }
                    if (jsonObject.has("chapter_id")) {
                        chapter_id =jsonObject.getString("chapter_id");
                    }
                    if(book_id!=null) {
                        BookHttp();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });

    }

    private void setViewPager() {


        activity_home_viewpager_book_GridView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return recommendProducs.size();
            }

            @Override
            public Recommend.RecommendProduc getItem(int i) {

                return recommendProducs.get(i);

            }

            @Override
            public long getItemId(int i) {
                return i;
            }

            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {
                if (view == null) {
                    view = LayoutInflater.from(activity).inflate(R.layout.activity_home_viewpager_classfy_gridview_item, null);
                }

                ImageView activity_home_viewpager_classfy_GridView_img = view.findViewById(R.id.activity_home_viewpager_classfy_GridView_img);
                TextView activity_home_flag = view.findViewById(R.id.activity_home_flag);
                RelativeLayout activity_home_viewpager_classfy_GridView_laiout = view.findViewById(R.id.activity_home_viewpager_classfy_GridView_laiout);

                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) activity_home_viewpager_classfy_GridView_laiout.getLayoutParams();
                layoutParams.width = WIDTH;
                layoutParams.height = HEIGHT;
                activity_home_viewpager_classfy_GridView_laiout.setLayoutParams(layoutParams);

                final CheckBox activity_home_viewpager_classfy_GridView_box = view.findViewById(R.id.activity_home_viewpager_classfy_GridView_box);

                final Recommend.RecommendProduc recommendProduc = getItem(i);
                // MyPicasso.IoadImage(activity, classfyBook.cover, R.mipmap.book_def, activity_home_viewpager_classfy_GridView_img);
                if (recommendProduc.book_id != null) {
                    activity_home_flag.setVisibility(View.GONE);
                } else {
                    activity_home_flag.setVisibility(View.VISIBLE);
                }
                MyPicasso.GlideImage(activity, recommendProduc.cover, activity_home_viewpager_classfy_GridView_img, WIDTH, HEIGHT);

                activity_home_viewpager_classfy_GridView_laiout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (recommendProduc.isChoose) {
                            recommendProduc.isChoose = false;
                            activity_home_viewpager_classfy_GridView_box.setChecked(false);
                            addrecommendProducs.remove(recommendProduc);

                            if (addrecommendProducs.isEmpty()) {
                                activity_home_viewpager_book_ok.setBackgroundResource(R.drawable.shape_login_bg);
                                activity_home_viewpager_book_ok.setTextColor(Color.parseColor("#8b8b8c"));
                            }
                        } else {
                            recommendProduc.isChoose = true;
                            activity_home_viewpager_classfy_GridView_box.setChecked(true);
                            addrecommendProducs.add(recommendProduc);
                            if (!addrecommendProducs.isEmpty()) {
                                activity_home_viewpager_book_ok.setBackgroundResource(R.drawable.shape_login_enable_bg);
                                activity_home_viewpager_book_ok.setTextColor(Color.WHITE);
                            }
                        }
                    }
                });

                return view;
            }
        });
    }

    private void getRecommend(int flag) {

        ReaderParams readerParams = new ReaderParams(activity);
        readerParams.putExtraParams("channel_id", flag + "");
        String json = readerParams.generateParamsJson();
        HttpUtils.getInstance(activity).sendRequestRequestParams3(ReaderConfig.start_recommend, json, true, new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(String response) {
                        ShareUitls.putString(activity, "isfirst", "no");
                        recommend = new Gson().fromJson(response, Recommend.class);
                        if (recommend != null) {
                            recommendProducs.addAll(recommend.book);
                            recommendProducs.addAll(recommend.comic);
                            activity_home_sex_layout.setVisibility(View.GONE);
                            setViewPager();
                        } else {
                            startMainActivity(activity);
                        }

                    }

                    @Override
                    public void onErrorResponse(String ex) {
                        startMainActivity(activity);
                    }
                }

        );

    }

    public void startMainActivity(Activity activity) {

        startActivity(new Intent(activity, MainActivity.class));
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
    /*    save_recommend(activity, new Save_recommend() {
            @Override
            public void saveSuccess() {

            }
        });*/
    }

    public interface Save_recommend {
        void saveSuccess();
    }

    public static void save_recommend(final Activity activity, final Save_recommend save_recommend) {

     /*   if (Utils.isLogin(activity)) {
            String gender = ShareUitls.getString(activity, "sextemp", "");

            if (gender.length() == 0) {
                save_recommend.saveSuccess();
                return;
            } else {
                ReaderParams params = new ReaderParams(activity);
                if (gender.equals("boy")) {
                    params.putExtraParams("channel_id", "2");
                } else if (gender.equals("gender")) {
                    params.putExtraParams("gender", "1");
                }
                String json = params.generateParamsJson();
                HttpUtils.getInstance(activity).sendRequestRequestParams3(ReaderConfig.save_recommend, json, true, new HttpUtils.ResponseListener() {
                            @Override
                            public void onResponse(String result) {
                                ShareUitls.putString(activity, "sextemp", "");
                                save_recommend.saveSuccess();
                            }

                            @Override
                            public void onErrorResponse(String ex) {
                                save_recommend.saveSuccess();
                            }
                        }

                );


            }
        }*/
    }


    private void BookHttp() {
        ReaderParams params = new ReaderParams(this);
        params.putExtraParams("book_id", book_id);
        String json = params.generateParamsJson();
        HttpUtils.getInstance(activity).sendRequestRequestParams3(ReaderConfig.mBookInfoUrl, json, false, new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(String result) {
                        try {
                            InfoBookItem infoBookItem = new Gson().fromJson(result, InfoBookItem.class);
                            InfoBook infoBook = infoBookItem.book;
                            if (infoBook != null) {
                                baseBook = new BaseBook();
                                baseBook.setName(infoBook.name);
                                baseBook.setBook_id(book_id);
                                baseBook.setCover(infoBook.cover);
                                baseBook.setAuthor(infoBook.author);
                                baseBook.setDescription(infoBook.description);
                                baseBook.setTotal_Chapter(infoBook.total_chapter);
                                baseBook.setRecentChapter(infoBook.total_chapter);
                                baseBook.setName(infoBook.name);
                                if (chapter_id != null) {
                                    baseBook.setCurrent_chapter_id(chapter_id);
                                }
                                initOpenBook(baseBook);
                            }
                        }catch (Exception e){}
                    }

                    @Override
                    public void onErrorResponse(String ex) {

                    }
                }

        );


    }




    List<ChapterItem> updateList;
    String chapter_id;
    String book_id;
    BaseBook baseBook;
    private void initOpenBook(BaseBook baseBook) {
        ReaderParams params = new ReaderParams(activity);
        params.putExtraParams("book_id", book_id);
        String json = params.generateParamsJson();
        HttpUtils.getInstance(activity).sendRequestRequestParams4(ReaderConfig.mChapterCatalogUrl, json, new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(final String result) {
                        try {
                            baseBook.setAddBookSelf(1);
                            String Chapter_text_Sign = Utils.MD5(result);
                            baseBook.setChapter_text(Chapter_text_Sign);
                            baseBook.saveIsexist(1);
                            updateList = new ArrayList<>();
                            int size = 0;
                            JSONObject jsonObj = new JSONObject(result);
                            String bookName = jsonObj.getString("name");
                            JSONArray chapterListArr = jsonObj.getJSONArray("chapter_list");
                            size = chapterListArr.length();
                            if (size == 0) {
                                return;
                            }
                            for (int i = 0; i < size; i++) {
                                JSONObject jsonObject1 = chapterListArr.getJSONObject(i);
                                String tempchapter_id = jsonObject1.getString("chapter_id");
                                if (tempchapter_id != null && tempchapter_id.length() != 0) {
                                    ChapterItem querychapterItem = new ChapterItem();
                                    querychapterItem.setChapter_id(jsonObject1.getString("chapter_id"));
                                    querychapterItem.setChapter_title(jsonObject1.getString("chapter_title"));
                                    querychapterItem.setIs_preview(jsonObject1.getString("is_preview"));
                                    querychapterItem.setUpdate_time(jsonObject1.getString("update_time"));
                                    querychapterItem.setDisplay_order(jsonObject1.getInt("display_order"));
                                    querychapterItem.setChapteritem_begin(0);
                                    querychapterItem.setBook_id(book_id);
                                    querychapterItem.setBook_name(bookName);
                                    querychapterItem.setCharset("utf-8");
                                    if (i + 1 == size) {
                                        querychapterItem.setNext_chapter_id("-2");
                                    } else {
                                        querychapterItem.setNext_chapter_id(chapterListArr.getJSONObject(i + 1).getString("chapter_id"));
                                    }
                                    if (i == 0) {
                                        querychapterItem.setPre_chapter_id("-1");
                                    } else {
                                        querychapterItem.setPre_chapter_id(updateList.get(i - 1).getChapter_id());
                                    }
                                    String filepath = FileManager.getSDCardRoot().concat("Reader/book/").concat(querychapterItem.getBook_id() + "/").concat(querychapterItem.getChapter_id() + "/").concat(querychapterItem.getIs_preview() + "/").concat(querychapterItem.getUpdate_time()).concat(".txt");
                                    querychapterItem.setChapter_path(filepath);
                                    updateList.add(querychapterItem);
                                    LitePal.saveAll(updateList);

                                }
                            }
                        } catch (Exception e) {
                        }

                    }

                    @Override
                    public void onErrorResponse(String ex) {

                    }
                }

        );
    }



}
