package com.fed.FlickrGal;

import android.net.Uri;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by f on 24.07.2017.
 */

public class FlickrFetcher {
//    public static final String REQUEST_URL = "https://api.flickr.com/services/rest/?method=flickr.photos.getRecent&api_key=47fb634bfa4f0d090ec5845b9e9d92d0&format=json&nojsongcallback=1";
//    public static final String BASE_URL = "https://api.flickr.com/services/rest/";

    public static final String TAG = "FlickrFetcher";
    public static final String API_KEY = "47fb634bfa4f0d090ec5845b9e9d92d0";


    public byte[] getUrlBytes(String urlSpec) throws IOException {
        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(connection.getResponseMessage() + ": with " + urlSpec);
            }
            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            return out.toByteArray();
        } finally {
            connection.disconnect();
        }
    }

    public String getUrlString(String urlSpec) throws IOException {
        return new String(getUrlBytes(urlSpec));
    }


    public ArrayList<PhotoItem> fetchItems() {
        ArrayList<PhotoItem> photos = new ArrayList<>();
//        Gson gson = new GsonBuilder().create();

        try {
            String url = Uri.parse("https://api.flickr.com/services/rest/")
                    .buildUpon()
                    .appendQueryParameter("method", "flickr.photos.getRecent")
                    .appendQueryParameter("api_key", API_KEY)
                    .appendQueryParameter("format", "json")
                    .appendQueryParameter("nojsoncallback", "1")
                    .appendQueryParameter("extras", "url_s")
                    .build()
                    .toString();
            String jsonString = getUrlString(url);
            JSONObject jsonBody = new JSONObject(jsonString);
            parseItems(photos, jsonBody);

//            ResponseItem resp;
//            resp = gson.fromJson(jsonString, ResponseItem.class);
//            ArrayList<PhotoItem> respL = resp.getPhotoList();

            Log.i(TAG, "Received JSON: " + jsonString);
            Log.i(TAG, "Received ITEMS: " + photos);
//            Log.i(TAG, "Received RESPONCE: " + resp);
//            Log.i(TAG, "Received RESPONCE_PAGE: " + respL);

        } catch (IOException ioe) {
            Log.e(TAG, "Failed to fetch items", ioe);
        } catch (JSONException e) {
            Log.e(TAG, "Failed with GSON");
        }
        return photos;
    }

    private void parseItems(List<PhotoItem> items, JSONObject jsonBody) throws IOException, JSONException {
        JSONObject photosJsonObject = jsonBody.getJSONObject("photos");
        JSONArray photoJsonArray = photosJsonObject.getJSONArray("photo");
        for (int i = 0; i < photoJsonArray.length(); i++) {
            JSONObject photoJsonObject = photoJsonArray.getJSONObject(i);
            PhotoItem item = new PhotoItem();
            item.setId(photoJsonObject.getString("id"));
            item.setTitle(photoJsonObject.getString("title"));
            if (!photoJsonObject.has("url_s")) {
                continue;
            }
            item.setUrl(photoJsonObject.getString("url_s"));
            items.add(item);
        }
    }

//    private class ResponseItem {
//        @SerializedName("page")
//        private String page;
//        @SerializedName("pages")
//        private String pages;
//        @SerializedName("perpage")
//        private String perpage;
//        @SerializedName("total")
//        private String total;
//        @SerializedName("photo")
//        ArrayList<PhotoItem> photo;
//
//        private ArrayList<PhotoItem> getPhotoList(){
//            return photo;
//        }
//
//        private String getPage(){return page;}
//    }
}
