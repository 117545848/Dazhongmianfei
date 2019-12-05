package com.dazhongmianfei.dzmfreader.dialog;

import android.app.Activity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.dazhongmianfei.dzmfreader.R;
import com.dazhongmianfei.dzmfreader.R2;
import com.dazhongmianfei.dzmfreader.bean.Recommend;
import com.dazhongmianfei.dzmfreader.book.been.BaseBook;
import com.dazhongmianfei.dzmfreader.bean.SigninSuccess;
;
;
import com.dazhongmianfei.dzmfreader.config.ReaderApplication;
import com.dazhongmianfei.dzmfreader.eventbus.RefreshBookSelf;
import com.dazhongmianfei.dzmfreader.eventbus.RefreshMine;
import com.dazhongmianfei.dzmfreader.utils.ImageUtil;
import com.dazhongmianfei.dzmfreader.utils.ShareUitls;
import com.dazhongmianfei.dzmfreader.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.litepal.LitePal;
import org.litepal.crud.callback.SaveCallback;
//.view.annotation.ViewInject;
//.x;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by scb on 2018/10/31.
 */

public class MyPoPwindow {


    public void getSignPop(final Activity activity) {
        try {
            final String result = ShareUitls.getString(activity, "sign_pop", "");
            if (result.length() == 0) {
                return;
            }
            final SigninSuccess signinSuccess = new Gson().fromJson(result, SigninSuccess.class);
            if (signinSuccess == null) {
                return;
            }
            final List<Recommend.RecommendProduc> signBookList = signinSuccess.getBook();
            if (signBookList == null) {
                return;
            }
            int size = signBookList.size();
            View view = LayoutInflater.from(activity).inflate(R.layout.dialog_sign, null);

            final PopupWindow popupWindow;
            HolderSign holderSign = new HolderSign(view);
            popupWindow = new PopupWindow(view);
            popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            popupWindow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);

            if (size == 0) {
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) holderSign.popwindow_sign_layout.getLayoutParams();
                layoutParams.height = ImageUtil.dp2px(activity, 200);
                layoutParams.width = ImageUtil.dp2px(activity, 160);
                holderSign.popwindow_sign_layout.setLayoutParams(layoutParams);
            } else {
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) holderSign.popwindow_sign_layout.getLayoutParams();
                layoutParams.height = ImageUtil.dp2px(activity, 300);
                layoutParams.width = ImageUtil.dp2px(activity, 260);
                holderSign.popwindow_sign_layout.setLayoutParams(layoutParams);
            }
            holderSign.popwindow_sign_golds.setText(signinSuccess.getAward());
            holderSign.popwindow_sign_golds_tomorrow.setText(signinSuccess.getTomorrow_award());

            if (signBookList != null) {

                if (size == 0) {
                    holderSign.popwindow_sign_book_havebook.setVisibility(View.GONE);
                    holderSign.popwindow_sign_nobook.setVisibility(View.VISIBLE);
                    holderSign.popwindow_sign_nobook.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            popupWindow.dismiss();
                        }
                    });
                } else {
                    switch (size) {
                        case 1:
                            holderSign.popwindow_sign_book_layout2.setVisibility(View.GONE);
                            holderSign.popwindow_sign_book_layout3.setVisibility(View.GONE);
                            holderSign.popwindow_sign_view2.setVisibility(View.GONE);
                            holderSign.popwindow_sign_view3.setVisibility(View.GONE);
                            ImageLoader.getInstance().displayImage(signBookList.get(0).cover, holderSign.popwindow_sign_book1, ReaderApplication.getOptions());
                            holderSign.popwindow_sign_bookname1.setText(signBookList.get(0).name);
                            break;
                        case 2:
                            holderSign.popwindow_sign_book_layout3.setVisibility(View.GONE);
                            holderSign.popwindow_sign_view3.setVisibility(View.GONE);
                            ImageLoader.getInstance().displayImage(signBookList.get(0).cover, holderSign.popwindow_sign_book1, ReaderApplication.getOptions());
                            holderSign.popwindow_sign_bookname1.setText(signBookList.get(0).name);
                            ImageLoader.getInstance().displayImage(signBookList.get(1).cover, holderSign.popwindow_sign_book2, ReaderApplication.getOptions());
                            holderSign.popwindow_sign_bookname2.setText(signBookList.get(1).name);

                            break;
                        case 3:
                        default:
                            ImageLoader.getInstance().displayImage(signBookList.get(0).cover, holderSign.popwindow_sign_book1, ReaderApplication.getOptions());
                            holderSign.popwindow_sign_bookname1.setText(signBookList.get(0).name);
                            ImageLoader.getInstance().displayImage(signBookList.get(1).cover, holderSign.popwindow_sign_book2, ReaderApplication.getOptions());
                            holderSign.popwindow_sign_bookname2.setText(signBookList.get(1).name);
                            ImageLoader.getInstance().displayImage(signBookList.get(2).cover, holderSign.popwindow_sign_book3, ReaderApplication.getOptions());
                            holderSign.popwindow_sign_bookname3.setText(signBookList.get(2).name);
                            break;


                    }
                    holderSign.popwindow_sign_no.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            popupWindow.dismiss();
                        }
                    });
                    holderSign.popwindow_sign_alladd.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            List<BaseBook> list = new ArrayList<>();
                            for (Recommend.RecommendProduc addrecommendProducs : signBookList) {
                                if (addrecommendProducs.book_id != null) {
                                    BaseBook mBaseBook = new BaseBook();
                                    mBaseBook.setBook_id(addrecommendProducs.book_id);
                                    mBaseBook.setName(addrecommendProducs.name);
                                    mBaseBook.setCover(addrecommendProducs.cover);
                                    mBaseBook.setRecentChapter(addrecommendProducs.total_chapter);
                                    mBaseBook.setTotal_Chapter(addrecommendProducs.total_chapter);
                                    mBaseBook.setDescription(addrecommendProducs.description);
                                    mBaseBook.setName(addrecommendProducs.name);
                                    mBaseBook.saveIsexist(1);
                                    mBaseBook.setAddBookSelf(1);
                                    list.add(mBaseBook);
                                }
                            }

                            LitePal.saveAllAsync(list).listen(new SaveCallback() {
                                @Override
                                public void onFinish(boolean success) {
                                    bookadd = true;
                                    if (comicadd) {
                                        bookadd = false;
                                        if (!list.isEmpty()) {
                                            EventBus.getDefault().post(new RefreshBookSelf(list));
                                        }

                                    }
                                }
                            });


                            popupWindow.dismiss();
                        }
                    });


                }

            }


            popupWindow.setFocusable(true);
            popupWindow.setOutsideTouchable(false);
            popupWindow.setAnimationStyle(R.style.sign_pop_anim);
            popupWindow.showAtLocation(activity.getWindow().getDecorView(), Gravity.CENTER, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        } catch (Exception e) {
            //    Log.i("RequestParams22--", "签到弹窗异常");
        }
        ShareUitls.putString(activity, "sign_pop", "");
    }

    boolean bookadd = false, comicadd = false;
    class HolderSign {
        @BindView(R2.id.popwindow_sign_golds)
        public TextView popwindow_sign_golds;
        @BindView(R2.id.popwindow_sign_golds_tomorrow)
        public TextView popwindow_sign_golds_tomorrow;
        @BindView(R2.id.popwindow_sign_layout)
        public FrameLayout popwindow_sign_layout;

        @BindView(R2.id.popwindow_sign_bookname1)
        public TextView popwindow_sign_bookname1;
        @BindView(R2.id.popwindow_sign_bookname2)
        public TextView popwindow_sign_bookname2;
        @BindView(R2.id.popwindow_sign_bookname3)
        public TextView popwindow_sign_bookname3;


        @BindView(R2.id.popwindow_sign_book_layout)
        public LinearLayout popwindow_sign_book_layout;
        @BindView(R2.id.popwindow_sign_book_layout1)
        public LinearLayout popwindow_sign_book_layout1;
        @BindView(R2.id.popwindow_sign_book_layout2)
        public LinearLayout popwindow_sign_book_layout2;
        @BindView(R2.id.popwindow_sign_book_layout3)
        public LinearLayout popwindow_sign_book_layout3;
        @BindView(R2.id.popwindow_sign_book_havebook)
        public LinearLayout popwindow_sign_book_havebook;
        @BindView(R2.id.popwindow_sign_view2)
        public View popwindow_sign_view2;
        @BindView(R2.id.popwindow_sign_view3)
        public View popwindow_sign_view3;


        @BindView(R2.id.popwindow_sign_book1)
        public ImageView popwindow_sign_book1;
        @BindView(R2.id.popwindow_sign_book2)
        public ImageView popwindow_sign_book2;
        @BindView(R2.id.popwindow_sign_book3)
        public ImageView popwindow_sign_book3;


        @BindView(R2.id.popwindow_sign_no)
        public Button popwindow_sign_no;
        @BindView(R2.id.popwindow_sign_alladd)
        public Button popwindow_sign_alladd;
        @BindView(R2.id.popwindow_sign_nobook)
        public Button popwindow_sign_nobook;
        public HolderSign(View view) {
            ButterKnife.bind(this, view);
        }

    }


}
