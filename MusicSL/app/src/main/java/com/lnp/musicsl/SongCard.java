package com.lnp.musicsl;

import android.graphics.Bitmap;

public class SongCard {
    private String songName;
    private String author;
    private String path;
    private int duration;

    public SongCard(Song song) {
        songName = song.getFileName();
        author = song.getSinger();
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
