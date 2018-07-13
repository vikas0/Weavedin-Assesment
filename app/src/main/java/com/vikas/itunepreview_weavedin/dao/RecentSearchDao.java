package com.vikas.itunepreview_weavedin.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;
@Dao
public interface RecentSearchDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(RecentSearch recentSearch);

    @Query("SELECT searchText from track_searches ORDER BY time DESC ")
    List<String> getAllRecentSearchesTexts();


    @Query("SELECT * from track_searches ")
    List<RecentSearch> getAllRecent();

    @Delete
    void delete(RecentSearch recentSearch);
}
