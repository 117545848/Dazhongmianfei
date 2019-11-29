package com.dazhongmianfei.dzmfreader.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.dazhongmianfei.dzmfreader.R;
import com.dazhongmianfei.dzmfreader.R2;
import com.dazhongmianfei.dzmfreader.adapter.HotWordsAdapter;
import com.dazhongmianfei.dzmfreader.adapter.OptionRecyclerViewAdapter;
import com.dazhongmianfei.dzmfreader.adapter.SearchVerticalAdapter;
import com.dazhongmianfei.dzmfreader.bean.OptionBeen;
import com.dazhongmianfei.dzmfreader.bean.OptionItem;
import com.dazhongmianfei.dzmfreader.bean.SerachItem;
import com.dazhongmianfei.dzmfreader.book.config.BookConfig;
import com.dazhongmianfei.dzmfreader.comic.activity.ComicInfoActivity;
import com.dazhongmianfei.dzmfreader.comic.config.ComicConfig;
import com.dazhongmianfei.dzmfreader.http.ReaderParams;
import com.dazhongmianfei.dzmfreader.utils.HttpUtils;
import com.dazhongmianfei.dzmfreader.utils.LanguageUtil;
import com.dazhongmianfei.dzmfreader.utils.MyToash;
import com.dazhongmianfei.dzmfreader.view.AdaptionGridViewNoMargin;
import com.dazhongmianfei.dzmfreader.view.MyContentLinearLayoutManager;
import com.dazhongmianfei.dzmfreader.view.ObservableScrollView;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 搜索首页
 * Created by scb on 2018/7/9.
 */
public class SearchActivity extends BaseButterKnifeActivity {
    @Override
    public int initContentView() {
        return R.layout.activity_search;
    }

    @BindView(R2.id.activity_search_keywords_listview)
    public XRecyclerView fragment_option_listview;
    /**
     * 搜索框
     */
    @BindView(R2.id.activity_search_keywords)
    public EditText activity_search_keywords;
    /**
     * cancel
     */
    @BindView(R2.id.activity_search_cancel)
    public TextView activity_search_cancel;
    /**
     * 热词grid
     */
    @BindView(R2.id.activity_search_hotwords_grid)
    public AdaptionGridViewNoMargin activity_search_hotwords_grid;
    /**
     * 热搜榜grid
     */
    @BindView(R2.id.activity_search_book_grid)
    public AdaptionGridViewNoMargin activity_search_book_grid;
    @BindView(R2.id.activity_search_keywords_listview_noresult)
    public LinearLayout activity_search_keywords_listview_noresult;
    @BindView(R2.id.activity_search_keywords_scrollview)
    public ObservableScrollView activity_search_keywords_scrollview;


    public String mKeyWord,mKeyWordHint;
    Gson gson = new Gson();
    int total_page, current_page = 1;
    OptionRecyclerViewAdapter optionAdapter;
    LayoutInflater layoutInflater;
    List<OptionBeen> optionBeenList;
    boolean PRODUCT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    OptionRecyclerViewAdapter.OnItemClick onItemClick = new OptionRecyclerViewAdapter.OnItemClick() {
        @Override
        public void OnItemClick(int position, OptionBeen optionBeen) {

            Intent intent = new Intent();
            if (PRODUCT) {
                intent.setClass(activity, BookInfoActivity.class);
                intent.putExtra("book_id", optionBeen.getBook_id());
            } else {
                intent.setClass(activity, ComicInfoActivity.class);
                intent.putExtra("comic_id", optionBeen.getComic_id());
            }
            startActivity(intent);

        }
    };

