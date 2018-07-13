package com.vikas.itunepreview_weavedin.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface TrackDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Track track);

    @Query("SELECT * from track_table ")
    LiveData<List<Track>> getAllTracks();

    @Delete
    void delete(Track track);
}
