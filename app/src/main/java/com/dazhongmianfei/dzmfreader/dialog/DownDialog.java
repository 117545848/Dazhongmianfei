package com.dazhongmianfei.dzmfreader.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.wang.avi.AVLoadingIndicatorView;
import com.wang.avi.indicators.LineSpinFadeLoaderIndicator;
import com.dazhongmianfei.dzmfreader.R;
import com.dazhongmianfei.dzmfreader.activity.BaseOptionActivity;
import com.dazhongmianfei.dzmfreader.activity.DownMangerActivity;
import com.dazhongmianfei.dzmfreader.activity.FeedBackActivity;
import com.dazhongmianfei.dzmfreader.activity.LoginActivity;
import com.dazhongmianfei.dzmfreader.book.been.BaseBook;
import com.dazhongmianfei.dzmfreader.bean.ChapterContent;
import com.dazhongmianfei.dzmfreader.bean.ChapterItem;
import com.dazhongmianfei.dzmfreader.bean.Downoption;
import com.dazhongmianfei.dzmfreader.config.ReaderConfig;
import com.dazhongmianfei.dzmfreader.http.DownloadUtil;
import com.dazhongmianfei.dzmfreader.http.ReaderParams;
import com.dazhongmianfei.dzmfreader.read.dialog.PurchaseDialog;
import com.dazhongmianfei.dzmfreader.read.manager.ChapterManager;
import com.dazhongmianfei.dzmfreader.utils.FileManager;
import com.dazhongmianfei.dzmfreader.utils.HttpUtils;
import com.dazhongmianfei.dzmfreader.utils.LanguageUtil;
import com.dazhongmianfei.dzmfreader.utils.MyToash;
import com.dazhongmianfei.dzmfreader.utils.ScreenSizeUtils;
import com.dazhongmianfei.dzmfreader.utils.ShareUitls;
import com.dazhongmianfei.dzmfreader.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.LitePal;
//.http.RequestParams;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import  static  com.dazhongmianfei.dzmfreader.config.ReaderConfig.DOWN;

public class DownDialog {
    public  boolean showOpen;
     Dialog bottomDialog;
     AVLoadingIndicatorView item_dialog_downadapter_RotationLoadingView;

