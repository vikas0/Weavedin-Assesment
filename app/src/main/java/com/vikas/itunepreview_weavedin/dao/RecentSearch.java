package com.vikas.itunepreview_weavedin.dao;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "track_searches")
public class RecentSearch {
    @PrimaryKey
    @NonNull
    private String searchText;

    private long time;

    public RecentSearch(@NonNull String searchText, long time) {
        this.searchText = searchText;
        this.time = time;
    }

    @NonNull
    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(@NonNull String searchText) {
        this.searchText = searchText;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
