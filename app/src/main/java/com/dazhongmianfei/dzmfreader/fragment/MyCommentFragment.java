package com.dazhongmianfei.dzmfreader.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.dazhongmianfei.dzmfreader.R;
import com.dazhongmianfei.dzmfreader.R2;
import com.dazhongmianfei.dzmfreader.adapter.MyCommentRecyclerViewAdapter;
import com.dazhongmianfei.dzmfreader.bean.CommentItem;
import com.dazhongmianfei.dzmfreader.bean.MyCommentItem;
import com.dazhongmianfei.dzmfreader.bean.PayGoldDetail;
import com.dazhongmianfei.dzmfreader.http.ReaderParams;
import com.dazhongmianfei.dzmfreader.utils.HttpUtils;
import com.dazhongmianfei.dzmfreader.utils.LanguageUtil;
import com.dazhongmianfei.dzmfreader.utils.MyToash;
import com.dazhongmianfei.dzmfreader.view.MyContentLinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.dazhongmianfei.dzmfreader.book.config.BookConfig.mUserCommentsUrl;
import static com.dazhongmianfei.dzmfreader.comic.config.ComicConfig.COMIC_comment_list_MY;


public class MyCommentFragment extends BaseButterKnifeFragment {


    @Override
    public int initContentView() {
        return R.layout.fragment_option;
    }


    @BindView(R2.id.fragment_option_noresult)
    public LinearLayout fragment_option_noresult;

    /*   @BindView(R2.id.fragment_option_listview)
       public PullToRefreshListView fragment_option_listview;
       */
    @BindView(R2.id.fragment_option_listview)
    public XRecyclerView fragment_option_listview;
    @BindView(R2.id.fragment_option_noresult_text)
    public TextView fragment_option_noresult_text;



    Gson gson = new Gson();
    int total_page, current_page = 1;
    boolean LoadingListener;
    LinearLayout temphead;
    LayoutInflater layoutInflater;

    boolean PRODUCT;
    private List<CommentItem> optionBeenList;
    private MyCommentRecyclerViewAdapter optionAdapter;

    @SuppressLint("ValidFragment")
    public MyCommentFragment(boolean PRODUCT) {

        this.PRODUCT = PRODUCT;

    }

    public MyCommentFragment() {
    }


    private void initHttpUrl() {
        optionBeenList = new ArrayList<>();
        layoutInflater = LayoutInflater.from(activity);
        temphead = (LinearLayout) layoutInflater.inflate(R.layout.item_list_head, null);
        fragment_option_noresult_text.setText(LanguageUtil.getString(activity,R.string.CommentListActivity_nopinglun));
        MyContentLinearLayoutManager layoutManager = new MyContentLinearLayoutManager(activity);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        fragment_option_listview.setLayoutManager(layoutManager);

        fragment_option_listview.addHeaderView(temphead);

        HttpData();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fragment_option_listview.setPullRefreshEnabled(false);
        fragment_option_listview.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadMore() {
                LoadingListener = true;
                HttpData();
            }
        });
        initHttpUrl();
    }

    int Size;

    private void initInfo(String result) {
        try {
            MyCommentItem optionItem = gson.fromJson(result, MyCommentItem.class);
            total_page = optionItem.total_page;
            MyToash.Log("optionBeenList22", current_page + "   " + optionBeenList.size() + "");
            int optionItem_list_size = optionItem.list.size();
            if (current_page <= total_page && optionItem_list_size != 0) {
                MyToash.Log("optionBeenList33", current_page + "   " + optionBeenList.size() + "");

                if (current_page == 1) {
                    optionBeenList.clear();
                    optionBeenList.addAll(optionItem.list);
                    optionAdapter = null;
                    Size = optionItem_list_size;
                    optionAdapter = new MyCommentRecyclerViewAdapter(activity, optionBeenList, layoutInflater, null);
                    fragment_option_listview.setAdapter(optionAdapter);

                } else {
                    MyToash.Log("optionBeenList44", current_page + "   " + optionBeenList.size() + "");
                    optionBeenList.addAll(optionItem.list);
                    int t = Size + optionItem_list_size;
                    optionAdapter.notifyItemRangeInserted(Size + 2, optionItem_list_size);
                    Size = t;

                    // optionAdapter.notifyDataSetChanged();
                }
                MyToash.Log("optionBeenList55", current_page + "   " + optionBeenList.size() + "");
                current_page = optionItem.current_page;
                ++current_page;

            } else {
                if (optionBeenList.isEmpty()) {
                    fragment_option_noresult.setVisibility(View.VISIBLE);
                }
                MyToash.ToashError(activity, LanguageUtil.getString(activity, R.string.ReadActivity_chapterfail));
            }
        } catch (Exception d) {
            if (optionBeenList.isEmpty()) {
                fragment_option_noresult.setVisibility(View.VISIBLE);
            }
            MyToash.ToashError(activity, LanguageUtil.getString(activity, R.string.ReadActivity_chapterfail));
        }

    }


    private void HttpData() {

        String URL;
        if (PRODUCT) {
            URL = mUserCommentsUrl;
        } else {
            URL = COMIC_comment_list_MY;
        }
        ReaderParams params = new ReaderParams(activity);

        params.putExtraParams("page_num", current_page + "");
        String json = params.generateParamsJson();
        HttpUtils.getInstance(activity).sendRequestRequestParams3(URL, json, true, new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(final String result) {
                        initInfo(result);
                        if (LoadingListener) {
                            LoadingListener = false;
                            fragment_option_listview.loadMoreComplete();
                        }
                    }

                    @Override
                    public void onErrorResponse(String ex) {
                        if (LoadingListener) {
                            LoadingListener = false;
                            fragment_option_listview.loadMoreComplete();
                        }
                    }
                }

        );
    }


    class OptionItem {
        public int total_page;//": 3,
        public int current_page;//": 2,
        public int page_size;//": 2,
        public int total_count;//,
        public List<PayGoldDetail> list;

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
