package com.dazhongmianfei.dzmfreader.view;

/**
 * 滑动接口
 * Created by wdyan on 2016/3/3.
 */
public interface Pullable {
    //声明方法
    boolean canPullDown();

    boolean canPullUp();
}