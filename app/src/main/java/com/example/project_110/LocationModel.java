package com.example.project_110;

import android.annotation.SuppressLint;
import android.app.Application;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class LocationModel extends AndroidViewModel {
    private final String TAG = "LocationModel";
    private final MediatorLiveData<Coord> lastKnownCoords;
    private final MediatorLiveData<Coord> lastKnownMockedCoords;

    private LiveData<Coord> locationProviderSource = null;
    private MutableLiveData<Coord> mockSource = null;
    private boolean useMockedCoords = false;

    public LocationModel(@NonNull Application application) {
        super(application);
        lastKnownCoords = new MediatorLiveData<>();
        lastKnownMockedCoords = new MediatorLiveData<>();
        lastKnownCoords.setValue(new Coord(0, 0));
    }

    public LiveData<Coord> getLastKnownCoords() {
        if (useMockedCoords)
            return lastKnownMockedCoords;
        return lastKnownCoords;
    }

    public boolean isUsingMockedCoords() {
        return useMockedCoords;
    }

    public void useRealCoords() {
        useMockedCoords = false;
    }

    /**
     * @param locationManager the location manager to request updates from.
     * @param provider        the provider to use for location updates (usually GPS).
     * @apiNote This method should only be called after location permissions have been obtained.
     * @implNote If a location provider source already exists, it is removed.
     */
    @SuppressLint("MissingPermission")
    public void addLocationProviderSource(LocationManager locationManager, String provider) {
        // If a location provider source is already added, remove it.
        if (locationProviderSource != null) {
            removeLocationProviderSource();
        }

        // Create a new GPS source.
        MutableLiveData<Coord> providerSource = new MutableLiveData<Coord>();
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                Coord coord = Coord.fromLocation(location);
                // Log.i(TAG, String.format("Model received GPS location update: %s", coord));
                // providerSource.postValue(coord);
                lastKnownCoords.setValue(coord);
                // System.out.println(getLastKnownCoords().getValue().lat + " " + getLastKnownCoords().getValue().lng);
            }
        };
        // Register for updates.
        locationManager.requestLocationUpdates(provider, 0, 0f, locationListener);

        locationProviderSource = providerSource;
        lastKnownCoords.addSource(locationProviderSource, lastKnownCoords::setValue);
    }

    void removeLocationProviderSource() {
        if (locationProviderSource == null) return;
        lastKnownCoords.removeSource(locationProviderSource);
    }

    @VisibleForTesting
    public void mockLocation(Coord coords) {
        lastKnownMockedCoords.postValue(coords);
    }

    @VisibleForTesting
    public Future<?> mockRoute(List<Coord> route, long delay, TimeUnit unit) {
        useMockedCoords = true;
        return Executors.newSingleThreadExecutor().submit(() -> {
            int i = 1;
            int n = route.size();
            for (Coord coord : route) {
                // Mock the location...
                Log.v(TAG, String.format("Model mocking route (%d / %d): %s", i++, n, coord));
                mockLocation(coord);

                // Sleep for a while...
                try {
                    Thread.sleep(unit.toMillis(delay));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
