package com.dazhongmianfei.dzmfreader.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dazhongmianfei.dzmfreader.R;
import com.dazhongmianfei.dzmfreader.bean.TaskCenter;

import java.util.List;

/**
 * Created by scb on 2018/10/28.
 */

public class TaskCenterAdapter extends BaseAdapter {

    private List<TaskCenter.TaskCenter2.Taskcenter> taskCenter2s;
    Activity activity;
    LayoutInflater layoutInflater;
    int othertask;
    String title1,title2;

    public TaskCenterAdapter(List<TaskCenter.TaskCenter2.Taskcenter> taskCenter2s, Activity activity, int othertask, String title1, String title2) {
        this.taskCenter2s = taskCenter2s;
        this.layoutInflater = LayoutInflater.from(activity);
        this.othertask = othertask;
        this.title1 = title1;
        this.title2 = title2;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return taskCenter2s.size();
    }

    @Override
    public TaskCenter.TaskCenter2.Taskcenter getItem(int i) {
        return taskCenter2s.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = layoutInflater.inflate(R.layout.listview_item_taskcenter, null);

        }
        TaskCenter.TaskCenter2.Taskcenter taskCenter2 = getItem(i);
        LinearLayout listview_taskcenter_tasktype_layout = view.findViewById(R.id.listview_taskcenter_tasktype_layout);
        TextView listview_taskcenter_tasktype = view.findViewById(R.id.listview_taskcenter_tasktype);
        if (i == 0 || i == othertask) {
            if (i == 0) {
                listview_taskcenter_tasktype.setText(title1);
            }
            else {
                listview_taskcenter_tasktype.setText(title2);
            }
            listview_taskcenter_tasktype_layout.setVisibility(View.VISIBLE);
        } else {
            listview_taskcenter_tasktype_layout.setVisibility(View.GONE);
        }
        TextView listview_taskcenter_task_labe = view.findViewById(R.id.listview_taskcenter_task_labe);
        TextView listview_taskcenter_award = view.findViewById(R.id.listview_taskcenter_award);
        TextView listview_taskcenter_task_desc = view.findViewById(R.id.listview_taskcenter_task_desc);
        TextView listview_taskcenter_status = view.findViewById(R.id.listview_taskcenter_status);
        listview_taskcenter_task_labe.setText(taskCenter2.getTask_label());
        listview_taskcenter_award.setText(taskCenter2.getTask_award());
        listview_taskcenter_task_desc.setText(taskCenter2.getTask_desc());
        if (taskCenter2.getTask_state() == 0) {
            listview_taskcenter_status.setText("去完成");
           // listview_taskcenter_status.setTextColor(activity.getResources().getColor(R.color.gray));
            listview_taskcenter_status.setBackgroundResource(R.drawable.shape_taskcenter_orenge);

        } else {
            listview_taskcenter_status.setText("已完成");
         //   listview_taskcenter_status.setTextColor(activity.getResources().getColor(R.color.white));
            listview_taskcenter_status.setBackgroundResource(R.drawable.shape_taskcenter_gary);
        }

        return view;
    }


}
