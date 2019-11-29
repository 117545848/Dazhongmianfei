package com.dazhongmianfei.dzmfreader.read.dialog;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ProgressBar;

/**
 * Created by scb on 2018/8/7.
 */
public class AutoProgress {
    int i = 0;
    boolean isPause = false;
    int currentProgress = 0;
    ProgressBar progressBar;
    long time = 5000;
    ProgressCallback callback;
    boolean isStopped = false;
    boolean isStarted = false;
    boolean isFinish = false;
    public static AutoProgress mAutoProgress;

    public static AutoProgress getInstance() {
        if (mAutoProgress == null) {
            mAutoProgress = new AutoProgress();
        }
        return mAutoProgress;
    }

    Handler updateBarHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            currentProgress = msg.arg1;
            System.out.println("----------start handle  " + currentProgress);

            progressBar.setProgress(currentProgress);
            if (currentProgress == time) {
                if (callback != null) {
                    callback.finish();

                }
                currentProgress = 0;
                i = 0;
                isPause = false;
                addTask();
            }
        }
    };


    public void setProgressBar(ProgressBar bar) {
        this.progressBar = bar;
    }

    public void addTask() {
        ProgressTaskManager.getInstance().addQueueTask(new ProgressTask() {
            @Override
            public void doRun() {
                while (currentProgress <= time && !isPause) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    i += 100;
                    Message msg = updateBarHandler.obtainMessage();
                    // 将Message对象的arg1参数的值设置为i
                    msg.arg1 = i; // 用arg1、arg2这两个成员变量传递消息，优点是系统性能消耗较少

                    if (i <= time) {
                        updateBarHandler.sendMessage(msg);
                    }
                }
            }
        });
    }

    public void start(ProgressCallback callback) {
        isStarted = true;
        isStopped = false;
        this.callback = callback;
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setMax((int) time);
        currentProgress = 0;
        i = 0;
        progressBar.setProgress(currentProgress);
        isPause = false;
        addTask();
    }

    public void restart() {
        isStarted = true;
        isStopped = false;
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setMax((int) time);
        currentProgress = 0;
        i = 0;
        progressBar.setProgress(currentProgress);
        isPause = false;
        addTask();
    }

    public void exit() {
        stop();
    }

    public boolean isStarted() {
        return isStarted;
    }

    public void pause() {
        isPause = true;
    }

    public void goStill() {
        isPause = false;
        addTask();
    }

    public void stop() {

        isStopped = true;
        isStarted = false;

        pause();
        ProgressTaskManager.getInstance().onDestroy();
        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }

    }

    public boolean isStop() {
        return isStopped;
    }


    public void setTime(long time) {
        this.time = time;
    }

    public interface ProgressCallback {
        /**
         * 进度条到达最大刻度时回调此方法
         */
        void finish();
    }
}
