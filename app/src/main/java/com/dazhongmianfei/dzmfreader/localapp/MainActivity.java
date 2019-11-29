package com.dazhongmianfei.dzmfreader.localapp;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.navigation.NavigationView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.dazhongmianfei.dzmfreader.filesearcher.FileSearcher;
import com.dazhongmianfei.dzmfreader.R;
import com.dazhongmianfei.dzmfreader.activity.AboutUsActivity;
import com.dazhongmianfei.dzmfreader.activity.FeedBackActivity;
import com.dazhongmianfei.dzmfreader.activity.SettingsActivity;
import com.dazhongmianfei.dzmfreader.book.been.BaseBook;
import com.dazhongmianfei.dzmfreader.bean.ChapterItem;
import com.dazhongmianfei.dzmfreader.localapp.bean.Book;


import com.dazhongmianfei.dzmfreader.read.manager.ChapterManager;
import com.dazhongmianfei.dzmfreader.utils.LanguageUtil;
import com.dazhongmianfei.dzmfreader.utils.MyToash;
import com.dazhongmianfei.dzmfreader.utils.Utils;

import org.litepal.LitePal;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by will on 2016/10/29.
 */

public class MainActivity extends AppCompatActivity {
    private static final int RESTART_REQUEST = 123;
    private Toast mToast;
    private BookListAdapter mAdapter;
    private Toolbar toolbar;
    private LinearLayout main_recycler_null;
    RecyclerView recyclerView;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle mDrawerToggle;
    NavigationView navigationView;
    Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = MainActivity.this;
        setContentView(R.layout.activity_localmain);
        initializeView();
    }


    private void initializeView() {
        drawerLayout = findViewById(R.id.drawerLayout);
        toolbar = findViewById(R.id.main_toolbar);
        navigationView = findViewById(R.id.navigation_view);
        setSupportActionBar(toolbar);
        mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, 0, 0);
        mDrawerToggle.syncState();
        drawerLayout.addDrawerListener(mDrawerToggle);
        navigationView.setItemIconTintList(null);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getTitle().toString()) {
                    case "我的收藏":
                        MyToash.ToashError(MainActivity.this, "暂无收藏");
                        break;
                    case "清理缓存":
                        MyToash.ToashSuccess(MainActivity.this, "清理成功");
                        break;
                    case "好评支持":
                        SettingsActivity.support(MainActivity.this);

                        break;
                    case "关于我们":

                        startActivity(new Intent(MainActivity.this, AboutUsActivity.class));
                        break;
                }
                //  MyToash.Toash(MainActivity.this,item.getItemId() + "--" + item.getTitle().toString());
                return true;
            }
        });

        //


        recyclerView = findViewById(R.id.main_recycler_view);
        main_recycler_null = findViewById(R.id.main_recycler_null);
        final GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(layoutManager);

        List<Book> books = LitePal.where().find(Book.class);
        if (books == null) {
            books = new ArrayList<>();
        }
        if (books.size() == 0) {
            main_recycler_null.setVisibility(View.VISIBLE);
        } else {
            main_recycler_null.setVisibility(View.GONE);
        }
        main_recycler_null.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new FileSearcher(MainActivity.this)
                        .withExtension("txt")
                        .withSizeLimit(10 * 1024, -1)
                        .search(new FileSearcher.FileSearcherCallback() {
                            @Override
                            public void onSelect(final List<File> files) {
                                mAdapter.addBookFromFile(MainActivity.this, files);
                            }
                        });
            }
        });
        mAdapter = new BookListAdapter(this, books);



        mAdapter.setOnClickCallback(new BookListAdapter.ClickCallback() {
            @Override
            public void onClick(final Book book) {

                BaseBook mBaseBook = new BaseBook();
                mBaseBook.setBook_id(book.getPath());
                mBaseBook.setTotal_Chapter(1);
                mBaseBook.setName(book.getBookName());
                mBaseBook.setRecentChapter(1);
                mBaseBook.setUid(Utils.getUID(activity));
                mBaseBook.saveIsexist(0);

                ChapterItem querychapterItem = new ChapterItem();
                querychapterItem.setChapter_id(book.getPath());
                querychapterItem.setChapter_title("第一章");
                querychapterItem.setIs_vip("0");
                querychapterItem.setIs_preview("0");
                querychapterItem.setDisplay_order(0);
                querychapterItem.setChapteritem_begin(0);
                querychapterItem.setBook_id(book.getPath());
                querychapterItem.setChapter_path(book.getPath());
                querychapterItem.setBook_name(book.getBookName());
                querychapterItem.setChaptertab("本地小说");
                querychapterItem.setChaptercolor("#000000");
                querychapterItem.setCharset("utf-8");
                querychapterItem.setNext_chapter_id("-2");
                querychapterItem.setPre_chapter_id("-1");
                querychapterItem.save();
                ChapterManager.getInstance(activity).openBook(mBaseBook,book.getPath(), book.getPath());

            }

            @Override
            public void onLongClick(final Book book) {

                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("删除")
                        .setMessage("确定删除?")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                mAdapter.deleteBook(book);
                                LitePal.deleteAll(Book.class,"path=?",book.getPath());
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .show();


            }


        });

        recyclerView.setAdapter(mAdapter);


    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.main_menu_search:
                new FileSearcher(this)
                        .withExtension("txt")
                        .withSizeLimit(10 * 1024, -1)
                        .search(new FileSearcher.FileSearcherCallback() {
                            @Override
                            public void onSelect(final List<File> files) {
                                mAdapter.addBookFromFile(MainActivity.this, files);
                            }
                        });
                break;
            case R.id.main_menu_management:
                //  mAdapter.showDeleteButton(!mAdapter.isDeleteButtonVisible());
                break;
            case R.id.main_menu_delete_all:
                showDeleteAllDialog();
                break;
            case R.id.main_menu_feedback:
                startActivity(new Intent(this, FeedBackActivity.class));
                break;

        }

        return true;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mAdapter.notifyDataSetChanged();
        if (mAdapter.getBooks().size() == 0) {
            main_recycler_null.setVisibility(View.VISIBLE);
        } else {
            main_recycler_null.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == RESTART_REQUEST && resultCode == Activity.RESULT_OK) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        if (mToast == null) {

        }
        if (mAdapter.isDeleteButtonVisible()) {
            // mAdapter.showDeleteButton(false);
        } else if (mToast.getView().getParent() == null) {
          MyToash.ToashSuccess(activity, LanguageUtil.getString(activity,R.string.MainActivity_exit));
        } else {
            super.onBackPressed();
        }
    }

    private void showDeleteAllDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppDialogTheme);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mAdapter.clearData();
                LitePal.deleteAll(Book.class);
                MyToash.ToashSuccess(activity,"已删除");

            }
        });
        builder.setMessage("确认删除全部书籍？");
        builder.setNegativeButton("取消", null);
        builder.show();
    }

}