    public void initView() {
        optionBeenList = new ArrayList<>();
        layoutInflater=LayoutInflater.from(activity);

        MyContentLinearLayoutManager layoutManager = new MyContentLinearLayoutManager(activity);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        fragment_option_listview.setLayoutManager(layoutManager);
        fragment_option_listview.addHeaderView((LinearLayout) layoutInflater.inflate(R.layout.item_list_head, null));


        PRODUCT = getIntent().getBooleanExtra("PRODUCT", false);
        mKeyWord = getIntent().getStringExtra("mKeyWord");
        if (mKeyWord != null) {
            mKeyWordHint=mKeyWord;
            activity_search_keywords.setHint(mKeyWordHint);
        }
        fragment_option_listview.setPullRefreshEnabled(false);
        fragment_option_listview.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadMore() {
                if (mKeyWord != null) {
                    gotoSearch(mKeyWord);
                }
            }
        });


        activity_search_keywords.clearFocus();
        activity_search_keywords.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String KeyWord = activity_search_keywords.getText().toString() + "";
                    if (TextUtils.isEmpty(KeyWord)&&Pattern.matches("\\s*", KeyWord)) {
                        mKeyWord= mKeyWordHint;
                    }else
                    mKeyWord = KeyWord;
                    current_page = 1;
                    gotoSearch(mKeyWord);
                    return true;
                }
                return false;
            }
        });
        activity_search_keywords.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    fragment_option_listview.setVisibility(View.GONE);
                    activity_search_keywords_scrollview.setVisibility(View.VISIBLE);
                }

            }
        });
        initData();
    }


    public void initData() {
        ReaderParams params = new ReaderParams(activity);
        String json = params.generateParamsJson();
        String url;
        if (PRODUCT) {
            url = BookConfig.mSearchIndexUrl;
        } else {
            url = ComicConfig.COMIC_search_index;
        }
     /*   if (true) {
            MyToash.Log("mSearchIndexUrl",url+"     "+json);
            return;
        }*/
        HttpUtils.getInstance(activity).sendRequestRequestParams3(url, json, true, new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(final String result) {

                        initInfo(result);
                    }

                    @Override
                    public void onErrorResponse(String ex) {

                    }
                }

        );

    }


    public void initInfo(String json) {
        SerachItem serachItem = gson.fromJson(json, SerachItem.class);
        if (serachItem.hot_word.length != 0) {
            //activity_search_keywords.setHint(serachItem.hot_word[0]);
            HotWordsAdapter hotWordsAdapter = new HotWordsAdapter(activity, serachItem.hot_word);
            activity_search_hotwords_grid.setAdapter(hotWordsAdapter);
            activity_search_hotwords_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    mKeyWord = serachItem.hot_word[position];
                    activity_search_keywords.setText(mKeyWord);
                    current_page = 1;
                    gotoSearch(mKeyWord);
                }
            });
        }
        SearchVerticalAdapter adapter = new SearchVerticalAdapter(activity, serachItem.list);
        activity_search_book_grid.setAdapter(adapter);
        activity_search_book_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent;
                if (PRODUCT) {
                    intent = new Intent(activity, BookInfoActivity.class);
                    intent.putExtra("book_id", serachItem.list.get(position).getBook_id());
                } else {
                    intent = new Intent(activity, ComicInfoActivity.class);
                    intent.putExtra("comic_id", serachItem.list.get(position).getComic_id());
                }
                startActivity(intent);
            }
        });


    }

    @OnClick(value = {R.id.activity_search_cancel})
    public void getEvent(View view) {
        switch (view.getId()) {
            case R.id.activity_search_cancel:
                finish();
                break;

        }
    }

    /**
     * 开始搜索
     *
     * @param keyword
     */
    public void gotoSearch(String keyword) {


        ReaderParams params = new ReaderParams(this);
        params.putExtraParams("keyword", keyword);
        params.putExtraParams("page_num", current_page + "");
        String json = params.generateParamsJson();
        String url;
        if (PRODUCT) {
            url = BookConfig.mSearchUrl;
        } else {
            url = ComicConfig.COMIC_search;
        }

        HttpUtils.getInstance(activity).sendRequestRequestParams3(url, json, true, new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(final String result) {
                        initNextInfo(result);
                    }

                    @Override
                    public void onErrorResponse(String ex) {

                    }
                }

        );
    }
    int Size;
    public void initNextInfo(String result) {
        OptionItem optionItem = gson.fromJson(result, OptionItem.class);
        total_page = optionItem.total_page;

        MyToash.Log("initNextInfo",optionItem.toString());
        if (total_page!=0&&(current_page <= total_page && !optionItem.list.isEmpty())) {
            int optionItem_list_size = optionItem.list.size();
            if (current_page == 1) {
                optionBeenList.clear();
                optionBeenList.addAll(optionItem.list);
                optionAdapter = null;
                Size = optionItem_list_size;
                optionAdapter = new OptionRecyclerViewAdapter(activity, optionBeenList, 0, PRODUCT, layoutInflater, onItemClick);
                fragment_option_listview.setAdapter(optionAdapter);

                activity_search_keywords_listview_noresult.setVisibility(View.GONE);
                fragment_option_listview.setVisibility(View.VISIBLE);
                activity_search_keywords_scrollview.setVisibility(View.GONE);
            } else {
                optionBeenList.addAll(optionItem.list);
                int t = Size + optionItem_list_size;
                optionAdapter.notifyItemRangeInserted(Size+2, optionItem_list_size);
                Size = t;
            }
            current_page = optionItem.current_page;
            ++current_page;
        } else {
            if (current_page != 1) {
                MyToash.ToashError(activity, LanguageUtil.getString(activity, R.string.ReadActivity_chapterfail));
            }
        }
        if (current_page == 1) {
            if (total_page==0||optionBeenList.isEmpty()) {
                activity_search_keywords_listview_noresult.setVisibility(View.VISIBLE);
                fragment_option_listview.setVisibility(View.GONE);
            } else {
                fragment_option_listview.setVisibility(View.VISIBLE);
                activity_search_keywords_listview_noresult.setVisibility(View.GONE);
            }
        }
        activity_search_keywords_scrollview.setVisibility(View.GONE);
    }


    @Override
    public void onBackPressed() {
        // 判断时间间隔
        if (fragment_option_listview.getVisibility() == View.VISIBLE || activity_search_keywords_listview_noresult.getVisibility() == View.VISIBLE) {
            fragment_option_listview.setVisibility(View.GONE);
            activity_search_keywords_listview_noresult.setVisibility(View.GONE);
            activity_search_keywords_scrollview.setVisibility(View.VISIBLE);
        } else {
            finish();
        }

    }

}
