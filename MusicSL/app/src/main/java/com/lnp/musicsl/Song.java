package com.lnp.musicsl;


import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;


public class Song {

    private String singer = "佚名";

    private String song = "未标题";

    private String fileName;

    private String path;


    private int duration;

    private long size;

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        Log.i("歌手名字是",singer);
        if (!singer.equals("<unknown>")) this.singer = singer;
    }

    public String getSong() {
        return song;
    }

    public void setSong(String song) {
        if (song != null) this.song = song;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

}

