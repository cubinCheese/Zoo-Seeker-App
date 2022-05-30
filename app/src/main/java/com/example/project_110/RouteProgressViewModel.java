package com.example.project_110;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import java.util.List;

public class RouteProgressViewModel extends AndroidViewModel {
    private final RouteProgressItemDao routeProgressItemDao;

    public RouteProgressViewModel(@NonNull Application application) {
        super(application);
        Context context = getApplication().getApplicationContext();
        VertexDatabase db = VertexDatabase.getSingleton(context);
        routeProgressItemDao = db.routeProgressItemDao();
    }

    public void storeRouteProgressItem (List<VertexInfoStorable> shortestVertexOrder, int currDestInd, int currViewInd) {
        clearAllItems();
        routeProgressItemDao.insert(new RouteProgressItem(shortestVertexOrder, currDestInd, currViewInd));
    }

    public RouteProgressItem loadRouteProgressItem() {
        System.out.println("c");
        List<RouteProgressItem> l = routeProgressItemDao.getAll();
        System.out.println("d");
        return (l == null || l.isEmpty()) ? null : l.get(0);
    }

    public void clearAllItems() {
        routeProgressItemDao.nukeTable();
    }
}
