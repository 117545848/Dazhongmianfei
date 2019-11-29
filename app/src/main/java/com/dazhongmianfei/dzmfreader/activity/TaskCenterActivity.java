package com.dazhongmianfei.dzmfreader.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.dazhongmianfei.dzmfreader.R;
import com.dazhongmianfei.dzmfreader.R2;
import com.dazhongmianfei.dzmfreader.adapter.TaskCenterAdapter;
import com.dazhongmianfei.dzmfreader.bean.TaskCenter;
import com.dazhongmianfei.dzmfreader.bean.UserInfoItem;
import com.dazhongmianfei.dzmfreader.config.ReaderApplication;
import com.dazhongmianfei.dzmfreader.config.ReaderConfig;
import com.dazhongmianfei.dzmfreader.dialog.MyPoPwindow;
import com.dazhongmianfei.dzmfreader.eventbus.RefreshMine;
import com.dazhongmianfei.dzmfreader.eventbus.ToStore;
import com.dazhongmianfei.dzmfreader.http.ReaderParams;
import com.dazhongmianfei.dzmfreader.utils.HttpUtils;
import com.dazhongmianfei.dzmfreader.utils.LanguageUtil;
import com.dazhongmianfei.dzmfreader.utils.MyShare;
import com.dazhongmianfei.dzmfreader.utils.MyToash;
import com.dazhongmianfei.dzmfreader.utils.ShareUitls;
import com.dazhongmianfei.dzmfreader.utils.Utils;
import com.dazhongmianfei.dzmfreader.view.CircleImageView;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;
//.http.RequestParams;
//.view.annotation.ContentView;
//.view.annotation.Event;
//.view.annotation.ViewInject;
//.x;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by abc on 2016/11/4.
 */

public class TaskCenterActivity extends BaseButterKnifeActivity {
    @Override
    public int initContentView() {
        return R.layout.activity_taskcenter;
    }

    @BindView(R2.id.titlebar_back)
    public LinearLayout titlebar_back;
    @BindView(R2.id.titlebar_text)
    public TextView titlebar_text;
    @BindView(R2.id.titlebar_right)
    public RelativeLayout titlebar_right;

    @BindView(R2.id.titlebar_right_img)
    public RelativeLayout titlebar_right_img;


    @BindView(R2.id.activity_taskcenter_listview)
    public ListView activity_taskcenter_listview;
    TaskCenter.Sign_info sign_info;
    public Holder holder;
    List<TaskCenter.TaskCenter2.Taskcenter> task_list = new ArrayList();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        titlebar_text.setText(LanguageUtil.getString(activity, R.string.TaskCenterActivity_titl));
        titlebar_right.setVisibility(View.VISIBLE);
        titlebar_right_img.setBackgroundResource(R.mipmap.task_guide);
        View view = LayoutInflater.from(this).inflate(R.layout.listview_item_taskcenter_head_new, null);
        holder = new Holder(view);
        activity_taskcenter_listview.addHeaderView(view, null, false);
        activity_taskcenter_listview.setHeaderDividersEnabled(true);

        activity_taskcenter_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent();
                TaskCenter.TaskCenter2.Taskcenter taskCenter2 = task_list.get(i - 1);

