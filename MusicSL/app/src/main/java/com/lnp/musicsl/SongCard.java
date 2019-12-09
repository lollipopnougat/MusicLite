package com.lnp.musicsl;

import android.graphics.Bitmap;

public class SongCard {
    private String songName;
    private String author;
    private Bitmap cover;

    public SongCard(Song song) {
        songName = song.getSong();
        author = song.getSinger();
        cover = song.getCover();
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
}
