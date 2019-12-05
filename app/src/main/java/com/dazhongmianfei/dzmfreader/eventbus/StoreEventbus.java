package com.dazhongmianfei.dzmfreader.eventbus;

public class StoreEventbus {
    public  boolean chooseWho;

    public  int Y;

    public StoreEventbus(boolean chooseWho, int y) {
        this.chooseWho = chooseWho;
        Y = y;
    }

    public boolean isChooseWho() {
        return chooseWho;
    }

    public void setChooseWho(boolean chooseWho) {
        this.chooseWho = chooseWho;
    }

    public int getY() {
        return Y;
    }

    public void setY(int y) {
        Y = y;
    }
}