    public  void showDownoption(final Activity activity, final List<Downoption> downoption) {

        bottomDialog = new Dialog(activity, R.style.BottomDialog);


        if (item_dialog_downadapter_RotationLoadingView != null) {
            item_dialog_downadapter_RotationLoadingView.setVisibility(View.GONE);
        }
        bottomDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                return true;
            }
        });
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_down, null);
        final TextView dialog_dowu_manger = view.findViewById(R.id.dialog_dowu_manger);
        final ListView dialog_dowu_list = view.findViewById(R.id.dialog_dowu_list);


        Dialog_downAdapter dialog_downAdapter = new Dialog_downAdapter(activity, downoption);
        dialog_dowu_list.setAdapter(dialog_downAdapter);
        final boolean[] noclick = {false};
        dialog_dowu_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View v, int i, long l) {
                if (!downoption.get(i).isdown && !noclick[0]) {
                    noclick[0] = true;
                    item_dialog_downadapter_RotationLoadingView = v.findViewById(R.id.item_dialog_downadapter_RotationLoadingView);
                    item_dialog_downadapter_RotationLoadingView.setVisibility(View.VISIBLE);

                    Downoption downoption1 = downoption.get(i);
                    ChapterManager.downChapter(activity, downoption1.book_id, new ChapterManager.DownChapter() {
                        @Override
                        public void success() {

                        /*
                           View view1 = v.findViewById(R.id.item_dialog_downadapter_lable);
                        int[] startLocation = new int[2];// 一个整型数组，用来存储按钮的在屏幕的X、Y坐标
                            view1.getLocationOnScreen(startLocation);// 这是获取购买按钮的在屏幕的X、Y坐标（这也是动画开始的坐标）
                            ImageView animImageView = new ImageView(activity);
                            animImageView.setImageBitmap(getBitmapFromView(view1));// 设置ball的图片
                            setAnim(activity, animImageView, startLocation, dialog_dowu_manger);// 开始执行动画
                            */
                            getdown_url(activity, downoption1);

                        }

                        @Override
                        public void fail() {
                            MyToash.ToashError(activity, "下载异常,请确认网络连接");
                            bottomDialog.dismiss();
                        }
                    });
                }
            }
        });


        dialog_dowu_manger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomDialog.dismiss();
               // activity.startActivity(new Intent(activity, DownMangerActivity.class));

                activity.startActivity(new Intent(activity, BaseOptionActivity.class).putExtra("OPTION",DOWN).putExtra("title",LanguageUtil.getString(activity, R.string.BookInfoActivity_down_manger)));


            }
        });


        bottomDialog.setContentView(view);
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        params.width = ScreenSizeUtils.getInstance(activity).getScreenWidth();
        view.setLayoutParams(params);
        bottomDialog.getWindow().setGravity(Gravity.BOTTOM);
        bottomDialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
        bottomDialog.show();
        bottomDialog.onWindowFocusChanged(false);
        bottomDialog.setCanceledOnTouchOutside(true);

    }


     class Dialog_downAdapter extends BaseAdapter {

        List<Downoption> list;
        Activity activity;

        public Dialog_downAdapter(Activity activity, List<Downoption> list) {
            this.list = list;
            this.activity = activity;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Downoption getItem(int i) {
            return list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            View contentView = LayoutInflater.from(activity).inflate(R.layout.item_dialog_downadapter, null, false);

            TextView item_dialog_downadapter_lable = contentView.findViewById(R.id.item_dialog_downadapter_lable);
            TextView item_dialog_downadapter_tag = contentView.findViewById(R.id.item_dialog_downadapter_tag);
            TextView item_dialog_downadapter_isxiazai = contentView.findViewById(R.id.item_dialog_downadapter_isxiazai);
            AVLoadingIndicatorView item_dialog_downadapter_RotationLoadingView = contentView.findViewById(R.id.item_dialog_downadapter_RotationLoadingView);
            item_dialog_downadapter_RotationLoadingView.setIndicator(new LineSpinFadeLoaderIndicator());

            //
            item_dialog_downadapter_lable.setText(getItem(i).label);
            if (getItem(i).tag.length() > 0) {
                item_dialog_downadapter_tag.setText(getItem(i).tag);
                item_dialog_downadapter_tag.setVisibility(View.VISIBLE);
            } else {
                item_dialog_downadapter_tag.setVisibility(View.GONE);
            }
            if (getItem(i).isdown) {
                item_dialog_downadapter_isxiazai.setVisibility(View.VISIBLE);
                item_dialog_downadapter_RotationLoadingView.setVisibility(View.GONE);
            } else {
                item_dialog_downadapter_isxiazai.setVisibility(View.GONE);
            }
            return contentView;
        }
    }

    private  ChapterItem ChapterItem;

    public  static  boolean isreaderbook;

    public  void getDownoption(final Activity activity, final BaseBook baseBook, ChapterItem chapterItem) {

        if (Getdown_urling) {
            return;
        }
        if (chapterItem == null) {
            isreaderbook = false;
        } else {
            isreaderbook = true;
        }
        ChapterItem = chapterItem;
        String chapter_id = chapterItem != null ? chapterItem.getChapter_id() : (baseBook.getCurrent_chapter_id() == null ? "" : baseBook.getCurrent_chapter_id());
        MyToash.Log("chapter_iddd", (chapterItem == null) + "   " + chapter_id);


        ReaderParams readerParams = new ReaderParams(activity);
        readerParams.putExtraParams("book_id", baseBook.getBook_id());
        readerParams.putExtraParams("chapter_id", chapter_id);

        String json = readerParams.generateParamsJson();

        HttpUtils.getInstance(activity).sendRequestRequestParams3(ReaderConfig.BASE_URL + "/chapter/down-option", json, true, new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(final String result) {

                        try {
                            JSONObject JsonObject = new JSONObject(result);
                            List<Downoption> downoptions = LitePal.where("book_id= ?", baseBook.getBook_id()).find(Downoption.class);
                            JSONArray jsonArray = JsonObject.getJSONArray("down_option");
                            if (jsonArray.length() > 0) {
                                List<Downoption> list = new ArrayList<>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    Downoption downoption = new Downoption();
                                    downoption.label = jsonObject.getString("label");
                                    downoption.tag = jsonObject.getString("tag");
                                    downoption.s_chapter = jsonObject.getString("s_chapter");
                                    downoption.down_num = jsonObject.getInt("down_num");
                                    downoption.file_name = jsonObject.getString("file_name");
                                    downoption.tag = jsonObject.getString("tag");
                                    downoption.book_id = baseBook.getBook_id();
                                    downoption.cover = baseBook.getCover();
                                    downoption.bookname = baseBook.getName();
                                    downoption.description = baseBook.getDescription();
                                    downoption.start_order = jsonObject.getInt("start_order");
                                    downoption.end_order = jsonObject.getInt("end_order");
                                    // String str0 = ShareUitls.getDown(activity, downoption.book_id, "0-0");
                                    downoption.isdown = false;

                                    for (Downoption downoption1 : downoptions) {//去重 起始 和结束序号 包含在已下载内 设为已下载
                                        if (downoption1.start_order <= downoption.start_order && downoption.end_order <= downoption1.end_order) {
                                            downoption.isdown = true;
                                        }
                                    }
/*
                                    if (str0.equals("0-0")) {
                                        downoption.isdown = false;
                                    } else {
                                        String[] str = str0.split("-");
                                        if (downoption.start_order >= Integer.parseInt(str[0]) && downoption.end_order <= Integer.parseInt(str[1])) {
                                            downoption.isdown = true;
                                        } else {
                                            downoption.isdown = false;
                                        }
                                    }*/
                                    // MyToash.Log(" downoption.showHead", downoption.showHead + "  " + str0 + "  " + i);
                                    list.add(downoption);
                                }

                                showDownoption(activity, list);
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onErrorResponse(String  ex) {

                    }
                }

        );

    }

    public  boolean Getdown_urling = false;

    public  void getdown_url(final Activity activity, final Downoption downoption) {
        Getdown_urling = true;
        ReaderParams readerParams = new ReaderParams(activity);
        readerParams.putExtraParams("book_id", downoption.book_id);
        readerParams.putExtraParams("chapter_id", downoption.s_chapter);
        readerParams.putExtraParams("num", downoption.down_num + "");
        String json = readerParams.generateParamsJson();
        HttpUtils.getInstance(activity).sendRequestRequestParams3(ReaderConfig.BASE_URL + "/chapter/down-url", json, false, new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(final String result) {
                        Getdown_urling = false;
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            final String file_url = jsonObject.getString("file_url");
                            final String file_name = jsonObject.getString("file_name");
                            new Thread(new Runnable() {
                                @Override
                                public void run() {

                                    downFile(activity, file_url, file_name, downoption);
                                }
                            }).start();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onErrorResponse(String  ex) {
                        if(ex!=null&&ex.equals("701")){

                            if (bottomDialog != null) {
                                bottomDialog.dismiss();
                            }
                            PurchaseDialog mPurchaseDialog = new PurchaseDialog(activity, true);
                            String str = downoption.label;
                            if (ChapterItem != null) {
                                str = ChapterItem.getChapter_title() + " (" + str + ")";
                            }
                            mPurchaseDialog.initData(downoption.book_id, downoption.s_chapter, downoption, str);
                            mPurchaseDialog.show();
                        }else {
                            Getdown_urling = false;
                        }
                    }
                }

        );
    }

    @SuppressLint("HandlerLeak")
     Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    MyToash.ToashSuccess(Activity, LanguageUtil.getString(Activity, R.string.BookInfoActivity_down_adddown));
                    if (item_dialog_downadapter_RotationLoadingView != null) {
                        item_dialog_downadapter_RotationLoadingView.setVisibility(View.GONE);
                    }
                    if (bottomDialog != null) {
                        bottomDialog.dismiss();
                    }
                    break;
                case 1:
                    MyToash.ToashSuccess(Activity, LanguageUtil.getString(Activity, R.string.BookInfoActivity_down_downcomplete));
                    break;
                case 2:
                    break;
            }
        }
    };
    public  Activity Activity;

    private  void downFile(final Activity activity, String url, String file_name, final Downoption downoption) {
        DownloadUtil.get().download(url, FileManager.getSDCardRoot() + "downoption/", file_name,
                new DownloadUtil.OnDownloadListener() {
                    @Override
                    public void onDownloadSuccess(File file) {
                        Activity = activity;

                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                        downoption.downoption_date = formatter.format(System.currentTimeMillis());
                        ShareUitls.putDown(activity, downoption.book_id, downoption.start_order + "-" + downoption.end_order);

                        List<Downoption> downoptions = LitePal.where("book_id= ?", downoption.book_id).find(Downoption.class);
                        for (Downoption downoption1 : downoptions) {//当前开始序号 包含在 已经下载的 某条序号记录内   重新记录最新的下载记录 为两个叠加
                            if (downoption.start_order <= downoption1.start_order && downoption1.end_order <= downoption.end_order) {
                                LitePal.deleteAll(Downoption.class, "file_name=?", downoption1.file_name);
                            } else if (downoption1.start_order <= downoption.start_order && downoption.start_order <= downoption1.end_order && downoption.end_order > downoption1.end_order) {
                                LitePal.deleteAll(Downoption.class, "file_name=?", downoption1.file_name);
                                downoption.start_order = downoption1.start_order;
                            } else {
                                if (downoption1.start_order <= downoption.end_order && downoption.end_order <= downoption1.end_order && downoption.start_order < downoption1.start_order) {
                                    LitePal.deleteAll(Downoption.class, "file_name=?", downoption1.file_name);
                                    downoption.end_order = downoption1.end_order;

                                }
                            }
                        }


                        downoption.save();
                        handler.sendEmptyMessage(0);

                        EventBus.getDefault().post(downoption);//通知进度更新

                        int size = 0;
                        String txt2String = FileManager.txt2String(file);
                        JsonParser jsonParser = new JsonParser();
                        JsonArray jsonElements = jsonParser.parse(txt2String).getAsJsonArray();//获取JsonArray对象

                        Gson gson = new Gson();

                        int temp = 0;
                        for (JsonElement jsonElement : jsonElements) {
                            ChapterContent chapterContent = gson.fromJson(jsonElement, ChapterContent.class);//解析
                            String filepath = FileManager.getSDCardRoot().concat("Reader/book/").concat(downoption.book_id + "/").concat(chapterContent.getChapter_id() + "/").concat(chapterContent.getIs_preview()+ "/").concat(chapterContent.getUpdate_time()).concat(".txt");
                            FileManager.createFile(filepath, chapterContent.getContent().getBytes());
                            size += chapterContent.getWords() * 3;
                            ++temp;
                            downoption.down_cunrrent_num = temp;
                            EventBus.getDefault().post(downoption);//通知进度更新
                        }
                        downoption.downoption_size = (int) (((float) size / 1048576f) * 100) / 100f + "M";
                        downoption.isdown = true;

                        ContentValues values = new ContentValues();
                        values.put("downoption_size", (int) (((float) size / 1048576f) * 100) / 100f + "M");
                        values.put("isdown", true);
                        LitePal.updateAll(Downoption.class, values, "file_name = ?", downoption.file_name);

                        EventBus.getDefault().post(downoption);


                        handler.sendEmptyMessage(1);
                    }

                    @Override
                    public void onDownloading(int totle, int progress) {
                    }

                    @Override
                    public void onDownloadFailed(Exception e) {
                     /*   activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                MyToash.Toash(activity,LanguageUtil.getString(activity,R.string.BookInfoActivity_down_downfile));
                            }
                        });*/

                    }
                });


    }


    /**
     * 设置动画
     *
     * @param v
     * @param startLocation
     * @param view
     */

     ViewGroup viewGroup;

    private  void setAnim(Activity activity, final View v, int[] startLocation, final View bottom) {

        viewGroup = null;
        viewGroup = createAnimLayout(activity);
        viewGroup.addView(v);// 把动画小球添加到动画层
        final View viewa = addViewToAnimLayout(viewGroup, v,
                startLocation);
        int[] endLocation = new int[2];// 存储动画结束位置的X、Y坐标
        bottom.getLocationOnScreen(endLocation);// shopCart是那个购物车

        // 计算位移
        int endX = endLocation[0] - startLocation[0];// 动画位移的X坐标
        int endY = endLocation[1] - startLocation[1];// 动画位移的y坐标
        TranslateAnimation translateAnimationX = new TranslateAnimation(0,
                endX, 0, 0);
        translateAnimationX.setInterpolator(new LinearInterpolator());
        translateAnimationX.setRepeatCount(0);// 动画重复执行的次数
        translateAnimationX.setFillAfter(true);

        TranslateAnimation translateAnimationY = new TranslateAnimation(0, 0,
                0, endY);
        translateAnimationY.setInterpolator(new AccelerateInterpolator());
        translateAnimationY.setRepeatCount(0);// 动画重复执行的次数
        translateAnimationX.setFillAfter(true);

        ScaleAnimation scaleAnimation = new ScaleAnimation(0.6f, 0.1f, 0.6f, 0.1f);
        scaleAnimation.setInterpolator(new AccelerateInterpolator());
        scaleAnimation.setRepeatCount(0);// 动画重复执行的次数
        scaleAnimation.setFillAfter(true);

        AnimationSet set = new AnimationSet(false);
        set.setFillAfter(false);
        set.addAnimation(scaleAnimation);
        set.addAnimation(translateAnimationY);
        set.addAnimation(translateAnimationX);
        set.setDuration(600);// 动画的执行时间
        viewa.startAnimation(set);
        // 动画监听事件
        set.setAnimationListener(new Animation.AnimationListener() {
            // 动画的开始
            @Override
            public void onAnimationStart(Animation animation) {
                v.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            // 动画的结束
            @Override
            public void onAnimationEnd(Animation animation) {
                v.setVisibility(View.GONE);
            }
        });
    }

    /**
     * @param
     * @return void
     * @throws
     * @Description: 创建动画层
     */
    private  ViewGroup createAnimLayout(Activity activity) {
        ViewGroup rootView = (ViewGroup) activity.getWindow()
                .getDecorView();
        LinearLayout animLayout = new LinearLayout(activity);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        animLayout.setLayoutParams(lp);
        animLayout.setId(Integer.MAX_VALUE);
        animLayout.setBackgroundResource(android.R.color.transparent);
        rootView.addView(animLayout);
        return animLayout;
    }

    private  View addViewToAnimLayout(final ViewGroup parent, final View view,
                                            int[] location) {
        int x = location[0];
        int y = location[1];
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.leftMargin = x;
        lp.topMargin = y;
        view.setLayoutParams(lp);
        return view;
    }

    /**
     * 将drawable对象进行指定大小的缩放
     *
     * @param drawable
     * @param w
     * @param h
     * @return
     */
    public  Drawable zoomDrawable(Drawable drawable, int w, int h) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap oldbmp = drawableToBitmap(drawable); // drawable 转换成 bitmap
        Matrix matrix = new Matrix(); // 创建操作图片用的 Matrix 对象
        float scaleWidth = ((float) w / width); // 计算缩放比例
        float scaleHeight = ((float) h / height);
        matrix.postScale(scaleWidth, scaleHeight); // 设置缩放比例
        Bitmap newbmp = Bitmap.createBitmap(oldbmp, 0, 0, width, height,
                matrix, true); // 建立新的 bitmap ，其内容是对原 bitmap 的缩放后的图
        return new BitmapDrawable(newbmp); // 把 bitmap 转换成 drawable 并返回
    }

    /**
     * 将drawable 转换成 bitmap
     *
     * @param drawable
     * @return
     */
    public  Bitmap drawableToBitmap(Drawable drawable) {
        int width = drawable.getIntrinsicWidth(); // 取 drawable 的长宽
        int height = drawable.getIntrinsicHeight();
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                : Bitmap.Config.RGB_565; // 取 drawable 的颜色格式
        Bitmap bitmap = Bitmap.createBitmap(width, height, config); // 建立对应
        // bitmap
        Canvas canvas = new Canvas(bitmap); // 建立对应 bitmap 的画布
        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas); // 把 drawable 内容画到画布中
        return bitmap;
    }

    // dp转换为像素px
    public  int dip2Px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }


    public  Bitmap getBitmapFromView(View v) {
        Bitmap b = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.RGB_565);
        Canvas c = new Canvas(b);
        v.layout(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
        // Draw background
        Drawable bgDrawable = v.getBackground();
        if (bgDrawable != null)
            bgDrawable.draw(c);
        else
            c.drawColor(Color.WHITE);
        // Draw view to canvas
        v.draw(c);
        return b;
    }
}
