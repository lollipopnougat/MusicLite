package com.lnp.musicsl;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import pub.devrel.easypermissions.EasyPermissions;

import java.util.List;

public class MainFragment extends Fragment implements EasyPermissions.PermissionCallbacks {

    private ViewPager mViewPager;
    private CardPagerAdapter mCardAdapter;
    private ShadowTransformer mCardShadowTransformer;
    private Toolbar mtoolbar;
    private List<Song> musicData;
    private int CODE = 1;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        String[] perms = {Manifest.permission.READ_EXTERNAL_STORAGE};
        View view = inflater.inflate(R.layout.drawer_layout, container, false);
        mViewPager = view.findViewById(R.id.view_pager);
        mtoolbar = view.findViewById(R.id.tool_bar);
        mtoolbar.setNavigationOnClickListener(v -> {
            Intent intent = MusicListActivity.newIntent(getActivity());
            startActivity(intent);
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

    private void updateViewPager() {
        MusicUtils musicUtils = MusicUtils.getMusicUtils(getContext());
        musicData = musicUtils.getMusicData();
        Toast.makeText(getContext(),Integer.toString(musicData.size()),Toast.LENGTH_SHORT).show();
        if(musicData.size() != 0) {
            mCardAdapter = new CardPagerAdapter();
            for (Song song : musicData) {
                mCardAdapter.addSongCard(new SongCard(song));
            }
            mCardShadowTransformer = new ShadowTransformer(mViewPager, mCardAdapter);
            mCardShadowTransformer.enableScaling(true);
            mViewPager.setAdapter(mCardAdapter);
            mViewPager.setPageTransformer(false, mCardShadowTransformer);
            mViewPager.setOffscreenPageLimit(3);
        }
        else {
            Toast.makeText(getContext(),"没有歌曲！！",Toast.LENGTH_SHORT).show();
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
