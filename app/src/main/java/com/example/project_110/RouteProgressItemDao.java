package com.example.project_110;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface RouteProgressItemDao {
    @Query("DELETE FROM route_progress_items")
    public void nukeTable();

    @Insert
    long insert(RouteProgressItem routeProgressItem);

    @Query("SELECT * FROM `route_progress_items`")
    List<RouteProgressItem> getAll();

}
