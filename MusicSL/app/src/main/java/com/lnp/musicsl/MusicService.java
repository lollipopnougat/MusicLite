package com.lnp.musicsl;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import androidx.annotation.Nullable;

public class MusicService extends Service {

    private MediaPlayer mediaPlayer;
    private MusicBinder musicBinder;
    private boolean isSetData;//是否设置资源
    private boolean isPausing;
    private boolean isFine;

    //播放模式
    public static final int SINGLE_CYCLE = 1;     //单曲循环
    public static final int ALL_CYCLE = 2;        //全部循环
    public static final int RANDOM_PLAY = 3;      //随机播放

    private int MODE;

    public static Intent newIntent(Context packageContext) {
        return new Intent(packageContext, MusicService.class);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        //初始化数据
        isSetData = false;
        isPausing = true;
        isFine = false;
        MODE = ALL_CYCLE;
        mediaPlayer = new MediaPlayer();
        musicBinder = new MusicBinder();
    }

    private void playMusic(String path) {
        try {
            //设置资源
            mediaPlayer.reset();
            mediaPlayer.setDataSource(path);
            isSetData = true;

            //播放结束监听
            mediaPlayer.setOnCompletionListener(mp -> {

                switch (MODE) {
                    case SINGLE_CYCLE:
                        //单曲循环
                        mediaPlayer.start();
                        break;

                    case ALL_CYCLE:
                        //全部循环
                        //isSetData = false;
                        //playNextSong();                 //调用MainActivity的 playNextSong方法
                        break;

                    case RANDOM_PLAY:
                        //随机播放
                        //isSetData = false;
                        //playRandomSong();               //这里也是MainActivity的方法
                        break;

                    default:
                }
            });

            //异步缓冲准备及监听
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(mp -> mediaPlayer.start());


        } catch (Exception e) {
            e.printStackTrace();
            isSetData = false;
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        isSetData = false;
    }

    class MusicBinder extends Binder {


        MusicService getService() {
            return MusicService.this;
        }

        //开始播放
        void start(String songUrl) {
            isFine = true;
            playMusic(songUrl);
        }

        //获取资源状态
        boolean isSetData() {
            return isSetData;
        }

        void seekTo(int msc) {
            mediaPlayer.seekTo(msc);
        }

        //获取当前播放状态
        boolean isPlaying() {
            return mediaPlayer.isPlaying();
        }

        boolean isPausing() {
            return isPausing;
        }

        //继续播放
        boolean play() {
            isFine = false;
            if (isSetData) {
                if (!mediaPlayer.isPlaying()) {
                    mediaPlayer.start();
                }
            }
            isPausing = false;
            return mediaPlayer.isPlaying();
        }

        //暂停播放
        boolean pause() {
            if (isSetData) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                }
            }
            isPausing = true;
            return mediaPlayer.isPlaying();
        }

        /**
         * 获取歌曲当前时长位置
         * 如果返回-1，则mediaplayer没有缓冲歌曲
         *
         * @return
         */
        int getCurrent() {
            if (isSetData) {
                return mediaPlayer.getCurrentPosition();
            } else {
                return -1;
            }
        }

        boolean getIsFine() {
            return isFine;
        }


        /**
         * 获取歌曲总时长
         * 如果返回-1，则mediaplayer没有缓冲歌曲
         *
         * @return
         */
        int getDuration() {
            if (isSetData) {
                return mediaPlayer.getDuration();
            } else {
                return -1;
            }
        }

        //获取当前播放模式
        int getMode() {
            return MODE;
        }


        /**
         * 更换播放模式
         * 单曲循环 → 全部循环 → 随机播放 → 单曲循环
         */
        int changeMode() {
            switch (MODE) {
                case SINGLE_CYCLE:
                    MODE = ALL_CYCLE;
                    break;

                case ALL_CYCLE:
                    MODE = RANDOM_PLAY;
                    break;

                case RANDOM_PLAY:
                    MODE = SINGLE_CYCLE;
                    break;

                default:
            }
            return MODE;
        }

    }

    @Override
    public IBinder onBind(Intent intent) {
        return musicBinder;
    }
}



