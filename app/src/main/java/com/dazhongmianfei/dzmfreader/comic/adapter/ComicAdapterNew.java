package com.dazhongmianfei.dzmfreader.comic.adapter;

import android.app.Activity;
import android.graphics.Color;
import androidx.core.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.dazhongmianfei.dzmfreader.R;
import com.dazhongmianfei.dzmfreader.R2;
import com.dazhongmianfei.dzmfreader.comic.been.BaseComic;

import com.dazhongmianfei.dzmfreader.comic.been.ComicChapter;
import com.dazhongmianfei.dzmfreader.comic.config.ComicConfig;
import com.dazhongmianfei.dzmfreader.config.ReaderApplication;

import com.dazhongmianfei.dzmfreader.eventbus.ToStore;
import com.dazhongmianfei.dzmfreader.fragment.BookshelfFragment;
import com.dazhongmianfei.dzmfreader.http.ReaderParams;
import com.dazhongmianfei.dzmfreader.utils.FileManager;
import com.dazhongmianfei.dzmfreader.utils.HttpUtils;
import com.dazhongmianfei.dzmfreader.utils.LanguageUtil;
import com.dazhongmianfei.dzmfreader.utils.MyToash;
import com.dazhongmianfei.dzmfreader.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.litepal.LitePal;
//.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *
 */
public class ComicAdapterNew extends BaseAdapter {


    public ImageView shelfitem_img_first;
    private List<BaseComic> mBookList;
    private Activity mActivity;
    private TextView mDeleteBtn;
    int ListSize = 0;
    /**
     * 是否是处于编辑状态，可删除
     */
    private boolean mIsDeletable = false;

    public List<BaseComic> checkedBookList = new ArrayList<>();
    public int WIDTH, HEIGHT;

    public ComicAdapterNew(int WIDTH, int HEIGHT, List<BaseComic> mBookList, Activity mActivity) {
        MyToash.Log("ShelfAdapterNew2", WIDTH + "   " + HEIGHT + "  " + mBookList.size());

        this.WIDTH = WIDTH;
        this.HEIGHT = HEIGHT;
        this.mBookList = mBookList;
        ListSize = mBookList.size();
        this.mActivity = mActivity;
    }

