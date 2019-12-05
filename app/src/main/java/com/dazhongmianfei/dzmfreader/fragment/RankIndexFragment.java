package com.dazhongmianfei.dzmfreader.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.dazhongmianfei.dzmfreader.R;
import com.dazhongmianfei.dzmfreader.R2;
import com.dazhongmianfei.dzmfreader.activity.BaseOptionActivity;
import com.dazhongmianfei.dzmfreader.adapter.RankAdapter;
import com.dazhongmianfei.dzmfreader.bean.RankItem;
import com.dazhongmianfei.dzmfreader.http.ReaderParams;
import com.dazhongmianfei.dzmfreader.utils.HttpUtils;

import java.util.List;

import butterknife.BindView;

import static com.dazhongmianfei.dzmfreader.book.config.BookConfig.mRankUrl;

import static com.dazhongmianfei.dzmfreader.config.ReaderConfig.PAIHANG;

/**
 * Created by abc on 2016/11/4.
 */

public class RankIndexFragment extends BaseButterKnifeFragment {
    @Override
    public int initContentView() {
        return R.layout.fragment_rank_index;
    }

    @BindView(R2.id.fragment_option_noresult)
    public LinearLayout fragment_option_noresult;

    @BindView(R2.id.fragment_rankindex_listview)
    public ListView fragment_rankindex_listview;

    boolean PRODUCT;
    int SEX, OPTION;
    String httpUrl;
    Gson gson = new Gson();
    RankAdapter rankAdapter;
    List<RankItem> mRankItemList;
    LinearLayout temphead;
    LayoutInflater layoutInflater;


    @SuppressLint("ValidFragment")
    public RankIndexFragment(boolean PRODUCT, int OPTION, int SEX) {
        this.PRODUCT = PRODUCT;
        this.SEX = SEX;
        this.OPTION = OPTION;
    }


    public RankIndexFragment() {
    }


    private void initHttpUrl() {
        layoutInflater = LayoutInflater.from(activity);
        temphead = (LinearLayout) layoutInflater.inflate(R.layout.item_list_head, null);
        httpUrl = mRankUrl;
        fragment_rankindex_listview.addHeaderView(temphead);
        fragment_rankindex_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(activity, BaseOptionActivity.class);
                intent.putExtra("rank_type", mRankItemList.get(position - 1).getRank_type());
                intent.putExtra("SEX", SEX);
                intent.putExtra("OPTION", PAIHANG);
                intent.putExtra("PRODUCT", PRODUCT);
                intent.putExtra("title", mRankItemList.get(position - 1).getList_name());
                startActivity(intent);
            }
        });
        HttpData();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initHttpUrl();
    }


    private void initInfo(String result) {
        mRankItemList = gson.fromJson(result, new TypeToken<List<RankItem>>() {
        }.getType());
        if (mRankItemList == null || mRankItemList.isEmpty()) {
            fragment_option_noresult.setVisibility(View.VISIBLE);
        } else {
            rankAdapter = new RankAdapter(activity, mRankItemList, mRankItemList.size());
            fragment_rankindex_listview.setAdapter(rankAdapter);
        }
    }

    private void HttpData() {
        if (httpUrl == null) {
            return;
        }
        ReaderParams params = new ReaderParams(activity);
        params.putExtraParams("channel_id", SEX + "");
        String json = params.generateParamsJson();
        HttpUtils.getInstance(activity).sendRequestRequestParams3(httpUrl, json, true, new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(final String result) {
                        try {
                            initInfo(result);
                        } catch (Exception e) {
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
