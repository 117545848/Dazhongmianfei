package com.dazhongmianfei.dzmfreader.comic.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.dazhongmianfei.dzmfreader.R;
import com.dazhongmianfei.dzmfreader.R2;
import com.dazhongmianfei.dzmfreader.activity.BaseButterKnifeActivity;
import com.dazhongmianfei.dzmfreader.activity.LoginActivity;
import com.dazhongmianfei.dzmfreader.comic.adapter.CommentAdapter;
import com.dazhongmianfei.dzmfreader.comic.been.ComicComment;
import com.dazhongmianfei.dzmfreader.comic.been.RefreashComicInfoActivity;
import com.dazhongmianfei.dzmfreader.comic.config.ComicConfig;
import com.dazhongmianfei.dzmfreader.http.ReaderParams;
import com.dazhongmianfei.dzmfreader.utils.HttpUtils;
import com.dazhongmianfei.dzmfreader.utils.LanguageUtil;
import com.dazhongmianfei.dzmfreader.utils.MyToash;
import com.dazhongmianfei.dzmfreader.utils.Utils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;

public class ComicCommentActivity extends BaseButterKnifeActivity {

    @BindView(R2.id.titlebar_back)
    public LinearLayout titlebar_back;
    @BindView(R2.id.titlebar_text)
    public TextView titlebar_text;

    @BindView(R2.id.activity_finish_listview_noresult)
    public LinearLayout mNoResult;
    @BindView(R2.id.activity_finish_listview)
    public ListView mListView;
    @BindView(R2.id.activity_comment_list_add_comment)
    public EditText activity_comment_list_add_comment;

    // public RelativeLayout mSearchLayout;

    boolean IsBook;
    int mCurrentPage = 1, total_count;
    Gson gson = new Gson();
    List<ComicComment.Comment> commentList;
    CommentAdapter commentAdapter;
    String comic_id,mCommentId,mNickName;

