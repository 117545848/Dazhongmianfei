package com.dazhongmianfei.dzmfreader.book.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.dazhongmianfei.dzmfreader.R;
import com.dazhongmianfei.dzmfreader.R2;
import com.dazhongmianfei.dzmfreader.activity.BaseOptionActivity;
import com.dazhongmianfei.dzmfreader.activity.BookInfoActivity;
import com.dazhongmianfei.dzmfreader.activity.WebViewActivity;
import com.dazhongmianfei.dzmfreader.adapter.VerticalAdapter;
import com.dazhongmianfei.dzmfreader.banner.ConvenientBanner;
import com.dazhongmianfei.dzmfreader.book.been.StroreBookcLable;
import com.dazhongmianfei.dzmfreader.config.MainHttpTask;
import com.dazhongmianfei.dzmfreader.config.ReaderConfig;
import com.dazhongmianfei.dzmfreader.fragment.BaseButterKnifeFragment;
import com.dazhongmianfei.dzmfreader.http.ReaderParams;
import com.dazhongmianfei.dzmfreader.jinritoutiao.TodayOneAD;
import com.dazhongmianfei.dzmfreader.utils.DateUtils;
import com.dazhongmianfei.dzmfreader.utils.HttpUtils;
import com.dazhongmianfei.dzmfreader.utils.ImageUtil;
import com.dazhongmianfei.dzmfreader.utils.MyPicasso;
import com.dazhongmianfei.dzmfreader.utils.MyToash;
import com.dazhongmianfei.dzmfreader.utils.ScreenSizeUtils;
import com.dazhongmianfei.dzmfreader.view.AdaptionGridView;
import com.dazhongmianfei.dzmfreader.view.PullToRefreshLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;

import static com.dazhongmianfei.dzmfreader.book.config.BookConfig.book_refresh;
import static com.dazhongmianfei.dzmfreader.config.ReaderConfig.LOOKMORE;

/**
 * 发现
 */
public class DiscoveryBookFragment extends BaseButterKnifeFragment {

    @Override
    public int initContentView() {
        return R.layout.fragment_discovery;
    }


    @BindView(R2.id.fragment_discovery_container)
    public LinearLayout fragment_discovery_container;

    @BindView(R2.id.fragment_discovery_banner_male)
    public ConvenientBanner mStoreBannerMale;
    @BindView(R2.id.list_ad_view_layout)
    FrameLayout list_ad_view_layout;

