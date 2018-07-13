package com.vikas.itunepreview_weavedin.dao;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {Track.class,RecentSearch.class}, version = 1)
public abstract class TrackRoomDatabase extends RoomDatabase {
    public abstract TrackDao TrackDao();
    public abstract RecentSearchDao RecentSearchDao();

    private static TrackRoomDatabase INSTANCE;

    public static TrackRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (TrackRoomDatabase.class) {
                if (INSTANCE == null) {
                    // Create database here
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            TrackRoomDatabase.class, "track_database")
                            .allowMainThreadQueries().build();
                }
            }
        }
        return INSTANCE;
    }

}
