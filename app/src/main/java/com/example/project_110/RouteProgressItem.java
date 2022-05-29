package com.example.project_110;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.List;

@Entity(tableName = "route_progress_items")
public class RouteProgressItem {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @NonNull @TypeConverters({VertexInfoStorableListConverter.class})
    public List<VertexInfoStorable> shortestVertexOrder; // The order of exhibits
    public int currDestInd; // The index of the next destination our route is taking us to
    public int currViewInd; // The index of the current destination/exhibit the user is viewing in the app

    RouteProgressItem() {}

    RouteProgressItem(@NonNull List<VertexInfoStorable> shortestVertexOrder, int currDestInd, int currViewInd) {
        this.shortestVertexOrder = shortestVertexOrder;
        this.currDestInd = currDestInd;
        this.currViewInd = currViewInd;
    }
}
