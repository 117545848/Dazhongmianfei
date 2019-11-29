package com.dazhongmianfei.dzmfreader.comic.view;

import com.dazhongmianfei.dzmfreader.view.ZoomListView;

import java.util.EventListener;

public interface SwipeListener extends EventListener {
    public void OnAction(ZoomListView.Action action);
}
