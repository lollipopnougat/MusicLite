package com.lnp.musicsl;

import android.content.Context;
import android.content.Intent;
import androidx.fragment.app.Fragment;

public class MusicListActivity extends  SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new MusicListFragment();
    }

    public static Intent newIntent(Context packageContext) {
        return new Intent(packageContext, MusicListActivity.class);
    }
}
