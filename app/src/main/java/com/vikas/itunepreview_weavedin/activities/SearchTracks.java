package com.vikas.itunepreview_weavedin.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.vikas.itunepreview_weavedin.R;
import com.vikas.itunepreview_weavedin.adapters.TrackListAdapter;
import com.vikas.itunepreview_weavedin.api.VolleyApiClient;
import com.vikas.itunepreview_weavedin.dao.RecentSearch;
import com.vikas.itunepreview_weavedin.dao.TrackRepository;
import com.example.android.codelabs.paging.api.TrackSearchResponse;

public class SearchTracks extends BaseActivity {
    AutoCompleteTextView searchRepo;
    ImageView searchimage;
    private TextView listCountTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_tracks);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        listCountTextView = findViewById(R.id.list_count_text);

        RecyclerView recyclerView = findViewById(R.id.track_list);
        final TrackListAdapter adapter = new TrackListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        searchRepo = findViewById(R.id.search_repo);
        searchimage =findViewById(R.id.search_image);

        ArrayAdapter<String> recentSuggestAdapter = new ArrayAdapter<String>
                (this,R.layout.my_select_dialog_item, new TrackRepository(this).getAllRecentSearches());

        searchRepo.setThreshold(2);
        searchRepo.setAdapter(recentSuggestAdapter);

        searchimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(searchRepo.getText().length()>2)
                    applySearch(adapter,searchRepo.getText().toString());
            }
        });
        searchRepo.setOnEditorActionListener(new EditText.OnEditorActionListener() {
    @Override
    public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
       if( (actionId == EditorInfo.IME_ACTION_SEARCH ||
               actionId == EditorInfo.IME_ACTION_DONE ||
               actionId == EditorInfo.IME_ACTION_GO ||
               actionId == EditorInfo.IME_ACTION_NEXT )
                            && textView.getText().length()>3)
       {
         applySearch(adapter,textView.getText().toString());
         return true;
       }
       else return false;
    }
});



    }

    private void applySearch(final TrackListAdapter adapter, final String s) {
        adapter.setTracks(null);
        adapter.notifyDataSetChanged();
        showLoader();
        VolleyApiClient.getInstance(SearchTracks.this).searchTracks(s, new VolleyApiClient.TrackRequestListner() {
            @Override
            public void onSuccess(TrackSearchResponse response) {
                new TrackRepository(SearchTracks.this).insert(new RecentSearch(s,System.currentTimeMillis()));
                adapter.setTracks(response.getItems());
                listCountTextView.setText("All Songs - "+response.getTotal());
                adapter.notifyDataSetChanged();

                hideLoader();
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    private void hideLoader() {

    }

    private void showLoader() {


    }

    public void goToFavorite(View view) {
        startActivity(new Intent(this,FavoriteTracks.class));
    }
}
