package com.dazhongmianfei.dzmfreader.book.fragment;

import android.content.Intent;
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
import com.dazhongmianfei.dzmfreader.activity.BookInfoActivity;
import com.dazhongmianfei.dzmfreader.book.adapter.ReadHistoryRecyclerViewAdapter;
import com.dazhongmianfei.dzmfreader.book.been.ReadHistory;
import com.dazhongmianfei.dzmfreader.book.been.BaseBook;
import com.dazhongmianfei.dzmfreader.book.config.BookConfig;
import com.dazhongmianfei.dzmfreader.dialog.GetDialog;
import com.dazhongmianfei.dzmfreader.eventbus.RefreshReadHistory;
import com.dazhongmianfei.dzmfreader.fragment.BaseButterKnifeFragment;
import com.dazhongmianfei.dzmfreader.http.ReaderParams;
import com.dazhongmianfei.dzmfreader.read.manager.ChapterManager;
import com.dazhongmianfei.dzmfreader.utils.HttpUtils;
import com.dazhongmianfei.dzmfreader.utils.LanguageUtil;
import com.dazhongmianfei.dzmfreader.utils.MyToash;
import com.dazhongmianfei.dzmfreader.utils.Utils;
import com.dazhongmianfei.dzmfreader.view.MyContentLinearLayoutManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by scb on 2018/12/21.
 */


public class ReadHistoryBookFragment extends BaseButterKnifeFragment {
    @Override
    public int initContentView() {
        return R.layout.fragment_readhistory;
    }

    @BindView(R2.id.fragment_readhistory_readhistory)
    public XRecyclerView fragment_option_listview;

    @BindView(R2.id.fragment_readhistory_pop)
    public LinearLayout fragment_readhistory_pop;


    int current_page = 1;
   //// @BindView(R2.id.fragment_bookshelf_go_shelf)
   // public Button fragment_bookshelf_go_shelf;
    @BindView(R2.id.fragment_bookshelf_text)
    public TextView fragment_bookshelf_text;
    Gson gson = new Gson();
    ReadHistoryRecyclerViewAdapter optionAdapter;
    List<ReadHistory.ReadHistoryBook> optionBeenList = new ArrayList<>();
    LinearLayout temphead;
    LayoutInflater layoutInflater;
    int LoadingListener = 0;

    public interface GetPosition {
        void getPosition(int falg, ReadHistory.ReadHistoryBook readHistoryBook, int position);
    }

