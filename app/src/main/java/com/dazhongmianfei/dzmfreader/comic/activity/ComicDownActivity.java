package com.dazhongmianfei.dzmfreader.comic.activity;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.dazhongmianfei.dzmfreader.R;
import com.dazhongmianfei.dzmfreader.R2;
import com.dazhongmianfei.dzmfreader.Task.InstructTask;
import com.dazhongmianfei.dzmfreader.Task.TaskManager;
import com.dazhongmianfei.dzmfreader.activity.BaseButterKnifeActivity;
import com.dazhongmianfei.dzmfreader.comic.adapter.ComicDownOptionAdapter;
import com.dazhongmianfei.dzmfreader.comic.been.BaseComic;
import com.dazhongmianfei.dzmfreader.comic.been.BaseComicImage;
import com.dazhongmianfei.dzmfreader.comic.been.ComicChapter;
import com.dazhongmianfei.dzmfreader.comic.been.ComicChapterItem;
import com.dazhongmianfei.dzmfreader.comic.been.ComicDownOptionData;
import com.dazhongmianfei.dzmfreader.comic.config.ComicConfig;
import com.dazhongmianfei.dzmfreader.comic.dialog.PurchaseDialog;
import com.dazhongmianfei.dzmfreader.comic.eventbus.ComicChapterEventbus;
import com.dazhongmianfei.dzmfreader.comic.eventbus.DownComicEvenbus;
import com.dazhongmianfei.dzmfreader.comic.fragment.ComicinfoMuluFragment;
import com.dazhongmianfei.dzmfreader.dialog.WaitDialog;
import com.dazhongmianfei.dzmfreader.eventbus.BuyLoginSuccess;
import com.dazhongmianfei.dzmfreader.eventbus.RefreshMine;
import com.dazhongmianfei.dzmfreader.http.ReaderParams;
import com.dazhongmianfei.dzmfreader.utils.FileManager;
import com.dazhongmianfei.dzmfreader.utils.HttpUtils;
import com.dazhongmianfei.dzmfreader.utils.LanguageUtil;
import com.dazhongmianfei.dzmfreader.utils.MyToash;
import com.dazhongmianfei.dzmfreader.utils.ShareUitls;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.LitePal;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.OnClick;

import static com.dazhongmianfei.dzmfreader.comic.fragment.DownMangerComicFragment.DownMangerComicFragment;
import static com.dazhongmianfei.dzmfreader.utils.FileManager.GlideCopy;

public class ComicDownActivity extends BaseButterKnifeActivity {
    @Override
    public int initContentView() {
        return R.layout.activity_comicdown;
    }

    @BindView(R2.id.titlebar_text)
    public TextView titlebar_text;
    @BindView(R2.id.activity_comicdown_choose_count)
    public TextView activity_comicdown_choose_count;
    @BindView(R2.id.fragment_comicinfo_mulu_zhuangtai)
    public TextView fragment_comicinfo_mulu_zhuangtai;
    @BindView(R2.id.activity_comicdown_down)
    public TextView activity_comicdown_down;


    @BindView(R2.id.activity_comicdown_gridview)
    public GridView activity_comicdown_gridview;

    @BindView(R2.id.fragment_bookshelf_noresult)
    public LinearLayout fragment_bookshelf_noresult;


    @BindView(R2.id.fragment_comicinfo_mulu_xu)
    public TextView fragment_comicinfo_mulu_xu;
    @BindView(R2.id.fragment_comicinfo_mulu_xu_img)
    public ImageView fragment_comicinfo_mulu_xu_img;
    @BindView(R2.id.fragment_comicinfo_mulu_layout)
    public RelativeLayout fragment_comicinfo_mulu_layout;

    private TaskManager mTaskManager;
    boolean shunxu, Flag;
    String comic_id;
    List<ComicChapter> comicDownOptionList;
    ComicDownOptionAdapter comicDownOptionAdapter;
    Gson gson = new Gson();
    //  JsonParser jsonParser = new JsonParser();
    List<ComicChapter> comicChapterCatalogs;
    BaseComic baseComic;
    int down_chapters;
    long id;
    int Size;