    @Override
    public int getCount() {

        return ListSize + 1;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        if (position < ListSize) {
            return mBookList.get(position);
        }
        return null;

    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    int mPosition;

    public void setDeletable(final boolean deletable, TextView btn, int position, final BookshelfFragment.DeleteBook delete) {
        MyToash.Log("refreshBtn q", (mDeleteBtn == null) + "");
        checkedBookList.clear();
        mIsDeletable = deletable;
        mPosition = position;
        mDeleteBtn = btn;
        mDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkedBookList.isEmpty()) {
                    StringBuilder builder = new StringBuilder();
                    for (BaseComic BaseComic : checkedBookList) {
                        builder.append("," + BaseComic.getComic_id());
                        if (BaseComic.getId() != 0) {
                            LitePal.delete(BaseComic.class, BaseComic.getId());
                        } else {
                            LitePal.deleteAll(ComicChapter.class, "comic_id = ?", BaseComic.getComic_id());
                        }
                        LitePal.deleteAllAsync(ComicChapter.class, "comic_id = ?", BaseComic.getComic_id());
                        String filepath = FileManager.getSDCardRoot().concat("Reader/comic/").concat(BaseComic.getComic_id() + "/");
                        FileManager.deleteFile(filepath);
                    }
                    String str = builder.toString();
                    String bookIdArr = str.substring(1);
                    mBookList.removeAll(checkedBookList);
                    checkedBookList.clear();
                    delete.success();
                    if (Utils.isLogin(mActivity)) {
                        deleteBook(bookIdArr);
                    }
                }
            }
        });
        notifyDataSetChanged();


        refreshBtn();
    }

    public void selectAll(boolean selectAll) {
        checkedBookList.clear();
        if (selectAll) {
            checkedBookList.addAll(mBookList);
        }

        refreshBtn();

    }

    public void refreshBtn() {
        MyToash.Log("refreshBtn", (mDeleteBtn == null) + "");
        if (mDeleteBtn == null) {
            return;
        }
        int size = checkedBookList.size();

        if (size == 0) {
            mDeleteBtn.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.lightgray2));
            mDeleteBtn.setTextColor(Color.GRAY);
            mDeleteBtn.setText(String.format(LanguageUtil.getString(mActivity, R.string.noverfragment_shanchushuji2), 0));
        } else {
            mDeleteBtn.setText(String.format(LanguageUtil.getString(mActivity, R.string.noverfragment_shanchushuji2), size));
            mDeleteBtn.setTextColor(Color.WHITE);
            mDeleteBtn.setBackgroundColor(Color.RED);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public View getView(final int position, View contentView, ViewGroup arg2) {

        // TODO Auto-generated method stub
        try {
            ViewHolder viewHolder = null;
            ViewHolder2 viewHolder2 = null;

            if (contentView == null) {
                if (position < ListSize) {
                    contentView = LayoutInflater.from(mActivity).inflate(R.layout.shelfitem, null);
                    viewHolder = new ViewHolder(contentView);


                    FrameLayout.LayoutParams layoutParams2 = (FrameLayout.LayoutParams) viewHolder.shelfitem_img.getLayoutParams();
                    layoutParams2.width = WIDTH;
                    layoutParams2.height = HEIGHT;
                    viewHolder.shelfitem_img.setLayoutParams(layoutParams2);

                    FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) viewHolder.shelfitem_check_container.getLayoutParams();
                    layoutParams.width = WIDTH;
                    layoutParams.height = HEIGHT;
                    viewHolder.shelfitem_check_container.setLayoutParams(layoutParams);

                    contentView.setTag(viewHolder);


                } else {
                    contentView = LayoutInflater.from(mActivity).inflate(R.layout.nover_add, null);
                    viewHolder2 = new ViewHolder2(contentView);
                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) viewHolder2.listview_item_nover_add_layout.getLayoutParams();
                    layoutParams.width = WIDTH;
                    layoutParams.height = HEIGHT;
                    viewHolder2.listview_item_nover_add_layout.setLayoutParams(layoutParams);

                    contentView.setTag(viewHolder2);
                }
            } else {
                if (position < ListSize)
                    viewHolder = (ViewHolder) contentView.getTag();
                else
                    viewHolder2 = (ViewHolder2) contentView.getTag();
            }
            if (position == 0 && shelfitem_img_first == null) {
                if (ListSize > 0) {
                    shelfitem_img_first = viewHolder.shelfitem_img;
                } else {
                    shelfitem_img_first = viewHolder2.listview_item_nover_add_image;

                }
            }
            if (position < ListSize) {
                final BaseComic BaseComic = mBookList.get(position);
                final ViewHolder finalViewHolder = viewHolder;
                viewHolder.shelfitem_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            if (!checkedBookList.contains(BaseComic)) {
                                checkedBookList.add(BaseComic);
                            }
                            finalViewHolder.shelfitem_check_container.setBackgroundColor(Color.parseColor("#00ffffff"));
                        } else {
                            finalViewHolder.shelfitem_check_container.setBackgroundColor(Color.parseColor("#7fffffff"));
                            if (checkedBookList.contains(BaseComic)) {
                                checkedBookList.remove(BaseComic);
                            }

                        }
                        refreshBtn();
                    }
                });


                viewHolder.shelfitem_title.setText(BaseComic.getName());

                if (BaseComic.getRecentChapter() != 0 && (BaseComic.getRecentChapter() != BaseComic.getTotal_chapters())) {
                    viewHolder.shelfitem_top_newchapter.setVisibility(View.VISIBLE);
                    viewHolder.shelfitem_top_newchapter.setText(BaseComic.getRecentChapter() + "");
                } else {
                    viewHolder.shelfitem_top_newchapter.setVisibility(View.GONE);
                }
                ImageLoader.getInstance().displayImage(BaseComic.getVertical_cover(), viewHolder.shelfitem_img, ReaderApplication.getOptions());
                if (mIsDeletable) {
                    viewHolder.shelfitem_check.setVisibility(View.VISIBLE);
                    viewHolder.shelfitem_check_container.setVisibility(View.VISIBLE);
                    final ViewHolder finalViewHolder1 = viewHolder;
                    viewHolder.shelfitem_check_container.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finalViewHolder1.shelfitem_check.toggle();
                        }
                    });
                    if (checkedBookList.contains(BaseComic)) {
                        viewHolder.shelfitem_check.setChecked(true);
                    } else {
                        viewHolder.shelfitem_check.setChecked(false);
                    }


                } else {
                    viewHolder.shelfitem_check.setVisibility(View.GONE);
                    viewHolder.shelfitem_check_container.setVisibility(View.GONE);
                }
            } else {
                viewHolder2.listview_item_nover_add_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!mIsDeletable) {
                            EventBus.getDefault().post(new ToStore(2));
                        }
                    }
                });
                if (mIsDeletable) {


                    viewHolder2.listview_item_nover_add_image.setImageResource(0);
                    viewHolder2.listview_item_nover_add_layout.setBackgroundColor(0);
                } else {
                    viewHolder2.listview_item_nover_add_image.setImageResource(R.mipmap.icon_addbook);
                    viewHolder2.listview_item_nover_add_layout.setBackgroundColor(-526087);
                }

            }
        } catch (Exception e) {
        }
        return contentView;
    }


    /**
     * 删除书架书籍
     */
    public void deleteBook(final String bookIdArr) {
        ReaderParams params = new ReaderParams(mActivity);
        params.putExtraParams("comic_id", bookIdArr);
        String json = params.generateParamsJson();
        HttpUtils.getInstance(mActivity).sendRequestRequestParams3(ComicConfig.COMIC_SHELF_DEL, json, false, new HttpUtils.ResponseListener() {
                    @Override
                    public void onResponse(final String result) {
                        // deleteLocalBook(bookIdArr, deleteBook);
                    }

                    @Override
                    public void onErrorResponse(String ex) {
                        //  Toast.makeText(mActivity, " 删除失败", Toast.LENGTH_LONG).show();
                        //deleteLocalBook(bookIdArr, deleteBook);

                    }
                }

        );


    }


    public void setDeletable(boolean deletable) {
        checkedBookList.clear();
        mIsDeletable = deletable;
    }


    public boolean isDeletable() {
        return mIsDeletable;
    }

    public class ViewHolder {
        @BindView(R2.id.shelfitem_check)
        public CheckBox shelfitem_check;
        @BindView(R2.id.shelfitem_title)
        public TextView shelfitem_title;
        @BindView(R2.id.shelfitem_img)
        public ImageView shelfitem_img;
        @BindView(R2.id.shelfitem_layout)
        public LinearLayout shelfitem_layout;
        @BindView(R2.id.shelfitem_check_container)
        public View shelfitem_check_container;
        @BindView(R2.id.shelfitem_top_newchapter)
        public TextView shelfitem_top_newchapter;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

    }

    public class ViewHolder2 {
        @BindView(R2.id.listview_item_nover_add_image)
        public ImageView listview_item_nover_add_image;
        @BindView(R2.id.listview_item_nover_add_layout)
        public RelativeLayout listview_item_nover_add_layout;

        public ViewHolder2(View view) {
            ButterKnife.bind(this, view);
        }
    }


}
