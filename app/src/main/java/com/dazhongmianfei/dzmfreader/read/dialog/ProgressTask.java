package com.dazhongmianfei.dzmfreader.read.dialog;

/**
 * 宝箱倒计时任务<P>
 * Created by scb on 2018/7/17.
 */
public abstract class ProgressTask implements Runnable {

    public ProgressTask() {

    }

    @Override
    public final void run() {
        doRun();
    }

    /**
     * 任务处理
     */
    public abstract void doRun();

}
