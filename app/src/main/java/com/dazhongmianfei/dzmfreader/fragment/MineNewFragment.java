package com.dazhongmianfei.dzmfreader.fragment;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.dazhongmianfei.dzmfreader.R;
import com.dazhongmianfei.dzmfreader.R2;
import com.dazhongmianfei.dzmfreader.activity.AboutUsActivity;
import com.dazhongmianfei.dzmfreader.activity.AcquireBaoyueActivity;
import com.dazhongmianfei.dzmfreader.activity.BaseOptionActivity;
import com.dazhongmianfei.dzmfreader.activity.FeedBackActivity;
import com.dazhongmianfei.dzmfreader.activity.LoginActivity;
import com.dazhongmianfei.dzmfreader.activity.RechargeActivity;
import com.dazhongmianfei.dzmfreader.activity.SettingsActivity;
import com.dazhongmianfei.dzmfreader.activity.TaskCenterActivity;
import com.dazhongmianfei.dzmfreader.activity.UserInfoActivity;
import com.dazhongmianfei.dzmfreader.bean.UserInfoItem;
import com.dazhongmianfei.dzmfreader.config.MainHttpTask;
import com.dazhongmianfei.dzmfreader.config.ReaderConfig;
import com.dazhongmianfei.dzmfreader.eventbus.RefreshMine;
import com.dazhongmianfei.dzmfreader.http.ReaderParams;
import com.dazhongmianfei.dzmfreader.utils.AppPrefs;
import com.dazhongmianfei.dzmfreader.utils.HttpUtils;
import com.dazhongmianfei.dzmfreader.utils.ImageUtil;
import com.dazhongmianfei.dzmfreader.utils.LanguageUtil;
import com.dazhongmianfei.dzmfreader.utils.MyPicasso;
import com.dazhongmianfei.dzmfreader.utils.MyShare;
import com.dazhongmianfei.dzmfreader.utils.MyToash;
import com.dazhongmianfei.dzmfreader.utils.Utils;
import com.dazhongmianfei.dzmfreader.view.CircleImageView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;

import static com.dazhongmianfei.dzmfreader.config.ReaderConfig.DOWN;
import static com.dazhongmianfei.dzmfreader.config.ReaderConfig.LIUSHUIJIELU;
import static com.dazhongmianfei.dzmfreader.config.ReaderConfig.MYCOMMENT;
import static com.dazhongmianfei.dzmfreader.config.ReaderConfig.READHISTORY;
import static com.dazhongmianfei.dzmfreader.config.ReaderConfig.USE_AD_FINAL;
import static com.dazhongmianfei.dzmfreader.config.ReaderConfig.getCurrencyUnit;
import static com.dazhongmianfei.dzmfreader.config.ReaderConfig.getSubUnit;
import static com.dazhongmianfei.dzmfreader.read.util.PageFactory.IS_VIP;
import static com.dazhongmianfei.dzmfreader.read.util.PageFactory.close_AD;

/**
 * 我的
 */

public class MineNewFragment extends BaseButterKnifeFragment {
    @Override
    public int initContentView() {
        return R.layout.fragment_mine_new;
    }

    public String TAG = MineNewFragment.class.getSimpleName();
    public UserInfoItem mUserInfo;

    @BindView(R2.id.fragment_mine_user_info_avatar)
    public CircleImageView fragment_mine_user_info_avatar;
    @BindView(R2.id.fragment_mine_user_info_sex)
    public ImageView fragment_mine_user_info_sex;

    @BindView(R2.id.fragment_mine_user_info_nickname)
    public TextView fragment_mine_user_info_nickname;
    @BindView(R2.id.fragment_mine_user_info_id)
    public TextView fragment_mine_user_info_id;
    @BindView(R2.id.fragment_mine_user_info_nologin)
    public LinearLayout fragment_mine_user_info_nologin;


