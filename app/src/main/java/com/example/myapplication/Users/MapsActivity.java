package com.example.myapplication.Users;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.myapplication.Admin.Medicine;
import com.example.myapplication.Admin.Pharmacy;
import com.example.myapplication.Doctor.Prescription;
import com.example.myapplication.R;
import com.example.myapplication.User;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener {
    private GoogleMap googleMap;
    private LocationManager locationManager;
    private Marker userMarker;
    private Map<String, Pharmacy> pharmacies;
    private FirebaseAuth mAuth;
    private List<Prescription> prescriptionList;

    private ArrayAdapter<Prescription> prescriptionAdapter;


    private static final int REQUEST_LOCATION_PERMISSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        mAuth = FirebaseAuth.getInstance();
        prescriptionList = new ArrayList<>();
        prescriptionAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, prescriptionList);

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

        // Initialize pharmacies
        pharmacies = new HashMap<>();
        initPharmacies();
    }

    private void initPharmacies() {
        // Retrieve the pharmacy data from Firebase or any data source of your choice

        // Pharmacy1
        String pharmacy1Uid = "-NXa0At1X8muEuWd0lv5";
        String pharmacy1Name = "Pharmacy1";
        String pharmacy1Address = "Address1";
        String pharmacy1Manager = "Manager1";
        double pharmacy1Latitude = 32.968965;
        double pharmacy1Longitude = 35.379027;
        Pharmacy pharmacy1 = new Pharmacy(pharmacy1Uid, pharmacy1Name, pharmacy1Address, pharmacy1Manager, pharmacy1Latitude, pharmacy1Longitude);
        pharmacy1.addMedicinesToInventory(List.of(
                "Adderall", "Ativan", "Amoxicillen", "Atorvastatin", "Brilinta",
                "Cephalexin", "Farxiga", "Imbruvica", "Naproxen", "Naloxone",
                "Naltrexone", "Dupixent"
        ));
        pharmacies.put(pharmacy1Uid, pharmacy1);

        // Pharmacy2
        String pharmacy2Uid = "-NXutxv5U7fvyWLCQask";
        String pharmacy2Name = "Pharmacy2";
        String pharmacy2Address = "Address2";
        String pharmacy2Manager = "Manager2";
        double pharmacy2Latitude = 32.96224171520463;
        double pharmacy2Longitude = 35.36252830999811;
        Pharmacy pharmacy2 = new Pharmacy(pharmacy2Uid, pharmacy2Name, pharmacy2Address, pharmacy2Manager, pharmacy2Latitude, pharmacy2Longitude);
        pharmacy2.addMedicinesToInventory(List.of(
                "Adderall", "Ativan", "Amoxicillen", "Atorvastatin", "Brilinta", "Bunavail"
        ));
        pharmacies.put(pharmacy2Uid, pharmacy2);

        // Pharmacy3
        String pharmacy3Uid = "-NXuwsoXqzoBVnIaOu2K";
        String pharmacy3Name = "Pharmacy3";
        String pharmacy3Address = "Address3";
        String pharmacy3Manager = "Manager3";
        double pharmacy3Latitude = 32.97525193279973;
        double pharmacy3Longitude = 35.33702457632527;
        Pharmacy pharmacy3 = new Pharmacy(pharmacy3Uid, pharmacy3Name, pharmacy3Address, pharmacy3Manager, pharmacy3Latitude, pharmacy3Longitude);
        pharmacy3.addMedicinesToInventory(List.of(
                "Adderall", "Ativan", "Entyvio", "Clonazepam", "Dupixent",
                "Humira", "Bunavail", "Doxycycline"
        ));
        pharmacies.put(pharmacy3Uid, pharmacy3);

        // Pharmacy4
        String pharmacy4Uid = "-NXuxVqN0OJQKMzv9lZA";
        String pharmacy4Name = "Pharmacy4";
        String pharmacy4Address = "Address4";
        String pharmacy4Manager = "Manager4";
        double pharmacy4Latitude = 32.95375909581809;
        double pharmacy4Longitude = 35.390284716559215;
        Pharmacy pharmacy4 = new Pharmacy(pharmacy4Uid, pharmacy4Name, pharmacy4Address, pharmacy4Manager, pharmacy4Latitude, pharmacy4Longitude);
        pharmacy4.addMedicinesToInventory(List.of(
                "Adderall", "Ativan", "Amoxicillen", "Atorvastatin", "Brilinta",
                "Bunavail", "Cephalexin", "Cymbalta", "Metoprolol", "Nurtec", "Onpattro"
        ));
        pharmacies.put(pharmacy4Uid, pharmacy4);

        // Pharmacy5
        String pharmacy5Uid = "-NXuxjAdjXqMASmYZ0KD";
        String pharmacy5Name = "Pharmacy5";
        String pharmacy5Address = "Address5";
        String pharmacy5Manager = "Manager5";
        double pharmacy5Latitude = 32.99722304368339;
        double pharmacy5Longitude = 35.30148827150986;
        Pharmacy pharmacy5 = new Pharmacy(pharmacy5Uid, pharmacy5Name, pharmacy5Address, pharmacy5Manager, pharmacy5Latitude, pharmacy5Longitude);
        pharmacy5.addMedicinesToInventory(List.of(
                "Adderall", "Atorvastatin", "Dupixent", "Invokana", "Naproxen",
                "Amoxicillen", "Buprenorphine"
        ));
        pharmacies.put(pharmacy5Uid, pharmacy5);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        // Add markers for each pharmacy
        for (Pharmacy pharmacy : pharmacies.values()) {
            LatLng pharmacyLocation = new LatLng(pharmacy.getLatitude(), pharmacy.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(pharmacyLocation)
                    .title(pharmacy.getName())
                    .snippet(getInventoryString(pharmacy.getInventory()));
            googleMap.addMarker(markerOptions);
        }

        // Move the camera to include all markers
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (Pharmacy pharmacy : pharmacies.values()) {
            LatLng pharmacyLocation = new LatLng(pharmacy.getLatitude(), pharmacy.getLongitude());
            builder.include(pharmacyLocation);
        }
        LatLngBounds bounds = builder.build();
        googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 200));

        if (userMarker != null) {
            LatLng userLocation = userMarker.getPosition();
            List<String> prescriptionMedicineNames = loadPrescriptions(); // Retrieve prescription medicine names from the user or the appropriate source
            List<Pharmacy> pharmaciesWithMedicines = getPharmaciesWithMedicines(prescriptionMedicineNames);

            if (!pharmaciesWithMedicines.isEmpty()) {
                Pharmacy nearestPharmacy = getNearestPharmacy(userLocation, pharmaciesWithMedicines);

                if (nearestPharmacy != null) {
                    Marker nearestMarker = googleMap.addMarker(new MarkerOptions()
                            .position(new LatLng(nearestPharmacy.getLatitude(), nearestPharmacy.getLongitude()))
                            .title(nearestPharmacy.getName())
                            .snippet(getInventoryString(nearestPharmacy.getInventory()))
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

                    for (Pharmacy pharmacy : pharmacies.values()) {
                        if (pharmacy != nearestPharmacy) {
                            googleMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(pharmacy.getLatitude(), pharmacy.getLongitude()))
                                    .title(pharmacy.getName())
                                    .snippet(getInventoryString(pharmacy.getInventory()))
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                        }
                    }

                    Toast.makeText(this, "Nearest pharmacy: " + nearestPharmacy.getName(), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "No pharmacies with all the required medicines found.", Toast.LENGTH_SHORT).show();
            }
        }

    }

    public List<String> loadPrescriptions() {
        DatabaseReference prescriptionsRef = FirebaseDatabase.getInstance().getReference("Prescriptions");
        String currentUserEmail = mAuth.getCurrentUser().getEmail(); // Get the current user's email

        prescriptionsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                prescriptionList.clear();

                for (DataSnapshot prescriptionSnapshot : dataSnapshot.getChildren()) {
                    Prescription prescription = prescriptionSnapshot.getValue(Prescription.class);
                    String prescriptionUserEmail = prescription.getUser().getEmail();

                    if (prescriptionUserEmail.equals(currentUserEmail)) {
                        prescriptionList.add(prescription);
                    }
                }

                prescriptionAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("LoadPrescriptions", "Error: " + databaseError.getMessage());
            }
        });

        List<String> prescriptionMedicineNames = new ArrayList<>();
        for (Prescription prescription : prescriptionList) {
            List<Medicine> medicines = prescription.getMedicines();
            for (Medicine medicine : medicines) {
                prescriptionMedicineNames.add(medicine.getName());
            }
        }

        return prescriptionMedicineNames;

    }

    private Pharmacy getNearestPharmacy(LatLng userLocation, List<Pharmacy> pharmacies) {
        Pharmacy nearestPharmacy = null;
        double minDistance = Double.MAX_VALUE;

        for (Pharmacy pharmacy : pharmacies) {
            double distance = calculateDistance(userLocation, new LatLng(pharmacy.getLatitude(), pharmacy.getLongitude()));
            if (distance < minDistance) {
                minDistance = distance;
                nearestPharmacy = pharmacy;
            }
        }

        return nearestPharmacy;
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

    private List<Pharmacy> getPharmaciesWithMedicines(List<String> prescriptionMedicines) {
        List<Pharmacy> pharmaciesWithMedicines = new ArrayList<>();

        for (Pharmacy pharmacy : pharmacies.values()) {
            List<String> pharmacyMedicines = pharmacy.getInventory();

            // Check if all prescription medicines are available at the pharmacy
            boolean hasAllMedicines = pharmacyMedicines.containsAll(prescriptionMedicines);

            if (hasAllMedicines) {
                pharmaciesWithMedicines.add(pharmacy);
            }
        }

        return pharmaciesWithMedicines;
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

    private String getInventoryString(List<String> inventory) {
        StringBuilder sb = new StringBuilder();
        for (String item : inventory) {
            sb.append(item).append(", ");
        }
        if (sb.length() > 2) {
            sb.setLength(sb.length() - 2); // Remove the trailing comma and space
        }
        return sb.toString();
    }
}
