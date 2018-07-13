package com.vikas.itunepreview_weavedin.api;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.android.codelabs.paging.api.TrackSearchResponse;
import com.google.gson.Gson;

public class VolleyApiClient {
    private static VolleyApiClient mInstance;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private static Context mCtx;

    private VolleyApiClient(Context context) {
        mCtx = context;
        mRequestQueue = getRequestQueue();

        mImageLoader = new ImageLoader(mRequestQueue,
                new ImageLoader.ImageCache() {
                    private final LruCache<String, Bitmap>
                            cache = new LruCache<String, Bitmap>(20);

                    @Override
                    public Bitmap getBitmap(String url) {
                        return cache.get(url);
                    }

                    @Override
                    public void putBitmap(String url, Bitmap bitmap) {
                        cache.put(url, bitmap);
                    }
                });
    }

    public static synchronized VolleyApiClient getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new VolleyApiClient(context.getApplicationContext());
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    public ImageLoader getImageLoader() {
        return mImageLoader;
    }
    public interface TrackRequestListner{
        void onSuccess(TrackSearchResponse response);
        void onError(String error);
    }
     public void searchTracks(String query, final TrackRequestListner listner)
    {
        String  url = "https://itunes.apple.com/search?media=music&term="+query.replace(" ","+");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        TrackSearchResponse trackSearchResponse = new Gson().fromJson(response,TrackSearchResponse.class);
                       listner.onSuccess(trackSearchResponse);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                          listner.onError(error.getMessage());
                    }
                });

// Access the RequestQueue through your singleton class.
      addToRequestQueue(stringRequest);
    }
}