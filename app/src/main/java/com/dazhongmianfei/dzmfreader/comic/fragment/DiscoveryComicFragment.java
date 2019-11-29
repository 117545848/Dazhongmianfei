package com.dazhongmianfei.dzmfreader.comic.fragment;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.dazhongmianfei.dzmfreader.R;
import com.dazhongmianfei.dzmfreader.R2;
import com.dazhongmianfei.dzmfreader.banner.ConvenientBanner;
import com.dazhongmianfei.dzmfreader.bean.BannerItemStore;
import com.dazhongmianfei.dzmfreader.comic.activity.ComicInfoActivity;
import com.dazhongmianfei.dzmfreader.comic.adapter.ComicDiscoveryAdapter;
import com.dazhongmianfei.dzmfreader.comic.been.DiscoveryComic;
import com.dazhongmianfei.dzmfreader.comic.config.ComicConfig;

import com.dazhongmianfei.dzmfreader.config.MainHttpTask;
import com.dazhongmianfei.dzmfreader.fragment.BaseButterKnifeFragment;
import com.dazhongmianfei.dzmfreader.http.ReaderParams;
import com.dazhongmianfei.dzmfreader.utils.HttpUtils;
import com.dazhongmianfei.dzmfreader.utils.ImageUtil;
import com.dazhongmianfei.dzmfreader.utils.LanguageUtil;
import com.dazhongmianfei.dzmfreader.utils.MyToash;
import com.dazhongmianfei.dzmfreader.utils.ScreenSizeUtils;
import com.dazhongmianfei.dzmfreader.view.NestedListView;
import com.dazhongmianfei.dzmfreader.view.ObservableScrollView;
import com.dazhongmianfei.dzmfreader.view.PullToRefreshLayout;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


/**
 * Created by scb on 2018/6/9.
 */
public class DiscoveryComicFragment extends BaseButterKnifeFragment {
    @Override
    public int initContentView() {
        return R.layout.fragment_discovery_comic;
    }

    public boolean postAsyncHttpEngine_ing;//正在刷新数据
    public int WIDTH, HEIGHT, STEXT;


/*    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refresh(RefreshDiscoveryFragment refreshMine) {
        postAsyncHttpEngine(null);
    }*/

    public DiscoveryComicFragment() {

    }

    Gson gson = new Gson();
    @BindView(R2.id.discover_banner_female)
    public ConvenientBanner<BannerItemStore> mStoreBannerMale;

    @BindView(R2.id.discover_entrance_grid_female)
    public NestedListView listView;


    @BindView(R2.id.discover_refreshLayoutMale)
    PullToRefreshLayout malePullLayout;

    @BindView(R2.id.discover_scrollViewMale)
    ObservableScrollView discover_scrollViewMale;
    int total_page, current_page = 1;
    ComicDiscoveryAdapter comicDiscoveryAdapter;
    List<DiscoveryComic> discoveryComicList;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // EventBus.getDefault().register(this);
        initViews();
        MainHttpTask.getInstance().getResultString(activity, "DiscoverComic", new MainHttpTask.GetHttpData() {
            @Override
            public void getHttpData(String result) {
                initInfo(result);
            }
        });
    }

    public void initViews() {
        discover_scrollViewMale.setLoadingMoreEnabled(true);
        discoveryComicList = new ArrayList<>();
        WIDTH = ScreenSizeUtils.getInstance(activity).getScreenWidth() - ImageUtil.dp2px(activity, 30);
        STEXT = ImageUtil.dp2px(activity, 40);

        HEIGHT = WIDTH * 4 / 7;

        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mStoreBannerMale.getLayoutParams();
        layoutParams.height = WIDTH / 3 - ImageUtil.dp2px(activity, 2);
        mStoreBannerMale.setLayoutParams(layoutParams);


        malePullLayout.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                current_page = 1;
                MainHttpTask.getInstance().httpSend(activity, ComicConfig.COMIC_featured, "DiscoverComic", new MainHttpTask.GetHttpData() {
                    @Override
                    public void getHttpData(String result) {
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
                postAsyncHttpEngine(pullToRefreshLayout);
            }
        });

        //男频下拉监听,改变channelbar的整体透明度
        malePullLayout.setOnPullListener(new PullToRefreshLayout.OnPullListener() {
            @Override
            public void onPulling(float y) {
/*                float ratio = Math.min(Math.max(y, 0), REFRESH_HEIGHT) / REFRESH_HEIGHT;
                fragment_newbookself_top.setAlpha(1 - ratio);*/
            }

        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(activity, ComicInfoActivity.class);
                intent.putExtra("comic_id", discoveryComicList.get(position).comic_id);
                activity.startActivity(intent);
            }
        });
      /*  mScrollViewMale.setScrollViewListener(new ObservableScrollView.ScrollViewListener() {
            @Override
            public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {
                EventBus.getDefault().post(new DiscoveryEventbus(true, y));
            }
        });*/


    }

    public void postAsyncHttpEngine(final PullToRefreshLayout pullToRefreshLayout) {
        if (postAsyncHttpEngine_ing) {
            return;
        }
        postAsyncHttpEngine_ing = true;
        ReaderParams params = new ReaderParams(activity);

        params.putExtraParams("page_num", current_page + "");
        String json = params.generateParamsJson();
        HttpUtils.getInstance(activity).sendRequestRequestParams3(ComicConfig.COMIC_featured, json, false, new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(final String result) {
                        initInfo(result);


                        if (pullToRefreshLayout != null) {
                            pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                        }
                    }

                    @Override
                    public void onErrorResponse(String ex) {
                        postAsyncHttpEngine_ing = false;
                        if (pullToRefreshLayout != null) {
                            pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                        }
                    }
                }

        );


    }


    /**
     * 初始化男频数据
     */
    public void initInfo(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);

            //1.初始化男频banner控件数据
            if (current_page == 1) {
                ConvenientBanner.initbanner(activity, gson, jsonObject.getString("banner"), mStoreBannerMale, 2000, 3);
            }
            initWaterfall(jsonObject.getString("item_list"));


            postAsyncHttpEngine_ing = false;
        } catch (Exception e) {
            e.printStackTrace();

            Log.i("nan_result", "JSONException");
        }
    }

    public void initWaterfall(String jsonObject) {
        DiscoveryItem discoveryItem = gson.fromJson(jsonObject, DiscoveryItem.class);



        MyToash.Log("initWaterfall", discoveryItem.toString());
        total_page = discoveryItem.total_page;
        if (current_page <= total_page && !discoveryItem.list.isEmpty()) {

            if (current_page == 1) {
                discoveryComicList.clear();
            }
            discoveryComicList.addAll(discoveryItem.list);
            comicDiscoveryAdapter = new ComicDiscoveryAdapter(activity, discoveryComicList, WIDTH, HEIGHT);
            listView.setAdapter(comicDiscoveryAdapter);
            current_page = discoveryItem.current_page;
            ++current_page;

        } else {
            MyToash.ToashError(activity, LanguageUtil.getString(activity, R.string.ReadActivity_chapterfail));
        }


    }

    class DiscoveryItem {
        public int total_page;//": 3,
        public int current_page;//": 2,
        public int page_size;//": 2,
        public int total_count;//,
        public List<DiscoveryComic> list;

        @Override
        public String toString() {
            return "DiscoveryItem{" +
                    "total_page=" + total_page +
                    ", current_page=" + current_page +
                    ", page_size=" + page_size +
                    ", total_count=" + total_count +
                    ", list=" + list +
                    '}';
        }
    }
}