    @OnClick(value = {R.id.titlebar_back
    })
    public void getEvent(View view) {
        switch (view.getId()) {
            case R.id.titlebar_back:
                Intent intent = new Intent();
                intent.putExtra("total_count", total_count);
                setResult(112, intent);
                finish();
                break;

        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            Intent intent = new Intent();
            intent.putExtra("total_count", total_count);
            setResult(112, intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public int initContentView() {
        return R.layout.activity_comiccomment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  EventBus.getDefault().register(this);
        init();

    }

    private void init() {
        activity_comment_list_add_comment.setHorizontallyScrolling(false);
        activity_comment_list_add_comment.setMaxLines(Integer.MAX_VALUE);
        titlebar_text.setText(LanguageUtil.getString(this, R.string.CommentListActivity_title));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE) {
            activity_comment_list_add_comment.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_SEND) {
                        String str = activity_comment_list_add_comment.getText().toString();
                        if (TextUtils.isEmpty(str) || Pattern.matches("\\s*", str)) {
                            MyToash.ToashError(activity, LanguageUtil.getString(activity, R.string.CommentListActivity_some));
                            return true;
                        }
                        sendComment(activity, comic_id, mCommentId,str, new SendSuccess() {
                            @Override
                            public void Success() {
                                activity_comment_list_add_comment.setText("");
                                total_count++;
                                mCurrentPage = 1;
                                getHttp();
                                //EventBus.getDefault().post(new RefreashComicInfoActivity(false));//刷新漫画详情 漫画评论页等   评论列表
                                //finish();
                            }
                        });
                        return true;
                    }
                    return false;
                }

            });
        }
        commentList = new ArrayList<>();
        Intent intent = getIntent();
        comic_id = intent.getStringExtra("comic_id");
        mCommentId = intent.getStringExtra("comment_id");
        mNickName = intent.getStringExtra("nickname");
        if(mCommentId!=null&&mNickName!=null){
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            activity_comment_list_add_comment.requestFocus();
            activity_comment_list_add_comment.setHint(LanguageUtil.getString(activity, R.string.CommentListActivity_huifu) + mNickName);
        }
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                activity_comment_list_add_comment.requestFocus();
                ComicComment.Comment comicComment = commentList.get(position);
                activity_comment_list_add_comment.setHint(LanguageUtil.getString(activity, R.string.CommentListActivity_huifu) + comicComment.getNickname());


             /*
                Intent intent = new Intent(activity, ReplyCommentActivity.class);
                intent.putExtra("comic_id", comic_id);
                intent.putExtra("comment_id", commentList.get(position).getComment_id());
                intent.putExtra("avatar", commentList.get(position).getAvatar());
                intent.putExtra("nickname", commentList.get(position).getNickname());
                intent.putExtra("origin_content", commentList.get(position).getContent());
                startActivity(intent);
*/


            }
        });
        getHttp();
    }

    public void addreplyComment() {
        if (!Utils.isLogin(this)) {
            startActivity(new Intent(this, LoginActivity.class));
            return;
        }
        String str = activity_comment_list_add_comment.getText().toString();
        if (TextUtils.isEmpty(str)) {
            MyToash.ToashError(activity, LanguageUtil.getString(activity, R.string.CommentListActivity_some));
            return;
        }
        if (Pattern.matches("\\s*", str)) {
            MyToash.ToashError(activity, LanguageUtil.getString(activity, R.string.CommentListActivity_some));
            return;
        }
        String url = "";
        ReaderParams params = new ReaderParams(this);

        params.putExtraParams("comic_id", comic_id);

        url= ComicConfig.COMIC_sendcomment;
        params.putExtraParams("comment_id", mCommentId);
        params.putExtraParams("content", str);
        String json = params.generateParamsJson();

        HttpUtils.getInstance(this).sendRequestRequestParams3(url, json, true, new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(final String result) {
                           MyToash.ToashSuccess(activity, LanguageUtil.getString(activity, R.string.CommentListActivity_success));
                            EventBus.getDefault().post(new RefreashComicInfoActivity(false));//刷新漫画详情 漫画评论页等   评论列表
                    
                    }

                    @Override
                    public void onErrorResponse(String ex) {

                    }
                }

        );
    }
    public void getHttp() {


        String requestParams;
        ReaderParams params = new ReaderParams(activity);
        requestParams = ComicConfig.COMIC_comment_list;
        params.putExtraParams("comic_id", comic_id);
        params.putExtraParams("page_num", mCurrentPage + "");
        String json = params.generateParamsJson();
        HttpUtils.getInstance(activity).sendRequestRequestParams3(requestParams, json, true, new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(final String result) {
                        if (!IsBook) {
                            ComicComment comicComment = gson.fromJson(result, ComicComment.class);
                            //    MyToash.Log("comicComment", mCurrentPage+"   "+comicComment.toString());
                            if ((mCurrentPage > comicComment.total_page)) {
                                if (mCurrentPage > 1) {
                                    MyToash.ToashError(activity, LanguageUtil.getString(activity, R.string.ReadActivity_chapterfail));
                                }
                                return;
                            }
                            if (mCurrentPage == 1) {
                                total_count = comicComment.total_count;
                            }

                            if (!comicComment.list.isEmpty()) {

                                if (mCurrentPage == 1) {
                                    commentList.clear();
                                    commentList.addAll(comicComment.list);
                                    commentAdapter = new CommentAdapter(activity, commentList, commentList.size(), false);
                                    mListView.setAdapter(commentAdapter);
                                } else {
                                    commentList.addAll(comicComment.list);
                                    commentAdapter.notifyDataSetChanged();
                                }

                                mCurrentPage = comicComment.current_page;
                                mCurrentPage++;
                            }
                            if (commentList.isEmpty()) {
                                mNoResult.setVisibility(View.VISIBLE);
                                mListView.setVisibility(View.GONE);
                            } else {
                                mNoResult.setVisibility(View.GONE);
                                mListView.setVisibility(View.VISIBLE);
                            }
                        }

                    }

                    @Override
                    public void onErrorResponse(String ex) {

                    }
                }

        );
    }

    interface SendSuccess {
        void Success();
    }

    public static void sendComment(Activity activity, String comic_id, String mCommentId,String content, SendSuccess sendSuccess) {
        if (!Utils.isLogin(activity)) {
            MyToash.ToashError(activity, LanguageUtil.getString(activity, R.string.MineNewFragment_nologin));
            Intent intent = new Intent();
            intent.setClass(activity, LoginActivity.class);
            activity.startActivity(intent);
            return;
        }
        ReaderParams params = new ReaderParams(activity);
        String requestParams = ComicConfig.COMIC_sendcomment;
        params.putExtraParams("comic_id", comic_id);
        if(mCommentId!=null) {
          params.putExtraParams("comment_id", mCommentId);
        }
        params.putExtraParams("content", content);
        String json = params.generateParamsJson();
        HttpUtils.getInstance(activity).sendRequestRequestParams3(requestParams, json, true, new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(final String result) {
                        //MyToash.ToashSuccess(activity, LanguageUtil.getString(activity, R.string.CommentListActivity_success));
                        if (sendSuccess != null) {
                            sendSuccess.Success();

                        }
                    }

                    @Override
                    public void onErrorResponse(String ex) {

                    }
                }

        );
    }

    /*    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        protected void onDestroy() {
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN) {
                mSearchLayout.getViewTreeObserver().removeGlobalOnLayoutListener(mOnGlobalLayoutListener);
            } else {
                mSearchLayout.getViewTreeObserver().removeOnGlobalLayoutListener(mOnGlobalLayoutListener);
            }
            super.onDestroy();
        }*/


    /*@Subscribe(threadMode = ThreadMode.MAIN)
    public void refresh(RefreashComicInfoActivity refreshBookInfo) {
        if (!refreshBookInfo.isSave) {
            mCurrentPage = 1;
            getHttp();
        }
    }*/

    @Override
    public void finish() {
        // TODO Auto-generated method stub
        super.finish();
        //关闭窗体动画显示
        this.overridePendingTransition(R.anim.activity_close, 0);
    }
}
