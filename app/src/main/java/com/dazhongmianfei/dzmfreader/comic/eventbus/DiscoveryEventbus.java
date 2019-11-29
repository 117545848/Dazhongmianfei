package com.dazhongmianfei.dzmfreader.comic.eventbus;

public class DiscoveryEventbus {
    public  boolean IS_NOTOP;
    public  int Y;

    public DiscoveryEventbus(boolean IS_NOTOP, int y) {
        this.IS_NOTOP = IS_NOTOP;
        Y = y;
    }

    public boolean isIS_NOTOP() {
        return IS_NOTOP;
    }

    public void setIS_NOTOP(boolean IS_NOTOP) {
        this.IS_NOTOP = IS_NOTOP;
    }

    public int getY() {
        return Y;
    }

    public void setY(int y) {
        Y = y;
    }
}
