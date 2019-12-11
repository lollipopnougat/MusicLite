package com.lnp.musicsl;

import android.content.Intent;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.PagerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class CardPagerAdapter extends PagerAdapter implements CardAdapter {
    private static final int SYNC_TIME = 1;
    private static final int INIT_SEEK_BAR = 2;
    private static final int FINE = 3;
    private static final int START = 4;
    private static final int PAUSE = 5;
    private List<CardView> mViews;
    private List<SongCard> mData;
    private float mBaseElevation;
    private int clickWho = -1;
    private TextView pauseBtn;

    public CardPagerAdapter() {
        mData = new ArrayList<>();
        mViews = new ArrayList<>();
    }

    public void addSongCard(SongCard item) {
        mViews.add(null);
        mData.add(item);
    }

    public float getBaseElevation() {
        return mBaseElevation;
    }


    @Override
    public CardView getCardViewAt(int position) {
        if (mViews.size() == 0) throw new RuntimeException("空！");
        return mViews.get(position);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.card_view, container, false);
        container.addView(view);
        bind(mData.get(position), view);
        TextView colorBtn = view.findViewById(R.id.card_song_name);
        CardView cardView = view.findViewById(R.id.card_view);
        cardView.setOnClickListener(v -> {
            if (clickWho != position) {
                if(pauseBtn != null) pauseBtn.setBackgroundResource(R.drawable.ic_card_btm);
                clickWho = position;
            } else {
                pauseBtn = colorBtn;
                if (MainFragment.musicBinder.isPausing()) {
                    colorBtn.setBackgroundResource(R.drawable.ic_card_btm);
                    MainFragment.musicBinder.play();
                } else {
                    MainFragment.musicBinder.pause();
                    colorBtn.setBackgroundResource(R.drawable.ic_card_btm_pause);
                }
                return;
            }
            SongCard mSongCard = mData.get(position);
            if (MainFragment.musicBinder.isPlaying()) {
                MainFragment.musicBinder.pause();
            }
            Message msgf = MainFragment.handler.obtainMessage();
            msgf.what = INIT_SEEK_BAR;
            msgf.obj = mSongCard.getDuration();
            MainFragment.musicBinder.start(mSongCard.getPath());
            MainFragment.handler.sendMessage(msgf);
            MainFragment.musicBinder.play();
            MainFragment.handler.sendEmptyMessage(START);
        });
        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            Toast.makeText(container.getContext(), "喜欢", Toast.LENGTH_SHORT).show();
        });

        if (mBaseElevation == 0) {
            mBaseElevation = cardView.getCardElevation();
        }

        cardView.setMaxCardElevation(mBaseElevation * MAX_ELEVATION_FACTOR);
        mViews.set(position, cardView);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
        mViews.set(position, null);
    }

    private void bind(SongCard item, View view) {
        TextView nameTextView = view.findViewById(R.id.card_song_name);
        TextView authTextView = view.findViewById(R.id.auth_text);
        ImageView imageView = view.findViewById(R.id.card_cover);
        nameTextView.setText(item.getSongName());
        authTextView.setText(item.getAuthor());
        imageView.setImageBitmap(item.getCover());
    }

}