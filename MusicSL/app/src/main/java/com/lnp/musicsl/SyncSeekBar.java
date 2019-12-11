package com.lnp.musicsl;

import android.os.Message;
import android.util.Log;
import android.widget.Toast;

public class SyncSeekBar extends Thread {
    private static final int SYNC_TIME = 1;
    private static final int INIT_SEEK_BAR = 2;
    private static final int FINE = 3;
    private static final int START = 4;

    @Override
    public void run() {
        Log.i("测试", "启动多线程");
        while(!MainFragment.musicBinder.isPlaying());
        Log.i("测试", "music播放:" + MainFragment.musicBinder.isPlaying());
        while (!MainFragment.musicBinder.getIsFine()) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            int nowPosition = MainFragment.musicBinder.getCurrent();
            if ((int)(nowPosition/1000) == (int)(MainFragment.musicBinder.getDuration()/1000)) {
                MainFragment.handler.sendEmptyMessage(FINE);
            }

            Message timeMsg = MainFragment.handler.obtainMessage();
            Log.i("测试","当前进度:"+nowPosition);
            timeMsg.obj = nowPosition;
            timeMsg.what = SYNC_TIME;
            //实时更新进度条
            MainFragment.handler.sendMessage(timeMsg);

        }

    }
}