    @OnClick(value = {R.id.titlebar_back, R.id.activity_comicdown_quanxuan, R.id.activity_comicdown_down, R.id.fragment_comicinfo_mulu_layout})
    public void getEvent(View view) {
        switch (view.getId()) {
            case R.id.titlebar_back:

                finish();

                break;
            case R.id.activity_comicdown_quanxuan:
                comicDownOptionAdapter.selectAll();
                break;
            case R.id.fragment_comicinfo_mulu_layout:
                shunxu = !shunxu;
                if (!shunxu) {
                    fragment_comicinfo_mulu_xu.setText(LanguageUtil.getString(activity, R.string.fragment_comic_info_zhengxu));
                    fragment_comicinfo_mulu_xu_img.setImageResource(R.mipmap.positive_order);
                } else {
                    fragment_comicinfo_mulu_xu.setText(LanguageUtil.getString(activity, R.string.fragment_comic_info_daoxu));
                    fragment_comicinfo_mulu_xu_img.setImageResource(R.mipmap.reverse_order);
                }
                Collections.reverse(comicDownOptionList);
                comicDownOptionAdapter.notifyDataSetChanged();

                break;
            case R.id.activity_comicdown_down://  public List<ComicDownOption> comicDownOptionListChooseDwn;
                if (!Flag) {//下载
                    String Chapter_id = "";
                    for (ComicChapter comicDownOption : comicDownOptionAdapter.comicDownOptionListChooseDwn) {
                        Chapter_id += "," + comicDownOption.chapter_id;
                    }
                    httpDownChapter(Chapter_id.substring(1));

                } else {//删除

                    for (ComicChapter comicDownOption : comicDownOptionAdapter.comicDownOptionListChooseDwn) {
                        ShareUitls.putComicDownStatus(activity, comicDownOption.chapter_id, 0);//

                        ContentValues values = new ContentValues();//设置该章节为 没有下载过
                        values.put("ISDown", "0");
                        LitePal.update(ComicChapter.class, values, comicDownOption.getId());


                        String localPath = FileManager.getManhuaSDCardRoot().concat(baseComic.getComic_id() + "/").concat(comicDownOption.chapter_id);
                        FileManager.deleteFile(localPath);//删除章节的图片

                    }
                    int size = comicDownOptionAdapter.comicDownOptionListChooseDwn.size();
                    int deleteSize = Size - size;
                    if (deleteSize == 0) {
                        fragment_bookshelf_noresult.setVisibility(View.VISIBLE);
                    }
                    baseComic.setDown_chapters(deleteSize);
                    ContentValues values1 = new ContentValues();
                    values1.put("down_chapters", deleteSize);
                    LitePal.update(BaseComic.class, values1, id);
                    EventBus.getDefault().post(baseComic);//更新上一界面的 数据

                    comicDownOptionList.removeAll(comicDownOptionAdapter.comicDownOptionListChooseDwn);
                    comicDownOptionAdapter = new ComicDownOptionAdapter(activity, comicDownOptionList, activity_comicdown_choose_count, activity_comicdown_down, Flag);
                    activity_comicdown_gridview.setAdapter(comicDownOptionAdapter);
                    comicDownOptionAdapter.comicDownOptionListChooseDwn.clear();
                    MyToash.ToashSuccess(activity, String.format(LanguageUtil.getString(activity, R.string.ReadHistoryFragment_yishanchus), size));

                }
                break;
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        Intent intent = getIntent();
        baseComic = (BaseComic) intent.getSerializableExtra("baseComic");
        // comicChapterCatalogs = (List<ComicChapter>) (intent.getSerializableExtra("comicChapter"));
        Flag = intent.getBooleanExtra("flag", false);//是否只显示已下载列表
        if (!Flag) {
            mTaskManager = new TaskManager();
        }
        comic_id = baseComic.getComic_id();
        down_chapters = baseComic.getDown_chapters();
        id = baseComic.getId();
        MyToash.Log("baseComicid", id + "");
        comicDownOptionList = new ArrayList<>();
        ComicinfoMuluFragment.GetCOMIC_catalog(activity, comic_id, new ComicinfoMuluFragment.GetCOMIC_catalogList() {
            @Override
            public void GetCOMIC_catalogList(List<ComicChapter> comicChapterList) {
                if (comicChapterList != null && !comicChapterList.isEmpty()) {
                    comicChapterCatalogs = comicChapterList;
                    httpData();
                }
            }
        });
    }


    public void httpData() {
        if (Flag) {//下载管理
            fragment_comicinfo_mulu_layout.setVisibility(View.GONE);
            comicDownOptionList = LitePal.where("comic_id = ? and ISDown=?", comic_id, "1").find(ComicChapter.class);
            Size = comicDownOptionList.size();
            if (Size != 0) {
                activity_comicdown_down.setText(LanguageUtil.getString(activity, R.string.ReadHistoryFragment_shangchu));
                fragment_comicinfo_mulu_zhuangtai.setText(String.format(LanguageUtil.getString(activity, R.string.ComicDownActivity_yixiazai), Size));
                comicDownOptionAdapter = new ComicDownOptionAdapter(activity, comicDownOptionList, activity_comicdown_choose_count, activity_comicdown_down, Flag);
                activity_comicdown_gridview.setAdapter(comicDownOptionAdapter);
            } else {
                fragment_bookshelf_noresult.setVisibility(View.VISIBLE);
            }
            titlebar_text.setText(LanguageUtil.getString(activity, R.string.BookInfoActivity_down_manger));
            return;
        }
        titlebar_text.setText(LanguageUtil.getString(activity, R.string.ComicDownActivity_title));

        ReaderParams params = new ReaderParams(activity);
        params.putExtraParams("comic_id", comic_id);
        String json = params.generateParamsJson();
        HttpUtils.getInstance(activity).sendRequestRequestParams3(ComicConfig.COMIC_down_option, json, true, new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(final String result) {

                        try {
                            JSONObject jsonObject = new JSONObject(result);

                            ComicDownOptionData.Base_info base_info = gson.fromJson(jsonObject.getString("base_info"), ComicDownOptionData.Base_info.class);
                            ;
                            fragment_comicinfo_mulu_zhuangtai.setText(base_info.display_label);

                            // List<ComicChapter> down_list;

                            JsonParser jsonParser = new JsonParser();
                            JsonArray jsonElements = jsonParser.parse(jsonObject.getString("down_list")).getAsJsonArray();//获取JsonArray对象
                            for (JsonElement jsonElement : jsonElements) {
                                ComicChapter comicChapter = gson.fromJson(jsonElement, ComicChapter.class);
                                if (ShareUitls.getComicDownStatus(activity, comicChapter.chapter_id, 0) == 2) {//把下载中状态改为 下载失败  否则下载失败的不能继续下载
                                    ShareUitls.putComicDownStatus(activity, comicChapter.chapter_id, 3);
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        ComicDownOptionData comicDownOptionData = gson.fromJson(result, ComicDownOptionData.class);
                        fragment_comicinfo_mulu_zhuangtai.setText(comicDownOptionData.base_info.display_label);


                        if (!comicDownOptionData.down_list.isEmpty()) {
                            comicDownOptionList.addAll(comicDownOptionData.down_list);
                            comicDownOptionAdapter = new ComicDownOptionAdapter(activity, comicDownOptionList, activity_comicdown_choose_count, activity_comicdown_down, Flag);
                            activity_comicdown_gridview.setAdapter(comicDownOptionAdapter);
                        }
                    }

                    @Override
                    public void onErrorResponse(String ex) {


                    }
                }

        );
    }

    public void httpDownChapter(String chapter_id) {
        ReaderParams params = new ReaderParams(activity);
        params.putExtraParams("comic_id", comic_id);
        params.putExtraParams("chapter_id", chapter_id);
        String json = params.generateParamsJson();
        HttpUtils.getInstance(activity).sendRequestRequestParamsDialog(ComicConfig.COMIC_down, json, new HttpUtils.ResponseListenerDialog() {
                    @Override
                    public void onResponse(final String result, WaitDialog waitDialog) {

                        Collections.sort(comicDownOptionAdapter.comicDownOptionListChooseDwn);// 排序
                        for (ComicChapter comicDownOption : comicDownOptionAdapter.comicDownOptionListChooseDwn) {
                            ShareUitls.putComicDownStatus(activity, comicDownOption.chapter_id, 2);

                        }
                        comicDownOptionAdapter.notifyDataSetChanged();
                        comicDownOptionAdapter.comicDownOptionListChooseDwn.clear();
                        comicDownOptionAdapter.refreshBtn(0);
                        MyToash.Log("XXomicChapter22", "333");
                        MyToash.ToashSuccess(activity, LanguageUtil.getString(activity, R.string.BookInfoActivity_down_adddown));

                        Intent intent = new Intent();
                        intent.setAction("com.dazhongmianfei.dzmfreader.comic.activity.DownComicService");
                        intent.setPackage("com.dazhongmianfei.dzmfreader");
                        Bundle bundle2 = new Bundle();
                        bundle2.putSerializable("baseComic", baseComic);
                        bundle2.putString("result", result);
                        bundle2.putSerializable("comicChapter", (Serializable) comicChapterCatalogs);
                        intent.putExtra("downcomic",bundle2);

                        //final Intent eintent = createExplicitFromImplicitIntent(activity,intent);
                        startService(intent);


                     /*   mTaskManager.addQueueTask(new InstructTask<String, String>(null) {

                            @Override
                            public String doRun(String s) {
                                MyToash.Log("XXomicChapter22", "555");
                                //  download(comic_id, result);
                                DaownData(result, comic_id);
                                return null;
                            }
                        });*/
                        waitDialog.dismissDialog();

                    }

                    @Override
                    public void onErrorResponse(String ex) {

                        if (ex != null && ex.equals("701")) {
                             purchaseDialog = new PurchaseDialog(activity, true, new PurchaseDialog.BuySuccess() {
                                @Override
                                public void buySuccess(String[] ids, int num) {
                                    for (ComicChapter comicDownOption : comicDownOptionAdapter.comicDownOptionListChooseDwn) {
                                        comicDownOption.is_preview = 1;
                                    }
                                    comicDownOptionAdapter.notifyDataSetChanged();

                                    httpDownChapter(chapter_id);
                                }
                            }, true);
                            purchaseDialog.initData(comic_id, chapter_id);
                            purchaseDialog.show();
                            ;
                        }

                    }
                }

        );
    }

    PurchaseDialog purchaseDialog;
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refresh(RefreshMine refreshMine) {

        if(purchaseDialog!=null&&purchaseDialog.isShowing()){
            purchaseDialog.dismiss();
        }
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
                                comicDownOptionList.get(i).is_preview=comicChapter.is_preview;
                                ++i;
                            }
                            comicDownOptionAdapter.notifyDataSetChanged();
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
    public void DownComicEvenbus(DownComicEvenbus downComicEvenbus) {//
        MyToash.Log("DownComicEvenbus",downComicEvenbus.baseComic.getComic_id());
        try {
            if (downComicEvenbus.baseComic.getComic_id().equals(comic_id)) {
                if (downComicEvenbus.flag) {
                    //MyToash.ToashSuccess(activity, String.format(LanguageUtil.getString(activity, R.string.BookInfoActivity_down_downcompleteSize), downComicEvenbus.Down_Size));
                    MyToash.ToashSuccess(activity, LanguageUtil.getString(activity, R.string.BookInfoActivity_down_downcompleteSize));

                } else {
                    comicDownOptionAdapter.notifyDataSetChanged();
                }
            }
        } catch (Exception e) {
        }
    }


/*    // 保存图片到手机
    @SuppressLint("StaticFieldLeak")
    public void download(String comic_id, final String result) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                DaownData(result, comic_id);
            }
        }).start();
    }

    int TotalChapter = 0;*/

    public  Intent createExplicitFromImplicitIntent(Context context, Intent implicitIntent) {
        // Retrieve all services that can match the given intent
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> resolveInfo = pm.queryIntentServices(implicitIntent, 0);//根据自己所需意图返回能够响应的服务列表

        // Make sure only one match was found
        if (resolveInfo == null || resolveInfo.size() != 1) {
            return null;
        }

        // Get component info and create ComponentName
        ResolveInfo serviceInfo = resolveInfo.get(0);//得到对应的Intent，因为只有一个所以索引为0
        String packageName = serviceInfo.serviceInfo.packageName;//得到其包名
        String className = serviceInfo.serviceInfo.name;//得到其类名
        ComponentName component = new ComponentName(packageName, className);

        // Create a new intent. Use the old one for extras and such reuse
        Intent explicitIntent = new Intent(implicitIntent);//使用原Intent创建

        // Set the component to be explicit
        explicitIntent.setComponent(component);//设置component,创建明确的Intent

        return explicitIntent;
    }
}
