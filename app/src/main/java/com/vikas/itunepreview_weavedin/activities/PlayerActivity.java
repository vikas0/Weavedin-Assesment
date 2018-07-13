
package com.vikas.itunepreview_weavedin.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.vikas.itunepreview_weavedin.R;
import com.vikas.itunepreview_weavedin.api.VolleyApiClient;
import com.vikas.itunepreview_weavedin.dao.Track;
import com.vikas.itunepreview_weavedin.dao.TrackRepository;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.audio.AudioRendererEventListener;
import com.google.android.exoplayer2.decoder.DecoderCounters;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.video.VideoRendererEventListener;


/**
 * A fullscreen activity to play audio or video streams.
 */
public class PlayerActivity extends BaseActivity {
    private static final String TAG = "LogTAg";
    private static final float BOX_WIDTH = 224;
    PlayerView playerView;
    SimpleExoPlayer player;

    boolean playWhenReady = true;
    int currentWindow = 0;
    long playbackPosition = 0;

    ComponentListener componentListener;
    Track track;
    TextView trackTitle, artistAlbumName;
    NetworkImageView trakImage;

    ImageView isFav,makeFav;

    private static final DefaultBandwidthMeter BANDWIDTH_METER =
            new DefaultBandwidthMeter();

//    private Handler mHandler;
//    private final Runnable updateProgressAction = new Runnable() {
//        @Override
//        public void run() {
//            updateProgress();
//        }
//    };


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        processIntent();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_player);
      processIntent();

    }

    private void processIntent() {
        releasePlayer();
        track = (Track) getIntent().getSerializableExtra("track");
        if (track == null)
            finish();

        isFav = findViewById(R.id.is_fav);
        makeFav = findViewById(R.id.make_fav);
        makeFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(track.isFasvorite())
                {
                    track.setFasvorite(false);
                    new TrackRepository(activity).removeFavorite(track);
                    setFav();
                }
                else
                {
                    track.setFasvorite(true);
                    new TrackRepository(activity).insert(track);
                    setFav();
                }
            }
        });
        setFav();

        trackTitle = findViewById(R.id.title);
        artistAlbumName = findViewById(R.id.artist_name_collection_name);
        trakImage = findViewById(R.id.track_image);
        trackTitle.setText(track.getTrackName());
        artistAlbumName.setText(track.getArtistName() + " | " + track.getCollectionName());
        trakImage.setImageUrl(track.getArtworkUrl100(), VolleyApiClient.getInstance(this).getImageLoader());

        //    setContentView(R.layout.activity_player);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        componentListener = new ComponentListener();
        playerView = findViewById(R.id.video_view);
        playerView.setControllerShowTimeoutMs(0);  // to disable timeout.
        playerView.setControllerHideOnTouch(false); // always show

//        hideSystemUi();
        initializePlayer();
    }

    private void setFav() {
        if(track.isFasvorite())
        {
//            isFav.setImageResource(R.drawable.shape_heart_red);
            makeFav.setImageResource(R.drawable.shape_heart_red);
        }
        else
        {
//            isFav.setImageResource(R.drawable.shape_heart);
            makeFav.setImageResource(R.drawable.shape_heart);

        }
    }

    private void initializePlayer() {

        player = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(this),
                new DefaultTrackSelector(), new DefaultLoadControl());

        playerView.setPlayer(player);
        player.addListener(componentListener);
        player.addVideoDebugListener(componentListener);
        player.addAudioDebugListener(componentListener);


        player.setPlayWhenReady(playWhenReady);
