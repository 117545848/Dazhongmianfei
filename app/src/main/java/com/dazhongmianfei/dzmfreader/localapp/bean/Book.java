package com.dazhongmianfei.dzmfreader.localapp.bean;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.dazhongmianfei.dzmfreader.localapp.common.TaskManager;

import org.litepal.crud.LitePalSupport;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;

/**
 * Created by will on 2016/10/29.
 */

public class Book  extends LitePalSupport implements Serializable {
    private String name,path;
    public Book(){}
    public Book(String name, String path){
        this.name = name;
        this.path = path;
    }

    public Book setBookName(String name){
        this.name = name;
        return this;
    }
    public String getBookName(){
        return  name;
    }

    public Book setPath(String path){
        this.path = path;
        return this;
    }
    public String getPath(){
        return path;
    }






    @Override
    public boolean equals(Object o) {
        if(o instanceof Book){
            Book book = (Book) o;
            return book.getPath().equals(this.path);

        } else{
            return super.equals(o);
        }
    }


}