    @BindView(R2.id.refreshLayoutMale)
    public PullToRefreshLayout pullToRefreshLayout;
    public int WIDTH, WIDTHH, WIDTH_MAIN_AD, WIDTHV, HEIGHT, HEIGHTV, HorizontalSpacing, H100, H50, H20, H30;
    LayoutInflater layoutInflater;
    Gson gson = new Gson();
    public boolean is_getNativeInfoListView;


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser&&list_ad_view_layout!=null&&activity!=null) {
            if (todayOneAD == null) {
                todayOneAD = new TodayOneAD(activity, 2, "");
            }
            todayOneAD. getTodayOneBanner(list_ad_view_layout, null, 2);
        }
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        pullToRefreshLayout.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout RefreshLayout) {
                MainHttpTask.getInstance().httpSend(activity, ReaderConfig.mDiscoveryUrl, "DiscoverBook", new MainHttpTask.GetHttpData() {
                    @Override
                    public void getHttpData(String result) {
                        is_getNativeInfoListView=true;
                        if (result != null) {
                            initInfo(result);
                        }

                        if (pullToRefreshLayout != null) {
                            pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                        }
                    }
                });
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {

            }
        });

        WIDTH = ScreenSizeUtils.getInstance(activity).getScreenWidth();
        WIDTHH = WIDTH;
        WIDTH_MAIN_AD = WIDTH - ImageUtil.dp2px(activity, 1);
        H30 = WIDTH / 5;
        layoutInflater = LayoutInflater.from(activity);
        H20 = ImageUtil.dp2px(activity, 20);
        WIDTH = (WIDTH - H20) / 3;//横向排版 图片宽度
        HEIGHT = (int) (((float) WIDTH * 4f / 3f));//
        HorizontalSpacing = ImageUtil.dp2px(activity, 3);//横间距
        WIDTHV = WIDTH - ImageUtil.dp2px(activity, 26);//竖向 图片宽度
        HEIGHTV = (int) (((float) WIDTHV * 4f / 3f));//
        H100 = ImageUtil.dp2px(activity, 100);
        H50 = ImageUtil.dp2px(activity, 50);
        timerTask = new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(0);
            }
        };

        MainHttpTask.getInstance().getResultString(activity, "DiscoverBook", new MainHttpTask.GetHttpData() {
            @Override
            public void getHttpData(String result) {
                initInfo(result);
            }
        });
    }

    public void initInfo(String info) {
        try {
            JSONObject jsonObject = new JSONObject(info);

            //1.初始化男频banner控件数据
            //  initBanner(jsonObject.getString("banner"));
            ConvenientBanner.initbanner(activity, gson, jsonObject.getString("banner"), mStoreBannerMale, 2000, 2);
            initWaterfall(jsonObject.getString("label"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    public  TodayOneAD todayOneAD;
    public void initWaterfall(String jsonObject) {
        fragment_discovery_container.removeAllViews();
        JsonParser jsonParser = new JsonParser();
        JsonArray jsonElements = jsonParser.parse(jsonObject).getAsJsonArray();//获取JsonArray对象
        for (JsonElement jsonElement : jsonElements) {
            StroreBookcLable stroreComicLable = gson.fromJson(jsonElement, StroreBookcLable.class);//解析
            if (stroreComicLable.ad_type != 0) {


                continue;
            }

            int Size = stroreComicLable.list.size();
            if (Size == 0) {
                continue;
            }
            // View type3 = layoutInflater.inflate(R.layout.fragment_discovery_book_layout, null, false);

            View type3 = layoutInflater.inflate(R.layout.fragment_store_book_layout, null, false);
            TextView fragment_store_gridview3_text = type3.findViewById(R.id.fragment_store_gridview3_text);
            fragment_store_gridview3_text.setText(stroreComicLable.label);
            AdaptionGridView fragment_store_gridview3_gridview_first = type3.findViewById(R.id.fragment_store_gridview3_gridview_first);
            fragment_store_gridview3_gridview_first.setHorizontalSpacing(HorizontalSpacing);
            AdaptionGridView fragment_store_gridview3_gridview_second = type3.findViewById(R.id.fragment_store_gridview3_gridview_second);
            AdaptionGridView fragment_store_gridview3_gridview_fore = type3.findViewById(R.id.fragment_store_gridview3_gridview_fore);
            View fragment_store_gridview1_view1 = type3.findViewById(R.id.fragment_store_gridview1_view1);
            View fragment_store_gridview1_view2 = type3.findViewById(R.id.fragment_store_gridview1_view2);
            View fragment_store_gridview1_view3 = type3.findViewById(R.id.fragment_store_gridview1_view3);
            int expire_time = stroreComicLable.expire_time;
            LinearLayout fragment_store_gridview1_more = type3.findViewById(R.id.fragment_store_gridview1_more);

            if (stroreComicLable.can_more) {
                fragment_store_gridview1_more.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        try {
                            Intent intent = new Intent(activity, BaseOptionActivity.class)
                                    .putExtra("OPTION", LOOKMORE)
                                    .putExtra("PRODUCT", true);
                            if (expire_time > 0) {
                                intent.putExtra("recommend_id", "-1");
                            } else {
                                intent.putExtra("recommend_id", stroreComicLable.recommend_id);
                            }
                            startActivity(intent);

                        } catch (Exception E) {
                        }
                    }
                });
            } else {
                fragment_store_gridview1_more.setVisibility(View.GONE);
            }

            LinearLayout fragment_store_gridview_huanyihuan = type3.findViewById(R.id.fragment_store_gridview_huanyihuan);
            if (stroreComicLable.can_refresh) {
                fragment_store_gridview_huanyihuan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        postHuanyihuan(stroreComicLable.recommend_id, stroreComicLable.style, fragment_store_gridview3_gridview_first, fragment_store_gridview3_gridview_second, fragment_store_gridview3_gridview_fore);
                    }
                });
            } else {
                fragment_store_gridview_huanyihuan.setVisibility(View.GONE);
            }
            RelativeLayout fragment_discovery_daojishi_end;
            if (expire_time == 0) {
            } else if (expire_time == -1) {
                fragment_discovery_daojishi_end = type3.findViewById(R.id.fragment_discovery_daojishi_end);
                fragment_discovery_daojishi_end.setVisibility(View.VISIBLE);
            } else {
                LinearLayout fragment_discovery_daojishi_layout = type3.findViewById(R.id.fragment_discovery_daojishi_layout);
                fragment_discovery_daojishi_end = type3.findViewById(R.id.fragment_discovery_daojishi_end);
                Expire_time = expire_time;
                try {
                    if (Fragment_discovery_daojishi_layout == null) {
                        timer.schedule(timerTask, 1000, 1000);
                    }
                } catch (Exception e) {
                }
                fragment_discovery_daojishi_layout.setVisibility(View.INVISIBLE);
                fragment_discovery_daojishi_end.setVisibility(View.GONE);

                Fragment_discovery_daojishi_end = fragment_discovery_daojishi_end;
                Fragment_discovery_daojishi_layout = fragment_discovery_daojishi_layout;

            }
            int ItemHeigth = Huanyihuan(stroreComicLable.style, stroreComicLable.list, fragment_store_gridview3_gridview_first, fragment_store_gridview3_gridview_second, fragment_store_gridview3_gridview_fore);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.height = ItemHeigth;

            if (!stroreComicLable.can_more && !stroreComicLable.can_refresh) {
                params.height = ItemHeigth - H50;
            } else if (!(stroreComicLable.can_more && stroreComicLable.can_refresh)) {
                buttomonlyOne(fragment_store_gridview1_view1, fragment_store_gridview1_view2, fragment_store_gridview1_view3);

            }
            fragment_discovery_container.addView(type3, params);

        }


    }

    private void buttomonlyOne(View fragment_store_gridview1_view1, View fragment_store_gridview1_view2, View fragment_store_gridview1_view3) {
        fragment_store_gridview1_view2.setVisibility(View.GONE);

        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) fragment_store_gridview1_view1.getLayoutParams();
        layoutParams.width = H30;
        fragment_store_gridview1_view1.setLayoutParams(layoutParams);
        LinearLayout.LayoutParams layoutParams3 = (LinearLayout.LayoutParams) fragment_store_gridview1_view3.getLayoutParams();
        layoutParams3.width = H30;
        fragment_store_gridview1_view1.setLayoutParams(layoutParams3);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (timerTask != null) {
            timerTask.cancel();
        }
        if (timer != null) {
            timer.cancel();
        }
    }

    TimerTask timerTask;
    public Timer timer = new Timer();
    LinearLayout Fragment_discovery_daojishi_layout;
    RelativeLayout Fragment_discovery_daojishi_end;

    TextView fragment_discovery_daojishi_hh, fragment_discovery_daojishi_mm, fragment_discovery_daojishi_ss;

    int Expire_time;

    // SimpleDateFormat aDate = new SimpleDateFormat("HH:mm:ss");


    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (Fragment_discovery_daojishi_layout != null) {
                if (--Expire_time == 0) {
                    Fragment_discovery_daojishi_end.setVisibility(View.VISIBLE);
                    Fragment_discovery_daojishi_layout.setVisibility(View.GONE);
                } else {
                    MyToash.Log("Expire_time", Expire_time);
                    String formatStr = DateUtils.secToTime(Expire_time);
                    String[] date = formatStr.split(":");
                    //  TextView fragment_discovery_daojishi_day = Fragment_discovery_daojishi_layout.findViewById(R.id.fragment_discovery_daojishi_day);

                    fragment_discovery_daojishi_hh = null;
                    fragment_discovery_daojishi_mm = null;
                    fragment_discovery_daojishi_ss = null;
                    fragment_discovery_daojishi_hh = Fragment_discovery_daojishi_layout.findViewById(R.id.fragment_discovery_daojishi_hh);
                    fragment_discovery_daojishi_mm = Fragment_discovery_daojishi_layout.findViewById(R.id.fragment_discovery_daojishi_mm);
                    fragment_discovery_daojishi_ss = Fragment_discovery_daojishi_layout.findViewById(R.id.fragment_discovery_daojishi_ss);

                    fragment_discovery_daojishi_hh.setText(date[0]);
                    fragment_discovery_daojishi_mm.setText(date[1]);
                    fragment_discovery_daojishi_ss.setText(date[2]);
                    if (Fragment_discovery_daojishi_layout.getVisibility() == View.INVISIBLE) {
                        Fragment_discovery_daojishi_layout.setVisibility(View.VISIBLE);
                    }
                }
            } else {

            }

        }
    };

    private int Huanyihuan(int style, List<StroreBookcLable.Book> bookList, AdaptionGridView fragment_store_gridview3_gridview_first, AdaptionGridView fragment_store_gridview3_gridview_second, AdaptionGridView fragment_store_gridview3_gridview_fore) {
        int size = bookList.size();
        int minSize = 0;
        int ItemHeigth = 0, raw = 0, start = 0;
        if (style == 1) {
            minSize = Math.min(size, 3);
            ItemHeigth = H100 + HEIGHT + H50;
        } else if (style == 2) {
            minSize = Math.min(size, 6);
            if (minSize > 3) {
                raw = 2;
                ItemHeigth = H100 + (HEIGHT + H50) * 2;
            } else {
                raw = 1;
                ItemHeigth = H100 + HEIGHT + H50;
            }
        } else if (style == 3) {
            minSize = Math.min(size, 3);
            ItemHeigth = H100 + HEIGHT + H50;
            if (size > 3) {
                ItemHeigth = H100 + HEIGHT + H50 + (HEIGHTV + HorizontalSpacing) * 3;
                fragment_store_gridview3_gridview_second.setVisibility(View.VISIBLE);
                final List<StroreBookcLable.Book> secondList = bookList.subList(3, Math.min(size, 6));
                VerticalAdapter horizontalAdapter = new VerticalAdapter(activity, secondList, WIDTHV, HEIGHTV, true);
                fragment_store_gridview3_gridview_second.setAdapter(horizontalAdapter);
                fragment_store_gridview3_gridview_second.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(activity, BookInfoActivity.class);
                        intent.putExtra("book_id", secondList.get(position).getBook_id());
                        activity.startActivity(intent);
                    }
                });
            }

        } else if (style == 4) {
            start = 1;
            minSize = Math.min(size, 4);
            ItemHeigth = H100 + HEIGHT + H50 + (HEIGHTV + HorizontalSpacing);
            fragment_store_gridview3_gridview_fore.setVisibility(View.VISIBLE);

            final List<StroreBookcLable.Book> secondList = bookList.subList(0, 1);
            VerticalAdapter horizontalAdapter = new VerticalAdapter(activity, secondList, WIDTHV, HEIGHTV, true);
            fragment_store_gridview3_gridview_fore.setAdapter(horizontalAdapter);

            fragment_store_gridview3_gridview_fore.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(activity, BookInfoActivity.class);
                    intent.putExtra("book_id", secondList.get(position).getBook_id());
                    activity.startActivity(intent);
                }
            });
        }

        List<StroreBookcLable.Book> firstList = bookList.subList(start, minSize);
        VerticalAdapter verticalAdapter = new VerticalAdapter(activity, firstList, WIDTH, HEIGHT, false);
        fragment_store_gridview3_gridview_first.setAdapter(verticalAdapter);

        fragment_store_gridview3_gridview_first.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(activity, BookInfoActivity.class);
                intent.putExtra("book_id", bookList.get(position).getBook_id());
                activity.startActivity(intent);
            }
        });
        return ItemHeigth;
    }

    public void postHuanyihuan(String recommend_id, int style, AdaptionGridView fragment_store_gridview3_gridview_first, AdaptionGridView fragment_store_gridview3_gridview_second, AdaptionGridView fragment_store_gridview3_gridview_fore) {

        ReaderParams params = new ReaderParams(activity);
        params.putExtraParams("recommend_id", recommend_id);
        String json = params.generateParamsJson();
        HttpUtils.getInstance(activity).sendRequestRequestParams3(book_refresh, json, false, new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(final String result) {
                        try {
                            List<StroreBookcLable.Book> bookList = gson.fromJson(new JSONObject(result).getString("list"), new TypeToken<List<StroreBookcLable.Book>>() {
                            }.getType());
                            if (!bookList.isEmpty()) {
                                int ItemHeigth = Huanyihuan(style, bookList, fragment_store_gridview3_gridview_first, fragment_store_gridview3_gridview_second, fragment_store_gridview3_gridview_fore);
                            }
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
}
