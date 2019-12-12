package com.lnp.musicsl;

import android.Manifest;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import static androidx.core.app.ActivityCompat.requestPermissions;

public class MusicUtils {
    private static final String TAG = "MusicUtils";
    private List<Song> list = new ArrayList<>();
    private static MusicUtils obj;

    /**
     * 扫描系统里面的音频文件，返回一个list集合
     */
    public static MusicUtils getMusicUtils(Context context) {
        if (obj == null) obj = new MusicUtils(context);
        return obj;
    }

    private MusicUtils(Context context) {
        int cc = 0;
        
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null,
                MediaStore.Audio.AudioColumns.IS_MUSIC);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Song song = new Song();

                song.setSong(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)));
                song.setSinger(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)));
                song.setPath(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)));
                song.setDuration(cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)));
                song.setSize(cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE)));
                String[] fnames = song.getPath().split("/");
                song.setFileName(fnames[fnames.length - 1]);
                Log.i(TAG, "歌曲:" + song.getSong() + "路径： " + song.getPath() + "时长:" + song.getDuration());


                /*if (song.getSize() > 1000 * 800) {//过滤掉短音频
                    // 分离出歌曲名和歌手
                    if (song.getSong().contains("-")) {
                        String[] str = song.getSong().split("-");
                        song.setSinger(str[0]);
                        song.setSong(str[1]);
                    }
                    list.add(song);
                }*/
                list.add(song);
                //cc++;
                //if (cc == 50) break;
            }
            // 释放资源
            cursor.close();
        }
    }

    public List<Song> getMusicData() {
        return list;
    }


    //格式化时间
    public static String formatTime(int time) {
        if (time / 1000 % 60 < 10) {
            return time / 1000 / 60 + ":0" + time / 1000 % 60;
        } else {
            return time / 1000 / 60 + ":" + time / 1000 % 60;
        }
    }

    public static Bitmap getCover(String mediaUri) {
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(mediaUri);
        byte[] picture = mediaMetadataRetriever.getEmbeddedPicture();
        //ImageView musicCover;
        //musicCover.setImageBitmap(bitmap);
        return BitmapFactory.decodeByteArray(picture, 0, picture.length);
    }

    private void musicPlay(int position) {

    }
}
