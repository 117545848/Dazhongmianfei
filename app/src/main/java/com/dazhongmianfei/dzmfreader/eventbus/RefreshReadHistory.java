package com.dazhongmianfei.dzmfreader.eventbus;

/**
 * Created by wudeyan on 2018/8/8.
 */
public class RefreshReadHistory {
    boolean PRODUCT;

    public RefreshReadHistory(boolean PRODUCT) {
        this.PRODUCT = PRODUCT;
    }

    public boolean isPRODUCT() {
        return PRODUCT;
    }

    public void setPRODUCT(boolean PRODUCT) {
        this.PRODUCT = PRODUCT;
    }
}
