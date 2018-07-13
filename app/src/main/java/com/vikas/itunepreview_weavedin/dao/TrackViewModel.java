package com.vikas.itunepreview_weavedin.dao;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

/**
 * View Model to keep a reference to the Track repository and
 * an up-to-date list of all Tracks.
 */

public class TrackViewModel extends AndroidViewModel {

    private TrackRepository mRepository;
    // Using LiveData and caching what getAlphabetizedTracks returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    private LiveData<List<Track>> mAllTracks;

    public TrackViewModel(Application application) {
        super(application);
        mRepository = new TrackRepository(application);
        mAllTracks = mRepository.getAllTracks();
    }

 public    LiveData<List<Track>> getAllTracks() { return mAllTracks; }

    public void insert(Track Track) { mRepository.insert(Track); }
}