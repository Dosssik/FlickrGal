package com.fed.FlickrGal;

import android.app.Fragment;

import com.fed.FlickrGal.Utils.SingleFragmentActivity;

public class MainActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return GalleryFragment.newInstance();
    }

}