    @BindView(R2.id.fragment_mine_user_info_gold_unit)
    public TextView fragment_mine_user_info_gold_unit;
    @BindView(R2.id.fragment_mine_user_info_shuquan_unit)
    public TextView fragment_mine_user_info_shuquan_unit;
    @BindView(R2.id.fragment_mine_user_info_recharge_song)
    public TextView fragment_mine_user_info_recharge_song;
    @BindView(R2.id.fragment_mine_user_info_friends_song)
    public TextView fragment_mine_user_info_friends_song;
    @BindView(R2.id.fragment_mine_user_info_recharge_text)
    public TextView fragment_mine_user_info_recharge_text;



/*   @BindView(R2.id.//fragment_mine_user_info_sign)
    public ImageView //fragment_mine_user_info_sign;*/

    @BindView(R2.id.fragment_mine_user_info_money_layout)
    public LinearLayout fragment_mine_user_info_money_layout;
    @BindView(R2.id.fragment_mine_user_info_paylayout_history)
    public LinearLayout fragment_mine_user_info_paylayout_history;
    @BindView(R2.id.fragment_mine_user_info_paylayout_downmanager)
    public LinearLayout fragment_mine_user_info_paylayout_downmanager;

    @BindView(R2.id.fragment_mine_user_info_gold)
    public TextView fragment_mine_user_info_gold;
    @BindView(R2.id.fragment_mine_user_info_shuquan)
    public TextView fragment_mine_user_info_shuquan;
    @BindView(R2.id.fragment_mine_user_info_tasklayout_task)
    public TextView fragment_mine_user_info_tasklayout_task;
    @BindView(R2.id.fragment_mine_user_info_isvip)
    public ImageView fragment_mine_user_info_isvip;
    @BindView(R2.id.fragment_mine_user_info_paylayout)
    public LinearLayout fragment_mine_user_info_paylayout;

    @BindView(R2.id.fragment_mine_user_info_paylayout_view)
    public View fragment_mine_user_info_paylayout_view;

    Gson gson = new Gson();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fragment_mine_user_info_gold_unit.setText(getCurrencyUnit(activity));
        fragment_mine_user_info_shuquan_unit.setText(getSubUnit(activity));
        fragment_mine_user_info_recharge_song.setText(String.format(LanguageUtil.getString(activity, R.string.MineNewFragment_song), getSubUnit(activity)));
        fragment_mine_user_info_friends_song.setText(String.format(LanguageUtil.getString(activity, R.string.MineNewFragment_song), getSubUnit(activity)));
        fragment_mine_user_info_recharge_text.setText(String.format(LanguageUtil.getString(activity, R.string.MineNewFragment_chongzhi), getCurrencyUnit(activity)));
        if (!ReaderConfig.USE_PAY) {
            //fragment_mine_user_info_sign.setVisibility(View.GONE);
            fragment_mine_user_info_money_layout.setVisibility(View.GONE);
            fragment_mine_user_info_paylayout.setVisibility(View.GONE);
            fragment_mine_user_info_paylayout_view.setVisibility(View.GONE);
        }

