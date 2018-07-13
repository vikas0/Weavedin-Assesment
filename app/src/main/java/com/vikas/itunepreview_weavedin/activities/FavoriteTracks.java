package com.vikas.itunepreview_weavedin.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.vikas.itunepreview_weavedin.R;
import com.vikas.itunepreview_weavedin.adapters.TrackListAdapter;
import com.vikas.itunepreview_weavedin.dao.Track;
import com.vikas.itunepreview_weavedin.dao.TrackViewModel;

import java.util.List;

public class FavoriteTracks extends BaseActivity {
 TrackViewModel trackViewModel;
 TextView listCountTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_traks);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listCountTextView = findViewById(R.id.list_count_text);
        RecyclerView recyclerView = findViewById(R.id.track_list);
        final TrackListAdapter adapter = new TrackListAdapter(this);
      
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Get a new or existing ViewModel from the ViewModelProvider.
        trackViewModel = ViewModelProviders.of(this).get(TrackViewModel.class);

        // Add an observer on the LiveData returned by getAlphabetizedWords.
        // The onChanged() method fires when the observed data changes and the activity is
        // in the foreground.
        trackViewModel.getAllTracks().observe(this, new Observer<List<Track>>() {
            @Override
            public void onChanged(@Nullable final List<Track> tracks) {
                // Update the cached copy of the words in the adapter.
                adapter.setTracks(tracks);
                listCountTextView.setText("Favorites - "+tracks.size());
            }
        });

    }

    public void goBack(View view) {
        onBackPressed();
    }
}