    GetPosition getPosition = new GetPosition() {
        @Override
        public void getPosition(int falg, ReadHistory.ReadHistoryBook readHistoryBook, int position) {
            Intent intent;
            switch (falg) {
                case 1:
                    BaseBook basebooks = LitePal.where("book_id = ?", readHistoryBook.getBook_id()).findFirst(BaseBook.class);
                    if (basebooks != null) {
                        basebooks.setCurrent_chapter_id(readHistoryBook.chapter_id);
                        ChapterManager.getInstance(activity).openBook(basebooks, readHistoryBook.getBook_id(), readHistoryBook.chapter_id);
                    } else {
                        BaseBook baseBook;
                        baseBook = new BaseBook();
                        baseBook.setBook_id(readHistoryBook.getBook_id());
                        baseBook.setName(readHistoryBook.getName());
                        baseBook.setCover(readHistoryBook.getCover());
                        baseBook.setCurrent_chapter_id(readHistoryBook.chapter_id);
                        baseBook.setDescription(readHistoryBook.getDescription());
                        baseBook.setAddBookSelf(0);
                        baseBook.setCurrent_chapter_id(readHistoryBook.chapter_id);
                        baseBook.saveIsexist(0);
                        ChapterManager.getInstance(activity).openBook(baseBook, readHistoryBook.getBook_id(), readHistoryBook.chapter_id);

                    }
                    break;
                case 0:


                    intent = new Intent(activity, BookInfoActivity.class);
                    intent.putExtra("book_id", readHistoryBook.getBook_id());
                    activity.startActivity(intent);
                    break;
                case 2:
                    GetDialog.IsOperation(activity, LanguageUtil.getString(activity, R.string.ReadHistoryFragment_qurenshanchu), "", new GetDialog.IsOperationInterface() {
                        @Override
                        public void isOperation() {

                            if (readHistoryBook.ad_type == 0) {
                                delad(readHistoryBook.log_id);
                            }
                            optionBeenList.remove(position);
                            optionAdapter.notifyDataSetChanged();


                        }
                    });

                    break;
            }

        }

    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EventBus.getDefault().register(this);
        layoutInflater = LayoutInflater.from(activity);
        temphead = (LinearLayout) layoutInflater.inflate(R.layout.item_list_head, null);
        MyContentLinearLayoutManager layoutManager = new MyContentLinearLayoutManager(activity);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        fragment_option_listview.setLayoutManager(layoutManager);
        fragment_option_listview.addHeaderView(temphead);
        fragment_bookshelf_text.setText(LanguageUtil.getString(activity,R.string.AboutActivity_noyuedujilu));
        /*fragment_bookshelf_go_shelf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.isLogin(activity)) {
                    EventBus.getDefault().post(new ToStore(1));
                    activity.finish();
                } else {
                    activity.startActivityForResult(new Intent(activity, LoginActivity.class), RefarchrequestCodee);
                }

            }
        });*/
        fragment_option_listview.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {

                LoadingListener = -1;
                current_page = 1;
                initData();
            }

            @Override
            public void onLoadMore() {
                // load more data here
                LoadingListener = 1;
                initData();
            }
        });

        initdata();
    }
    int RefarchrequestCode=890;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RefarchrequestCode) {
            current_page = 1;
            initdata();
        }

    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refresh(RefreshReadHistory refreshMine) {
        if(refreshMine.isPRODUCT()) {
            current_page=1;
            initdata();
        }
    }

    public void initdata() {
        if (Utils.isLogin(activity)) {
            fragment_option_listview.setVisibility(View.VISIBLE);
            fragment_readhistory_pop.setVisibility(View.GONE);
            initData();
        } else {
            fragment_option_listview.setVisibility(View.GONE);
            fragment_readhistory_pop.setVisibility(View.VISIBLE);
            //fragment_bookshelf_text.setText(LanguageUtil.getString(activity, R.string.ReadHistoryFragment_loginlook));
           // fragment_bookshelf_go_shelf.setText(LanguageUtil.getString(activity, R.string.ReadHistoryFragment_gologin));
        }
    }


    public void initData() {

        ReaderParams params = new ReaderParams(activity);
        params.putExtraParams("page_num", current_page + "");
        String json = params.generateParamsJson();
        HttpUtils.getInstance(activity).sendRequestRequestParams3(BookConfig.read_log, json, true, new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(String result) {
                        handData(result);
                        try {
                            if (LoadingListener == -1) {
                                fragment_option_listview.refreshComplete();
                            } else if (LoadingListener == 1) {
                                fragment_option_listview.loadMoreComplete();
                            }
                        } catch (Exception e) {
                        }
                    }

                    @Override
                    public void onErrorResponse(String ex) {

                        try {
                            if (LoadingListener == -1) {
                                fragment_option_listview.refreshComplete();
                            } else if (LoadingListener == 1) {
                                fragment_option_listview.loadMoreComplete();
                            }
                        } catch (Exception e) {
                        }
                    }
                }

        );

    }

    int Size;

    public void handData(String result) {
        try {
            final ReadHistory optionItem = gson.fromJson(result, ReadHistory.class);
            int total_page = optionItem.total_page;
            int optionItem_list_size = optionItem.list.size();
            if (current_page <= total_page && optionItem_list_size != 0) {
                if (current_page == 1) {
                    optionBeenList.clear();
                    optionBeenList.addAll(optionItem.list);
                    optionAdapter = null;
                    Size = optionItem_list_size;
                    optionAdapter = new ReadHistoryRecyclerViewAdapter(activity, optionBeenList, getPosition);
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
                    fragment_option_listview.setVisibility(View.GONE);
                    fragment_readhistory_pop.setVisibility(View.VISIBLE);
                   // fragment_bookshelf_text.setText(LanguageUtil.getString(activity, R.string.ReadHistoryFragment_noread));
                    //fragment_bookshelf_go_shelf.setText(LanguageUtil.getString(activity, R.string.noverfragment_gostore));
                } else {
                    fragment_option_listview.setVisibility(View.VISIBLE);
                    fragment_readhistory_pop.setVisibility(View.GONE);
                }
                MyToash.ToashError(activity, LanguageUtil.getString(activity, R.string.ReadActivity_chapterfail));
            }

        } catch (Exception e) {
        }
    }

    public void delad(String log_id) {
        ReaderParams params = new ReaderParams(activity);

        params.putExtraParams("log_id", log_id);
        String json = params.generateParamsJson();
        HttpUtils.getInstance(activity).sendRequestRequestParams3(BookConfig.del_read_log, json, true, new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(String result) {

                    }

                    @Override
                    public void onErrorResponse(String ex) {

                    }
                }

        );


    }
}
