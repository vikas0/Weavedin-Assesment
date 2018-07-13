package com.vikas.itunepreview_weavedin.dao;

import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.os.AsyncTask;


import java.util.List;


public class TrackRepository {

    private TrackDao mTrackDao;
    private RecentSearchDao mRecentSearchDao;
    private LiveData<List<Track>> mAllTracks;
    private List<String> mRecentSearches;
    private int NETWORK_PAGE_SIZE = 50;
    Context context;


    public TrackRepository(Context application) {
        this.context = application;
        TrackRoomDatabase db = TrackRoomDatabase.getDatabase(application);
        mTrackDao = db.TrackDao();
        mRecentSearchDao = db.RecentSearchDao();
    }
    public void removeFavorite(Track track)
    {
        mTrackDao.delete(track);
    }
    public void removerecent(RecentSearch recentSearch)
    {
        mRecentSearchDao.delete(recentSearch);
    }

 public LiveData<List<Track>> getAllTracks() {
     mAllTracks = mTrackDao.getAllTracks();

     return mAllTracks;
    }
 public List<String> getAllRecentSearches() {
     mRecentSearches = mRecentSearchDao.getAllRecentSearchesTexts();

     return mRecentSearches;
    }


//    List<Track>  search(String query){
//        VolleyApiClient.getInstance(application).searchTracks(query,new Tr);
//    }

//    private  requestAndSaveData( String query) {
////        if (isRequestInProgress) return
//
////                isRequestInProgress = true
//
//    }



    public void insert (Track Track) {
        new insertAsyncTask( mTrackDao).execute(Track);
    }
    public void insert (RecentSearch recentSearch) {
        new insertSearchAsyncTask( mRecentSearchDao).execute(recentSearch);
    }

    private static class insertAsyncTask extends AsyncTask<Track, Void, Void> {

        private TrackDao mAsyncTaskDao;

        insertAsyncTask(TrackDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Track... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class insertSearchAsyncTask extends AsyncTask<RecentSearch, Void, Void> {

        private RecentSearchDao mAsyncDao;

        insertSearchAsyncTask(RecentSearchDao dao) {
            mAsyncDao = dao;
        }

        @Override
        protected Void doInBackground(final RecentSearch... params) {
            mAsyncDao.insert(params[0]);
            return null;
        }
    }

}
