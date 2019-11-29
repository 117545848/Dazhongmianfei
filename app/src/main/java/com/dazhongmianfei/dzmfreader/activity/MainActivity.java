package com.dazhongmianfei.dzmfreader.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.core.content.ContextCompat;

import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.dazhongmianfei.dzmfreader.book.fragment.DiscoveryBookFragment;
import com.dazhongmianfei.dzmfreader.book.fragment.NovelFragmentNew;
import com.dazhongmianfei.dzmfreader.http.OkHttp3Engine;
import com.google.gson.Gson;
import com.dazhongmianfei.dzmfreader.R2;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMShareAPI;
import com.dazhongmianfei.dzmfreader.R;
import com.dazhongmianfei.dzmfreader.adapter.MyFragmentPagerAdapter;
import com.dazhongmianfei.dzmfreader.bean.AppUpdate;
import com.dazhongmianfei.dzmfreader.book.been.BaseBook;
import com.dazhongmianfei.dzmfreader.comic.been.BaseComic;
import com.dazhongmianfei.dzmfreader.dialog.MyPoPwindow;
import com.dazhongmianfei.dzmfreader.eventbus.ToStore;
import com.dazhongmianfei.dzmfreader.fragment.BookshelfFragment;
import com.dazhongmianfei.dzmfreader.fragment.DiscoveryNewFragment;
import com.dazhongmianfei.dzmfreader.fragment.MineNewFragment;
import com.dazhongmianfei.dzmfreader.fragment.StroeNewFragment;
import com.dazhongmianfei.dzmfreader.utils.LanguageUtil;
import com.dazhongmianfei.dzmfreader.utils.MyToash;
import com.dazhongmianfei.dzmfreader.utils.ShareUitls;
import com.dazhongmianfei.dzmfreader.utils.UpdateApp;
import com.dazhongmianfei.dzmfreader.utils.Utils;
import com.dazhongmianfei.dzmfreader.view.AndroidWorkaround;
import com.dazhongmianfei.dzmfreader.view.CustomScrollViewPager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.dazhongmianfei.dzmfreader.config.ReaderConfig.syncDevice;
import static com.dazhongmianfei.dzmfreader.fragment.StroeNewFragment.IS_NOTOP;
import static com.dazhongmianfei.dzmfreader.utils.StatusBarUtil.setStatusTextColor;

public class MainActivity extends BaseButterKnifeTransparentActivity {
    @BindView(R2.id.RadioGroup)
    public RadioGroup mRadioGroup;
    @BindView(R2.id.fragment_home_container)
    public CustomScrollViewPager customScrollViewPage;
    public long mExitTime = 0;
    @BindView(R2.id.main_menu_layout)
    public LinearLayout mNavigationView;

    @BindView(R2.id.home_novel_layout)
    public RadioButton home_novel_layout;
    @BindView(R2.id.home_store_layout)
    public RadioButton home_store_layout;
    @BindView(R2.id.home_discovery_layout)
    public RadioButton home_discovery_layout;
    @BindView(R2.id.home_mine_layout)
    public RadioButton home_mine_layout;

    @BindView(R2.id.shelf_book_delete_btn)
    public LinearLayout shelf_book_delete_btn;
    private List<Fragment> mFragmentList;

    @Override
    public int initContentView() {
        return R.layout.activity_main;
    }

