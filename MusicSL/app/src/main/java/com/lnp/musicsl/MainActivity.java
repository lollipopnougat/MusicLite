package com.lnp.musicsl;

import android.os.Bundle;
import androidx.fragment.app.Fragment;


public class MainActivity extends SingleFragmentActivity {

    /*@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_layout);
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

    }





    @Override
    protected Fragment createFragment() {
        return new MainFragment();
    }
}
