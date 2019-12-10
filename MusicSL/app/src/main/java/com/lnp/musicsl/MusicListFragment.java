package com.lnp.musicsl;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MusicListFragment extends Fragment {
    private RecyclerView mCrimeRecyclerView;
    private MusicAdapter mAdapter;
    private boolean mSubtitleVisible;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.music_list_fragment, container, false);

        mCrimeRecyclerView = view.findViewById(R.id.music_recyler_view);
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
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

    private class MusicHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mNameTextView;
        private TextView mDateTextView;

        private Song mSong;

        public MusicHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            mNameTextView = itemView.findViewById(R.id.list_item_music_title_text_view);
            mDateTextView = itemView.findViewById(R.id.list_item_music_auth_text_view);
        }

        public void bindMusic(Song song) {
            mSong = song;
            mNameTextView.setText(mSong.getSong());
            mDateTextView.setText(mSong.getSinger());
        }

        @Override
        public void onClick(View v) {
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
            holder.bindMusic(song);
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