//        mHandler = new Handler();
//        mHandler.post(updateProgressAction);
        player.seekTo(currentWindow, playbackPosition);

        MediaSource mediaSource1 = buildMediaSource(Uri.parse(track.getPreviewUrl()));
        player.prepare(mediaSource1, true, false);
    }

    private MediaSource buildMediaSource(Uri uri) {
        return new ExtractorMediaSource.Factory(
                new DefaultHttpDataSourceFactory("ua")).
                createMediaSource(uri);

//    return  = new /SourceFactory, extractorsFactory, null, null);

        // For Dash
//    DataSource.Factory manifestDataSourceFactory =
//            new DefaultHttpDataSourceFactory("ua");
//    DashChunkSource.Factory dashChunkSourceFactory =
//            new DefaultDashChunkSource.Factory(
//                    new DefaultHttpDataSourceFactory("ua", BANDWIDTH_METER));
//    return new DashMediaSource.Factory(dashChunkSourceFactory,
//            manifestDataSourceFactory).createMediaSource(uri);
    }
//
//    @Override
//    public void onStart() {
//        super.onStart();
//        if (Util.SDK_INT > 23) {
//            initializePlayer();
//        }
//    }

    //  @Override
//  public void onResume() {
//    super.onResume();
//    hideSystemUi();
//    if ((Util.SDK_INT <= 23 || player == null)) {
//      initializePlayer();
//    }
//  }
//
//  @Override
//  public void onPause() {
//    super.onPause();
//    if (Util.SDK_INT <= 23) {
//      releasePlayer();
//    }
//  }
//
  @Override
  public void onStop() {
    super.onStop();
//    if (Util.SDK_INT > 23) {
//      releasePlayer();
//    }
  }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }

    private void releasePlayer() {
        if (player != null) {
            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            playWhenReady = player.getPlayWhenReady();

            player.removeListener(componentListener);
            player.removeVideoDebugListener(componentListener);
            player.removeAudioDebugListener(componentListener);

            player.release();
            player = null;
        }
    }



    public void goBack(View view) {
        onBackPressed();
    }

    public void gotoList(View view) {
        onBackPressed();
    }

    private class ComponentListener extends Player.DefaultEventListener implements VideoRendererEventListener, AudioRendererEventListener {

        @Override
        public void onPlayerStateChanged(boolean playWhenReady,
                                         int playbackState) {
            String stateString;
            switch (playbackState) {
                case ExoPlayer.STATE_IDLE:
                    stateString = "ExoPlayer.STATE_IDLE      -";
                    break;
                case ExoPlayer.STATE_BUFFERING:
                    stateString = "ExoPlayer.STATE_BUFFERING -";
                    break;
                case ExoPlayer.STATE_READY:
                    stateString = "ExoPlayer.STATE_READY     -";
                    break;
                case ExoPlayer.STATE_ENDED:
                    stateString = "ExoPlayer.STATE_ENDED     -";
                    break;
                default:
                    stateString = "UNKNOWN_STATE             -";
                    break;
            }
            Log.d(TAG, "changed state to " + stateString
                    + " playWhenReady: " + playWhenReady);
        }

        @Override
        public void onVideoEnabled(DecoderCounters counters) {

        }

        @Override
        public void onVideoDecoderInitialized(String decoderName, long initializedTimestampMs, long initializationDurationMs) {

        }

        @Override
        public void onVideoInputFormatChanged(Format format) {

        }

        @Override
        public void onDroppedFrames(int count, long elapsedMs) {

        }

        @Override
        public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {

        }

        @Override
        public void onRenderedFirstFrame(Surface surface) {

        }

        @Override
        public void onVideoDisabled(DecoderCounters counters) {

        }

        @Override
        public void onAudioEnabled(DecoderCounters counters) {

        }

        @Override
        public void onAudioSessionId(int audioSessionId) {

        }

        @Override
        public void onAudioDecoderInitialized(String decoderName, long initializedTimestampMs, long initializationDurationMs) {

        }

        @Override
        public void onAudioInputFormatChanged(Format format) {

        }

        @Override
        public void onAudioSinkUnderrun(int bufferSize, long bufferSizeMs, long elapsedSinceLastFeedMs) {

        }

        @Override
        public void onAudioDisabled(DecoderCounters counters) {

        }
    }

}
