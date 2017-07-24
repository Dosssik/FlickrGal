package com.fed.FlickrGal;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.IOException;

/**
 * Created by f on 24.07.2017.
 */

public class GalleryFragment extends Fragment {

    public static final String TAG = "GalleryFragment";
    private RecyclerView mPhotoRecycler;

    public static GalleryFragment newInstance() {
        return new GalleryFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        new FetchItemTask().execute();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_gallery, container, false);

        mPhotoRecycler = v.findViewById(R.id.fragment_gallery_recycler);
        mPhotoRecycler.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        return v;
    }

    private class FetchItemTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            new FlickrFetcher().fetchItems();
            return null;
        }
    }
}
