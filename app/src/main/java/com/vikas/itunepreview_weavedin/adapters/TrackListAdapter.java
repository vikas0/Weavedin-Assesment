package com.vikas.itunepreview_weavedin.adapters;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.vikas.itunepreview_weavedin.R;
import com.vikas.itunepreview_weavedin.activities.PlayerActivity;
import com.vikas.itunepreview_weavedin.api.VolleyApiClient;
import com.vikas.itunepreview_weavedin.dao.Track;

import java.io.Serializable;
import java.util.List;

public class TrackListAdapter extends RecyclerView.Adapter<TrackListAdapter.TrackViewHolder> {

    Context context;
    class TrackViewHolder extends RecyclerView.ViewHolder {
        private final TextView title, artsitNameCollectionName;
        private NetworkImageView image;

        private TrackViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            artsitNameCollectionName = itemView.findViewById(R.id.artist_name_collection_name);
            image = itemView.findViewById(R.id.image);
        }
    }

    private final LayoutInflater mInflater;
    private List<Track> mTracks; // Cached copy of Tracks

  public  TrackListAdapter(Context context) {
      this.context = context;
      mInflater = LayoutInflater.from(context);


  }

    @Override
    public TrackViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.track, parent, false);
        return new TrackViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TrackViewHolder holder, int position) {
        if (mTracks != null) {
            final Track track = mTracks.get(position);
            holder.title.setText(track.getTrackName());
            holder.artsitNameCollectionName.setText(track.getArtistName()+" | "+track.getCollectionName());
            holder.image.setImageUrl(track.getArtworkUrl100(), VolleyApiClient.getInstance(context).getImageLoader());
           holder.itemView.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   context.startActivity(new Intent(context, PlayerActivity.class).putExtra("track", (Serializable) track));
               }
           });
        } else {
            // Covers the case of data not being ready yet.
            holder.title.setText("No Track");
        }
    }

   public void setTracks(List<Track> Tracks){
        mTracks = Tracks;
        notifyDataSetChanged();
    }

    // getItemCount() is called many times, and when it is first called,
    // mTracks has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (mTracks != null)
            return mTracks.size();
        else return 0;
    }
}