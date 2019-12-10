package com.lnp.musicsl;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.*;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.annotation.RequiresApi;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import pub.devrel.easypermissions.EasyPermissions;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class MainFragment extends Fragment implements EasyPermissions.PermissionCallbacks {

    private ViewPager mViewPager;
    private CardPagerAdapter mCardAdapter;
    private ShadowTransformer mCardShadowTransformer;
    private Toolbar mtoolbar;
    private List<Song> musicData;
    private static SeekBar mSeekBar;
    private ImageButton imageButton;
    private TextView musicListBtn;
    private TextView aboutBtn;
    private DrawerLayout drawerLayout;
    private static TextView mTextView;
    private MusicConnector conn = new MusicConnector();
    private int CODE = 1;
    private static final int SYNC_TIME = 1;
    private static final int INIT_SEEK_BAR = 2;
    private static final int FINE = 3;
    private static final int START = 4;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        String[] perms = {Manifest.permission.READ_EXTERNAL_STORAGE};
        View view = inflater.inflate(R.layout.drawer_layout, container, false);
        mViewPager = view.findViewById(R.id.view_pager);
        mtoolbar = view.findViewById(R.id.tool_bar);
        mSeekBar = view.findViewById(R.id.seek_bar);
        drawerLayout = view.findViewById(R.id.drawer_layout);
        imageButton = view.findViewById(R.id.music_mode);
        aboutBtn = view.findViewById(R.id.music_about_btn);
        aboutBtn.setOnClickListener(v->{
            Toast.makeText(getActivity(),"啊什么也没有",Toast.LENGTH_SHORT).show();
        });
        musicListBtn = view.findViewById(R.id.music_list_btn);
        musicListBtn.setOnClickListener(v -> {
            Intent intent = MusicListActivity.newIntent(getContext());
            startActivity(intent);
        });
        imageButton.setOnClickListener(v -> {
            Intent intent = MusicListActivity.newIntent(getContext());
            startActivity(intent);
        });
        mTextView = view.findViewById(R.id.current_time);
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                Log.i("测试","当前ProcessBar" + b);
                if(b) musicBinder.seekTo(mSeekBar.getProgress() * 1000);
                //musicBinder.seekTo(mSeekBar.getProgress() * 1000);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        Intent inten = MusicService.newIntent(getActivity());
        //getContext().startService(intent);
        conn = new MusicConnector();
        getContext().bindService(inten, conn, Context.BIND_AUTO_CREATE);
        mtoolbar.setNavigationOnClickListener(v -> {
            drawerLayout.openDrawer(Gravity.LEFT);
            //Intent intent = MusicListActivity.newIntent(getActivity());
            //startActivity(intent);
        });
        if (EasyPermissions.hasPermissions(getContext(), perms)) {
            updateViewPager();
        } else {
            EasyPermissions.requestPermissions(getActivity(), "请求，请求！", CODE, perms);
        }

        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (conn != null) getContext().unbindService(conn);
    }

    private void updateViewPager() {
        MusicUtils musicUtils = MusicUtils.getMusicUtils(getContext());
        musicData = musicUtils.getMusicData();
        Toast.makeText(getContext(), Integer.toString(musicData.size()), Toast.LENGTH_SHORT).show();
        if (musicData.size() != 0) {
            mCardAdapter = new CardPagerAdapter();
            for (Song song : musicData) {
                mCardAdapter.addSongCard(new SongCard(song));
            }
            mCardShadowTransformer = new ShadowTransformer(mViewPager, mCardAdapter);
            mCardShadowTransformer.enableScaling(true);
            mViewPager.setAdapter(mCardAdapter);
            mViewPager.setPageTransformer(false, mCardShadowTransformer);
            mViewPager.setOffscreenPageLimit(3);
        } else {
            Toast.makeText(getContext(), "没有歌曲！！", Toast.LENGTH_SHORT).show();
        }

    }

    @SuppressLint("HandlerLeak")
    public static Handler handler = new Handler() {
        private String fullTime;

        @SuppressLint("DefaultLocale")
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == SYNC_TIME) {
                int time = (int) msg.obj;
                int second = time / 1000;
                mSeekBar.setProgress(second);
                mSeekBar.animate();
                String currTime = String.format("%d : %d ", TimeUnit.MILLISECONDS.toMinutes(time),
                        TimeUnit.MILLISECONDS.toSeconds(time) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(time)));
                mTextView.setText(currTime + " / "  + fullTime);

            } else if (msg.what == INIT_SEEK_BAR) {
                int time = (int) msg.obj;
                mSeekBar.setMax(time / 1000);
                fullTime = String.format("%d : %d ", TimeUnit.MILLISECONDS.toMinutes(time),
                        TimeUnit.MILLISECONDS.toSeconds(time) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(time)));
                mTextView.setText("0:0 /" + " " + fullTime);

            } else if (msg.what == FINE) {

            }
            else if (msg.what == START) {
                new SyncSeekBar().start();
            }

        }


    };

    public static MusicService.MusicBinder musicBinder;

    private class MusicConnector implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            musicBinder = ((MusicService.MusicBinder) iBinder);
            Log.i("测试", "播放服务已经启动");
            Toast.makeText(getContext(), "播放服务启动成功", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            musicBinder = null;
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> list) {
        // Some permissions have been granted
        // ...
        if (requestCode == CODE) {
            updateViewPager();
        }


    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> list) {
        // Some permissions have been denied
        // ...
        getActivity().finish();
    }
}
