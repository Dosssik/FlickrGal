package com.fed.FlickrGal;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by f on 24.07.2017.
 */

public class GalleryFragment extends Fragment {

    public static final String TAG = "GalleryFragment";     // instead create static variable in each fragment, u can
                                                            // use YourFragment.class.getSimpleName() as tag.
                                                            // when u'll have over 20 fragments in app - u'll be tied to create special tag for each one

    private RecyclerView mPhotoRecyclerView;
    private List<GalleryItem> mItems = new ArrayList<>();

    public static GalleryFragment newInstance(){
        return new GalleryFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);        // Take care with that. If it only for avoid recreation of fragment - better to find another solution

        new FetchItemTask().execute();  // it will fire every rotation - better way is to fetch data and persist somewhere.
                                        // when device'll rotated - your can set already downloaded items
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_gallery, container, false);

        mPhotoRecyclerView = v.findViewById(R.id.fragment_gallery_recycler);
        mPhotoRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        setupAdapter();
        return v;
    }

    private void setupAdapter(){
        if (isAdded()) mPhotoRecyclerView.setAdapter(new PhotoAdapter(mItems));
    }

    private class PhotoAdapter extends RecyclerView.Adapter<PhotoHolder> {      // Should be in separated class

        private List<GalleryItem> mGalleryItems;

        public PhotoAdapter(List<GalleryItem> galleryItems) {
            mGalleryItems = galleryItems;
        }

        @Override
        public PhotoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            TextView textView = new TextView(getActivity());
            return new PhotoHolder(textView);
        }

        @Override
        public void onBindViewHolder(PhotoHolder holder, int position) {
            GalleryItem galleryItem = mGalleryItems.get(position);
            holder.bindGalleryItem(galleryItem);
        }

        @Override
        public int getItemCount() {
            return mGalleryItems.size();
        }
    }

    private class PhotoHolder extends RecyclerView.ViewHolder {     // Usually holder is private static class in adapter. Not critical, just FYI
        private TextView mTitleTextView;

        public PhotoHolder(View itemView) {
            super(itemView);
            mTitleTextView = (TextView) itemView;
        }

        public void bindGalleryItem(GalleryItem item) {
            mTitleTextView.setText(item.toString());
        }
    }

    // TODO: When you'll finish Retrofit & Dagger2 learning - remove AsyncTask, and replace it by better solution.
    // as on of popular interview question, be ready to answer "What's wrong with AsyncTask? Why we shouldn't use it?"
    private class FetchItemTask extends AsyncTask<Void, Void, List<GalleryItem>> {
        @Override
        protected List<GalleryItem> doInBackground(Void... params) {
            return new FlickrFetcher().fetchItems();
        }

        @Override
        protected void onPostExecute(List<GalleryItem> galleryItems) {
            mItems = galleryItems;
            setupAdapter();
        }
    }
}
