package com.lnp.musicsl;

import android.graphics.Bitmap;

public class SongCard {
    private String songName;
    private String author;
    private Bitmap cover;
    private String path;
    private int duration;

    public SongCard(Song song) {
        songName = song.getFileName();
        author = song.getSinger();
        cover = song.getCover();
        path = song.getPath();
        duration = song.getDuration();
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Bitmap getCover() {
        return cover;
    }

    public void setCover(Bitmap cover) {
        this.cover = cover;
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
}