    public static Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        try {
            EventBus.getDefault().register(this);
            permission(activity);
            MobclickAgent.openActivityDurationTrack(false);
            MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);
            if (AndroidWorkaround.checkDeviceHasNavigationBar(this)) {                                  //适配华为手机虚拟键遮挡tab的问题
                AndroidWorkaround.assistActivity(findViewById(android.R.id.content));                   //需要在setContentView()方法后面执行
            }
            initData();
        } catch (Exception e) {
        }
    }

    private AppUpdate mAppUpdate;
    List<BaseBook> bookLists;
    List<BaseComic> comicList;
    MyFragmentPagerAdapter myFragmentPagerAdapter;
    private int possition = 5;

    DiscoveryBookFragment discoveryBookFragment;

    public void initData() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        //判断本地书架是否有数据
        Intent intent = getIntent();
        if (intent != null) {
            bookLists = (List<BaseBook>) (intent.getSerializableExtra("mBaseBooks"));
            comicList = (List<BaseComic>) (intent.getSerializableExtra("mBaseComics"));
        }
        mFragmentList = new ArrayList<>();
      /*  BookshelfFragment bookshelfFragment = new BookshelfFragment(bookLists, comicList, shelf_book_delete_btn);
        mFragmentList.add(bookshelfFragment);*/
        NovelFragmentNew     novelFragment = new <Fragment>NovelFragmentNew(bookLists, shelf_book_delete_btn);

        mFragmentList.add(novelFragment);
        StroeNewFragment storeFragment = new StroeNewFragment();
        mFragmentList.add(storeFragment);

        discoveryBookFragment = new DiscoveryBookFragment();
        mFragmentList.add(discoveryBookFragment);

        MineNewFragment mineFragment = new MineNewFragment();
        mFragmentList.add(mineFragment);

        myFragmentPagerAdapter = new MyFragmentPagerAdapter(fragmentManager, mFragmentList);
        customScrollViewPage.setOffscreenPageLimit(4);
        customScrollViewPage.setAdapter(myFragmentPagerAdapter);
        setOption();
    }

    private void setOption() {
        setStatusTextColor(false, activity);
        home_store_layout.setChecked(true);
        customScrollViewPage.setCurrentItem(1, false);
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.home_novel_layout:
                        if (possition != 0) {
                            setStatusTextColor(true, activity);
                            IntentFragment(0);
                        }
                        break;
                    case R.id.home_store_layout:
                        if (possition != 1) {
                            IntentFragment(1);
                            setStatusTextColor(IS_NOTOP, activity);
                        }
                        break;
                    case R.id.home_discovery_layout:

                        if (possition != 2) {
                            setStatusTextColor(true, activity);
                            IntentFragment(2);
                        }
                        if (discoveryBookFragment.todayOneAD != null) {
                            discoveryBookFragment.todayOneAD.nativeRender();
                        }
                        break;
                    case R.id.home_mine_layout:

                        if (possition != 3) {
                            setStatusTextColor(true, activity);
                            IntentFragment(3);
                            setStatusTextColor(true, activity);
                        }
                        break;
                }
            }
        });
        syncDevice(activity);
    }

    private void IntentFragment(int i) {
        customScrollViewPage.setCurrentItem(i);
        ShareUitls.putTab(activity, "LastFragment", i);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Utils.mDialog = null;
        activity = null;
        OkHttp3Engine.mInstance = null;
    }

    public View getNavigationView() {
        return mNavigationView;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (popupWindow != null && popupWindow.isShowing()) {
                return true;
            }

            if ((System.currentTimeMillis() - mExitTime) > 2000) {

                MyToash.ToashSuccess(activity, LanguageUtil.getString(activity, R.string.MainActivity_exit));
                mExitTime = System.currentTimeMillis();
            } else {
                exitAPP();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    private void exitAPP() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.AppTask> appTaskList = null;
            appTaskList = activityManager.getAppTasks();
            for (ActivityManager.AppTask appTask : appTaskList) {
                appTask.finishAndRemoveTask();
            }
        } else {

            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(startMain);
            System.exit(0);


        }
    }


    //为了解决弹出PopupWindow后外部的事件不会分发,既外部的界面不可以点击
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (popupWindow != null && popupWindow.isShowing()) {
            return false;

        }
        return super.dispatchTouchEvent(event);
    }

    //权限管理

    public static void permission(Activity activity) {
        List<String> permissionLists = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            permissionLists.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.REQUEST_INSTALL_PACKAGES) != PackageManager.PERMISSION_GRANTED) {
            permissionLists.add(Manifest.permission.REQUEST_INSTALL_PACKAGES);
        }
        if (!permissionLists.isEmpty()) {//说明肯定有拒绝的权限
            ActivityCompat.requestPermissions(activity, permissionLists.toArray(new String[permissionLists.size()]), 1);
        }
    }


    private Dialog popupWindow;
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                popupWindow = new UpdateApp().getAppUpdatePop(activity, mAppUpdate);
            } else {
                new MyPoPwindow().getSignPop(activity);
            }

        }
    };


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //签到弹框 更新弹框等
        try {
            ShowPOP();
        } catch (Exception e) {
        }
    }

    public void ShowPOP() {

        String str = ShareUitls.getString(activity, "Update", "");
        if (str.length() > 0) {
            mAppUpdate = new Gson().fromJson(str, AppUpdate.class);
            if (mAppUpdate != null && mAppUpdate.getUpdate() == 1 || mAppUpdate.getUpdate() == 2) {
                mRadioGroup.post(new Runnable() {
                    @Override
                    public void run() {
                        handler.sendEmptyMessage(0);
                    }
                });
            } else {
                final String result = ShareUitls.getString(activity, "sign_pop", "");//签到弹框
                if (result.length() != 0) {
                    mRadioGroup.post(new Runnable() {
                        @Override
                        public void run() {
                            handler.sendEmptyMessageDelayed(1, 1000);

                        }
                    });
                }
            }
        } else {
            final String result = ShareUitls.getString(activity, "sign_pop", "");//签到弹框
            if (result.length() != 0) {
                mRadioGroup.post(new Runnable() {
                    @Override
                    public void run() {
                        handler.sendEmptyMessageDelayed(1, 1000);

                    }
                });
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void ToStore(ToStore toStore) {
        //任务中心 点击看书任务  跳转书城
        home_store_layout.setChecked(true);
    }

}