                MyToash.Log("TaskCenter2", taskCenter2.toString());
                if (taskCenter2.getTask_state() != 1) {

                    switch (taskCenter2.getTask_action()) {
                        case "finish_info":
                            intent.setClass(activity, UserInfoActivity.class);
                            startActivity(intent);
                            finish();
                            break;
                        case "add_book":
                        case "read_book":
                        case "comment_book":
                            EventBus.getDefault().post(new ToStore());
                            finish();
                            break;
                        case "recharge":
                            intent.setClass(activity, RechargeActivity.class);
                            startActivity(intent);
                            finish();
                            break;
                        case "vip":
                            intent.setClass(activity, AcquireBaoyueActivity.class);
                            startActivity(intent);
                            finish();
                            break;
                        case "share_app":
                            MyShare.ShareAPP(activity);
                            break;

                    }
                }
            }
        });
        getData();

    }

    @OnClick(value = {R.id.titlebar_back, R.id.titlebar_right})
    public void getEvent(View view) {
        switch (view.getId()) {
            case R.id.titlebar_back:
                finish();
                break;
            case R.id.titlebar_right:
                startActivity(new Intent(activity, TaskExplainActivity.class).
                        putExtra("sign_rules", sign_info.sign_rules));
                break;



        }
    }


    public class Holder {
        @BindView(R2.id.activity_taskcenter_lianxuday)
        public TextView activity_taskcenter_lianxuday;
        @BindView(R2.id.activity_taskcenter_sign)
        public ImageView activity_taskcenter_sign;
        @BindView(R2.id.activity_taskcenter_getshuquan)
        public TextView activity_taskcenter_getshuquan;

        public Holder(View view) {
            ButterKnife.bind(this, view);
        }

        @OnClick(value = {R.id.activity_taskcenter_sign})
        public void getEvent(View view) {
            switch (view.getId()) {
                case R.id.activity_taskcenter_sign:
                    signHttp(activity);
                    break;
            }
        }
    }

    public void signHttp(final Activity activity) {
        MyToash.Log("sign_info", (sign_info != null) + " sss ");
        if (sign_info != null && sign_info.sign_status != 1) {
            final ReaderParams params = new ReaderParams(activity);
            String json = params.generateParamsJson();
            HttpUtils.getInstance(activity).sendRequestRequestParams3(ReaderConfig.sIgninhttp, json, true, new HttpUtils.ResponseListener() {
                        @Override
                        public void onResponse(final String result) {
                            sign_info.sign_status = 1;
                            holder.activity_taskcenter_sign.setImageResource(R.mipmap.icon_sign);
                            ShareUitls.putString(activity, "sign_pop", result);
                            new MyPoPwindow().getSignPop(activity);
                            EventBus.getDefault().post(new RefreshMine(null));
                        }

                        @Override
                        public void onErrorResponse(String ex) {

                        }
                    }

            );
        }

    }

    public void getData() {
        final ReaderParams params = new ReaderParams(this);
        String json = params.generateParamsJson();
        HttpUtils.getInstance(this).sendRequestRequestParams3(ReaderConfig.taskcenter, json, true, new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(final String result) {
                        Log.i("taskcenter", result);
                        TaskCenter taskCenter = new Gson().fromJson(result, TaskCenter.class);
                        setData(taskCenter);


                    }

                    @Override
                    public void onErrorResponse(String ex) {

                    }
                }

        );

    }

    TaskCenter taskCenter;

    public void setData(TaskCenter taskCenter) {
        this.taskCenter = taskCenter;
        if (taskCenter != null) {
            sign_info = taskCenter.sign_info;
            if (sign_info.sign_status == 1) {
                holder.activity_taskcenter_sign.setImageResource(R.mipmap.icon_sign);
            } else {
                holder.activity_taskcenter_sign.setImageResource(R.mipmap.icon_unsign);
            }
            holder.activity_taskcenter_lianxuday.setText(sign_info.sign_days + "");
            holder.activity_taskcenter_getshuquan.setText(sign_info.max_award + "" + sign_info.unit);


            task_list.addAll(taskCenter.getTask_menu().get(0).getTask_list());
            task_list.addAll(taskCenter.getTask_menu().get(1).getTask_list());
            TaskCenterAdapter taskCenterAdapter = new TaskCenterAdapter(task_list, this, taskCenter.getTask_menu().get(0).getTask_list().size(), taskCenter.getTask_menu().get(0).getTask_title(), taskCenter.getTask_menu().get(1).getTask_title());
            activity_taskcenter_listview.setAdapter(taskCenterAdapter);

        }
    }

    public static void SignHttp(final Activity activity) {
        if (Utils.isLogin(activity)) {
            final ReaderParams params = new ReaderParams(activity);
            String json = params.generateParamsJson();
            HttpUtils.getInstance(activity).sendRequestRequestParams3(ReaderConfig.sIgninhttp, json, true, new HttpUtils.ResponseListener() {
                        @Override
                        public void onResponse(final String result) {
                            ShareUitls.putString(activity, "sign_pop", result);
                        }

                        @Override
                        public void onErrorResponse(String ex) {

                        }
                    }

            );
        }
    }
}
