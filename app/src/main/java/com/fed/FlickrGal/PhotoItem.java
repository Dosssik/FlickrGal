package com.fed.FlickrGal;

import com.google.gson.annotations.SerializedName;

/**
 * Created by f on 24.07.2017.
 */

public class PhotoItem {

    @SerializedName("title")
    private String mTitle;
    @SerializedName("id")
    private String mId;

    private String mUrl;



    @Override
    public String toString() {
        return mTitle;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }
}
