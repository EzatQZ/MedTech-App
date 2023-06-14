package com.example.myapplication.Users;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.myapplication.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener {
    private GoogleMap googleMap;
    private LocationManager locationManager;
    private Marker userMarker;
    private Marker pharmacy1Marker;
    private Marker pharmacy2Marker;

    private static final int REQUEST_LOCATION_PERMISSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Request runtime permissions if not granted
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        } else {
            // Start requesting location updates
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        // Add Pharmacy1 marker
        String pharmacy1Name = "Pharmacy1";
        double pharmacy1Latitude = 32.968965;
        double pharmacy1Longitude = 35.379027;
        LatLng pharmacy1Location = new LatLng(pharmacy1Latitude, pharmacy1Longitude);
        pharmacy1Marker = googleMap.addMarker(new MarkerOptions().position(pharmacy1Location).title(pharmacy1Name));

        // Add Pharmacy2 marker
        String pharmacy2Name = "Pharmacy2";
        double pharmacy2Latitude = 32.96224171520463;
        double pharmacy2Longitude = 35.36252830999811;
        LatLng pharmacy2Location = new LatLng(pharmacy2Latitude, pharmacy2Longitude);
        pharmacy2Marker = googleMap.addMarker(new MarkerOptions().position(pharmacy2Location).title(pharmacy2Name));

        // Move the camera to a suitable zoom level
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(pharmacy1Location);
        builder.include(pharmacy2Location);
        LatLngBounds bounds = builder.build();
        googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 200));

        // Calculate distances and display nearest pharmacy
        if (userMarker != null) {
            LatLng userLocation = userMarker.getPosition();
            double distanceToPharmacy1 = calculateDistance(userLocation, pharmacy1Location);
            double distanceToPharmacy2 = calculateDistance(userLocation, pharmacy2Location);

            Marker nearestMarker;
            Marker otherMarker;
            if (distanceToPharmacy1 < distanceToPharmacy2) {
                nearestMarker = pharmacy1Marker;
                otherMarker = pharmacy2Marker;
            } else {
                nearestMarker = pharmacy2Marker;
                otherMarker = pharmacy1Marker;
            }

            nearestMarker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            otherMarker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

            Toast.makeText(this, "Nearest pharmacy: " + nearestMarker.getTitle(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        // Get the updated latitude and longitude
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        // Update the user's marker or add it if it doesn't exist
        LatLng userLocation = new LatLng(latitude, longitude);
        if (userMarker == null) {
            userMarker = googleMap.addMarker(new MarkerOptions().position(userLocation).title("User Location"));
        } else {
            userMarker.setPosition(userLocation);
        }

        // Move the camera to the user's location
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(userLocation));

        // Update the markers when the user's location changes
        onMapReady(googleMap);
    }

    @Override
    public void onProviderEnabled(String provider) {
        // Empty implementation
    }

    @Override
    public void onProviderDisabled(String provider) {
        // Empty implementation
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // Empty implementation
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        locationManager.removeUpdates(this);
    }

    private double calculateDistance(LatLng start, LatLng end) {
        double earthRadius = 6371; // Earth's radius in kilometers

        double latDistance = Math.toRadians(end.latitude - start.latitude);
        double lngDistance = Math.toRadians(end.longitude - start.longitude);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(start.latitude)) * Math.cos(Math.toRadians(end.latitude))
                * Math.sin(lngDistance / 2) * Math.sin(lngDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return earthRadius * c * 1000; // Convert to meters
    }
}

