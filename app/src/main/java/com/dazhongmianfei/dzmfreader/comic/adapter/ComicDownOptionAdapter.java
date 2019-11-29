package com.dazhongmianfei.dzmfreader.comic.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dazhongmianfei.dzmfreader.R;
import com.dazhongmianfei.dzmfreader.R2;
import com.dazhongmianfei.dzmfreader.comic.been.ComicChapter;
import com.dazhongmianfei.dzmfreader.utils.LanguageUtil;
import com.dazhongmianfei.dzmfreader.utils.ShareUitls;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ComicDownOptionAdapter extends BaseAdapter {
    Activity activity;
    List<ComicChapter> comicDownOptionList;
    public List<ComicChapter> comicDownOptionListChooseDwn;
    LayoutInflater layoutInflater;
    TextView activity_comicdown_choose_count, activity_comicdown_down;
    public int Size;
    boolean flag;//


    public ComicDownOptionAdapter(Activity activity, List<ComicChapter> comicDownOptionList, TextView activity_comicdown_choose_count, TextView activity_comicdown_down, boolean flag) {
        this.activity = activity;
        this.flag = flag;

        this.activity_comicdown_choose_count = activity_comicdown_choose_count;
        this.activity_comicdown_down = activity_comicdown_down;
        this.comicDownOptionList = comicDownOptionList;
        this.layoutInflater = LayoutInflater.from(activity);
        Size = comicDownOptionList.size();
        comicDownOptionListChooseDwn = new ArrayList<>();
        refreshBtn(0);
    }

    @Override
    public int getCount() {
        return Size;
    }

    @Override
    public ComicChapter getItem(int position) {
        return comicDownOptionList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_comicdownoption, null, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        ComicChapter comicDownOption = getItem(position);


        viewHolder.item_comicdownoption_text.setText((comicDownOption.display_label==null?"":comicDownOption.display_label)+ "");


        //  File file=new File(FileManager.getManhuaSDCardRoot().concat(comicDownOption.comic_id + "/").concat(comicDownOption.chapter_id ));
        int status = 1;
        if (!flag) {
            status = ShareUitls.getComicDownStatus(activity, comicDownOption.chapter_id, 0);
        }
        if (status == 0||status==3) {
            viewHolder.item_comicdownoption_downstatus.setVisibility(View.GONE);
            if (comicDownOptionListChooseDwn.contains(comicDownOption)) {
                viewHolder.item_comicdownoption_text.setBackgroundResource(R.drawable.shape_comicdownoption_maincolor);
                viewHolder.item_comicdownoption_text.setTextColor(Color.WHITE);
                viewHolder.item_comicdownoption_vip.setImageResource(R.mipmap.lock_white);


            } else {
                viewHolder.item_comicdownoption_text.setBackgroundResource(R.drawable.shape_comicdownoption_white);
                viewHolder.item_comicdownoption_text.setTextColor(Color.BLACK);
                viewHolder.item_comicdownoption_vip.setImageResource(R.mipmap.lock_orange);
            }
            if (comicDownOption.is_preview == 0) {
                viewHolder.item_comicdownoption_vip.setVisibility(View.GONE);
            } else {
                viewHolder.item_comicdownoption_vip.setVisibility(View.VISIBLE);
            }

            if (status == 3) {
                viewHolder.item_comicdownoption_downstatus.setVisibility(View.VISIBLE);
                viewHolder.item_comicdownoption_downstatus.setText(LanguageUtil.getString(activity, R.string.ComicDownActivity_downfail));
            } else {
                viewHolder.item_comicdownoption_downstatus.setVisibility(View.GONE);

            }
        } else {
            viewHolder.item_comicdownoption_text.setBackgroundResource(R.drawable.shape_comicdownoption_gray);
            viewHolder.item_comicdownoption_text.setTextColor(Color.BLACK);
            viewHolder.item_comicdownoption_downstatus.setVisibility(View.VISIBLE);
            if (status == 2) {
                viewHolder.item_comicdownoption_downstatus.setText(LanguageUtil.getString(activity, R.string.ComicDownActivity_downing));
            } else {
                viewHolder.item_comicdownoption_downstatus.setText(LanguageUtil.getString(activity, R.string.ComicDownActivity_local));
            }
        }
        viewHolder.item_comicdownoption_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag) {
                    if (!comicDownOptionListChooseDwn.contains(comicDownOption)) {
                        comicDownOptionListChooseDwn.add(comicDownOption);
                        viewHolder.item_comicdownoption_text.setBackgroundResource(R.drawable.shape_comicdownoption_maincolor);
                        viewHolder.item_comicdownoption_text.setTextColor(Color.WHITE);
                    } else {
                        comicDownOptionListChooseDwn.remove(comicDownOption);
                        viewHolder.item_comicdownoption_text.setBackgroundResource(R.drawable.shape_comicdownoption_white);
                        viewHolder.item_comicdownoption_text.setTextColor(Color.BLACK);
                    }
                    refreshBtn(comicDownOptionListChooseDwn.size());
                } else {
                    int status = ShareUitls.getComicDownStatus(activity, comicDownOption.chapter_id, 0);
                    if (status == 0||status==3) {
                        if (!comicDownOptionListChooseDwn.contains(comicDownOption)) {
                            comicDownOptionListChooseDwn.add(comicDownOption);
                            viewHolder.item_comicdownoption_text.setBackgroundResource(R.drawable.shape_comicdownoption_maincolor);
                            viewHolder.item_comicdownoption_vip.setImageResource(R.mipmap.lock_white);
                            viewHolder.item_comicdownoption_text.setTextColor(Color.WHITE);
                        } else {
                            comicDownOptionListChooseDwn.remove(comicDownOption);
                            viewHolder.item_comicdownoption_text.setBackgroundResource(R.drawable.shape_comicdownoption_white);
                            viewHolder.item_comicdownoption_text.setTextColor(Color.BLACK);
                            viewHolder.item_comicdownoption_vip.setImageResource(R.mipmap.lock_orange);
                        }
                        refreshBtn(comicDownOptionListChooseDwn.size());
                    }
                }
            }
        });
        return convertView;
    }

    public void selectAll() {

        if (Size != comicDownOptionListChooseDwn.size()) {
            comicDownOptionListChooseDwn.clear();
            if(!flag) {
                for (ComicChapter comicChapter : comicDownOptionList) {
                    int status=ShareUitls.getComicDownStatus(activity, comicChapter.chapter_id, 0);
                    if ( status== 0||status==3) {
                        comicDownOptionListChooseDwn.add(comicChapter);
                    }
                }
            }else
            comicDownOptionListChooseDwn.addAll(comicDownOptionList);


            refreshBtn(Size);
        } else {
            comicDownOptionListChooseDwn.clear();
            refreshBtn(0);
        }
        notifyDataSetChanged();

    }

    public void refreshBtn(int size) {
        if (size == 0) {
            activity_comicdown_down.setClickable(false);
            activity_comicdown_down.setTextColor(Color.GRAY);
        } else {
            activity_comicdown_down.setTextColor(Color.BLACK);
            activity_comicdown_down.setClickable(true);
        }
        activity_comicdown_choose_count.setText(String.format(LanguageUtil.getString(activity, R.string.ComicDownActivity_choosecount), size));
    }

    class ViewHolder {
        @BindView(R2.id.item_comicdownoption_downstatus)
        TextView item_comicdownoption_downstatus;
        @BindView(R2.id.item_comicdownoption_vip)
        ImageView item_comicdownoption_vip;
        @BindView(R2.id.item_comicdownoption_text)
        TextView item_comicdownoption_text;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
