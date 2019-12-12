package com.lnp.musicsl;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import java.util.List;

public class MusicListFragment extends Fragment {
    private static final int SYNC_TIME = 1;
    private static final int INIT_SEEK_BAR = 2;
    private static final int FINE = 3;
    private static final int START = 4;
    private static final int PAUSE = 5;
    private RecyclerView mCrimeRecyclerView;
    private MusicAdapter mAdapter;
    private Toolbar toobar;
    //private MusicConnector conn = new MusicConnector();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        MainActivity.setStatusBarColor(getActivity(), Color.rgb(0,120,133));
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.music_list_fragment, container, false);
        toobar = view.findViewById(R.id.list_tool_bar);
        toobar.setNavigationOnClickListener(v -> {
            getActivity().finish();
        });
        mCrimeRecyclerView = view.findViewById(R.id.music_recyler_view);
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();

//        Intent intent = MusicService.newIntent(getActivity());
//        //getContext().startService(intent);
//        conn = new MusicConnector();
//        getContext().bindService(intent, conn, Context.BIND_AUTO_CREATE);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //if(conn != null) getContext().unbindService(conn);
    }

    private void updateUI() {
        MusicUtils musicUtils = MusicUtils.getMusicUtils(getContext());
        List<Song> songs = musicUtils.getMusicData();

        if (mAdapter == null) {
            mAdapter = new MusicAdapter(songs);
            mCrimeRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setMusics(songs);
            mAdapter.notifyDataSetChanged();
        }
    }
    /*private MusicService.MusicBinder musicBinder;

    private class MusicConnector implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            musicBinder = ((MusicService.MusicBinder) iBinder);
            Log.i("测试","服务已经启动");
            Toast.makeText(getContext(),"服务启动成功",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            musicBinder = null;
        }
    }*/

    private class MusicHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mNameTextView;
        private TextView mDateTextView;
        private int position;
        private Song mSong;

        public MusicHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            mNameTextView = itemView.findViewById(R.id.list_item_music_title_text_view);
            mDateTextView = itemView.findViewById(R.id.list_item_music_auth_text_view);
        }

        public void bindMusic(Song song,int p) {
            mSong = song;
            position = p;
            mNameTextView.setText(mSong.getFileName());
            mDateTextView.setText(mSong.getSinger());
        }


        @Override
        public void onClick(View v) {
//            Intent intent = MusicService.newIntent(getActivity());
            //getContext().startService(intent);
//            conn = new MusicConnector();
//            getContext().bindService(intent, conn, Context.BIND_AUTO_CREATE);
            //musicBinder.start(mSong.getPath());
            //musicBinder.play();
            if(MainFragment.musicBinder.isPlaying()) {
                MainFragment.musicBinder.pause();
            }
            Message msgf = MainFragment.handler.obtainMessage();
            msgf.what = INIT_SEEK_BAR;
            msgf.obj = mSong.getDuration();
            msgf.arg1 = position;
            MainFragment.musicBinder.start(mSong.getPath());
            MainFragment.handler.sendMessage(msgf);
            MainFragment.musicBinder.play();
            CardPagerAdapter.playingIndex = position;
            MainFragment.handler.sendEmptyMessage(START);
            //new SyncSeekBar().start();
            //Intent intent = MusicPagerActivity.newIntent(getActivity(), mMusic.getId());
            //startActivity(intent);
        }


    }

    private class MusicAdapter extends RecyclerView.Adapter<MusicHolder> {

        private List<Song> mSongs;

        public MusicAdapter(List<Song> Songs) {
            mSongs = Songs;
        }

        @NonNull
        @Override
        public MusicHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_music, parent, false);
            return new MusicHolder(view);
        }

        @Override
        public void onBindViewHolder(MusicHolder holder, int position) {
            Song song = mSongs.get(position);
            holder.bindMusic(song,position);
        }

        @Override
        public int getItemCount() {
            return mSongs.size();
        }

        public void setMusics(List<Song> songs) {
            mSongs = songs;
        }
    }
}