        MainHttpTask.getInstance().getResultString(activity, "Mine", new MainHttpTask.GetHttpData() {
            @Override
            public void getHttpData(String result) {
                initInfo(result, null);
            }
        });
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) fragment_mine_user_info_isvip.getLayoutParams();

        layoutParams.height = ImageUtil.dp2px(activity, 14);
        layoutParams.width = layoutParams.height * 138 / 48;
        fragment_mine_user_info_isvip.setLayoutParams(layoutParams);

    }

    @Override
    public void onStart() {
        super.onStart();
        if (ReaderConfig.REFREASH_USERCENTER) {
            refreshData();
            ReaderConfig.REFREASH_USERCENTER = false;
        }

    }

    public void initInfo(final String info, UserInfoItem userInfoItem) {
        if (!Utils.isLogin(activity)) {//
            fragment_mine_user_info_nologin.setVisibility(View.GONE);
            return;
        } else {
            fragment_mine_user_info_nologin.setVisibility(View.VISIBLE);
        }
        try {
            if (userInfoItem != null) {
                mUserInfo = userInfoItem;
            } else
                mUserInfo = gson.fromJson(info, UserInfoItem.class);
            if (mUserInfo.getIs_vip() == 1) {
                fragment_mine_user_info_isvip.setImageResource(R.mipmap.icon_isvip);
                IS_VIP=true;
            } else {

            }
            if (mUserInfo.getAuto_sub() == 0) {
                AppPrefs.putSharedBoolean(activity, ReaderConfig.AUTOBUY, false);
            } else {
                AppPrefs.putSharedBoolean(activity, ReaderConfig.AUTOBUY, true);
            }
            //   ImageLoader.getInstance().displayImage(mUserInfo.getAvatar(), fragment_mine_user_info_avatar, ReaderApplication.getOptions());
            MyPicasso.IoadImage(activity, mUserInfo.getAvatar(), R.mipmap.icon_def_head, fragment_mine_user_info_avatar);

            fragment_mine_user_info_nickname.setText(mUserInfo.getNickname());
            fragment_mine_user_info_id.setText("ID:  " + mUserInfo.getUid());

            //  fragment_mine_user_info_gold_unit.setText(String.format(LanguageUtil.getString(activity, R.string.MineNewFragment_yue), mUserInfo.getUnit()));
            //  fragment_mine_user_info_shuquan_unit.setText(String.format(LanguageUtil.getString(activity, R.string.MineNewFragment_yue), mUserInfo.getSubUnit()));

            fragment_mine_user_info_tasklayout_task.setText(mUserInfo.getTask_list().getFinish_num() + "/" + mUserInfo.getTask_list().getMission_num());
            fragment_mine_user_info_gold.setText(mUserInfo.getGoldRemain() + " ");
            fragment_mine_user_info_shuquan.setText(mUserInfo.getSilverRemain() + " ");
            //fragment_mine_user_info_sign.setImageResource(mUserInfo.getSign_status() == 1 ? R.mipmap.ic_signed : R.mipmap.ic_sign);
         /*   if (mUserInfo.getGender() == 0) {
                fragment_mine_user_info_sex.setImageResource(R.mipmap.ic_sex_unknow_selected);
            } else if (mUserInfo.getGender() == 1) {
                fragment_mine_user_info_sex.setImageResource(R.mipmap.ic_boy);
            } else {
                fragment_mine_user_info_sex.setImageResource(R.mipmap.ic_girl);
            }*/


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void refreshData() {
        ReaderConfig.REFREASH_USERCENTER = false;
        if (!Utils.isLogin(activity)) {//
            fragment_mine_user_info_nickname.setText(LanguageUtil.getString(activity, R.string.user_login));
            fragment_mine_user_info_id.setText("");
            fragment_mine_user_info_gold.setText("--");
            fragment_mine_user_info_shuquan.setText("--");
            fragment_mine_user_info_tasklayout_task.setText("--");
            fragment_mine_user_info_avatar.setImageResource(R.mipmap.hold_user_avatar);
            fragment_mine_user_info_isvip.setImageResource(R.mipmap.icon_novip);
            fragment_mine_user_info_nologin.setVisibility(View.GONE);
            return;
        } else {
            fragment_mine_user_info_nologin.setVisibility(View.VISIBLE);
        }


        ReaderParams params = new ReaderParams(activity);
        String json = params.generateParamsJson();

        HttpUtils.getInstance(activity).sendRequestRequestParams3(ReaderConfig.mUserCenterUrl, json, true, new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(final String result) {
                        initInfo(result, null);

                    }

                    @Override
                    public void onErrorResponse(String ex) {

                    }
                }

        );
    }

    @OnClick(value = {R.id.fragment_mine_user_info_avatar,
            R.id.fragment_mine_user_info_paylayout_recharge, R.id.fragment_mine_user_info_paylayout_vip,// R.id.fragment_mine_user_info_paylayout_rechargenotes,
            R.id.fragment_mine_user_info_tasklayout_mybookcomment,
            R.id.fragment_mine_user_info_tasklayout_feedback, R.id.fragment_mine_user_info_tasklayout_set,
            R.id.fragment_mine_user_info_tasklayout_friends, R.id.fragment_mine_user_info_nickname,
            R.id.fragment_mine_user_info_tasklayout_layout, R.id.fragment_mine_user_info_tasklayout_layout2, R.id.fragment_mine_user_info_shuquan_layout,
            R.id.fragment_mine_user_info_gold_layout, R.id.fragment_mine_user_info_paylayout_downmanager,
            R.id.fragment_mine_user_info_paylayout_history, R.id.fragment_mine_user_info_lianxi
    })
    public void getEvent(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.fragment_mine_user_info_lianxi:
                startActivity(new Intent(activity, AboutUsActivity.class));
                break;
            case R.id.fragment_mine_user_info_avatar:
            case R.id.fragment_mine_user_info_nickname:
                if (!Utils.isLogin(activity)) {//登录状态跳个人资料
                    startActivity(new Intent(activity, LoginActivity.class));
                } else {
                    intent.setClass(activity, UserInfoActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.fragment_mine_user_info_gold_layout:
                intent.setClass(activity, BaseOptionActivity.class)
                        .putExtra("OPTION", LIUSHUIJIELU)
                        .putExtra("title", LanguageUtil.getString(activity, R.string.liushuijilu_title))
                        .putExtra("Extra", false);
                startActivity(intent);
                break;
            case R.id.fragment_mine_user_info_shuquan_layout:
                intent.setClass(activity, BaseOptionActivity.class)
                        .putExtra("title", LanguageUtil.getString(activity, R.string.liushuijilu_title))
                        .putExtra("OPTION", LIUSHUIJIELU)
                        .putExtra("Extra", true);
                startActivity(intent);
                break;
            case R.id.fragment_mine_user_info_paylayout_recharge:
                intent.setClass(activity, RechargeActivity.class);
                intent.putExtra("isvip", false);
                startActivity(intent);
                break;
            case R.id.fragment_mine_user_info_paylayout_vip:
                intent.setClass(activity, AcquireBaoyueActivity.class);
                intent.putExtra("isvip", true);
                startActivity(intent);
                break;
            case R.id.fragment_mine_user_info_tasklayout_layout2:
            case R.id.fragment_mine_user_info_tasklayout_layout:

                if (Utils.isLogin(activity)) {
                    intent.setClass(activity, TaskCenterActivity.class);
                } else {
                    intent.setClass(activity, LoginActivity.class);
                    MyToash.ToashError(activity, LanguageUtil.getString(activity, R.string.MineNewFragment_nologin));
                }
                startActivity(intent);
                break;
            case R.id.fragment_mine_user_info_tasklayout_mybookcomment:
                startActivity(new Intent(activity, BaseOptionActivity.class)
                        .putExtra("OPTION", MYCOMMENT)
                        .putExtra("title", LanguageUtil.getString(activity, R.string.MineNewFragment_shuping)));
                break;
            case R.id.fragment_mine_user_info_tasklayout_feedback:
                intent.setClass(activity, FeedBackActivity.class);
                startActivity(intent);
                break;
            case R.id.fragment_mine_user_info_tasklayout_set:
                SettingsActivity.chengeLangaupage = false;
                startActivity(new Intent(activity, SettingsActivity.class));
                break;

            case R.id.fragment_mine_user_info_tasklayout_friends:

                MyShare.ShareAPP(activity);
                break;

            case R.id.fragment_mine_user_info_paylayout_history:

                startActivity(new Intent(activity, BaseOptionActivity.class)
                        .putExtra("OPTION", READHISTORY)
                        .putExtra("title", LanguageUtil.getString(activity, R.string.noverfragment_yuedulishi)));
                break;
            case R.id.fragment_mine_user_info_paylayout_downmanager:

                startActivity(new Intent(activity, BaseOptionActivity.class).putExtra("OPTION", DOWN).putExtra("title", LanguageUtil.getString(activity, R.string.BookInfoActivity_down_manger)));

                break;

        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refresh(RefreshMine refreshMine) {
        refreshData();
    }
}
