package com.example.myapplication.Users;


        import androidx.annotation.NonNull;
        import androidx.appcompat.app.AppCompatActivity;
        import androidx.core.app.ActivityCompat;
        import androidx.core.content.ContextCompat;

        import android.content.Intent;
        import android.content.pm.PackageManager;
        import android.location.Location;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.View;
        import android.widget.AdapterView;
        import android.widget.ArrayAdapter;
        import android.widget.ListView;
        import android.widget.Toast;

        import com.example.myapplication.Admin.Medicine;
        import com.example.myapplication.Admin.Pharmacy;
        import com.example.myapplication.Doctor.Prescription;
        import com.example.myapplication.R;
        import com.google.android.gms.maps.CameraUpdateFactory;
        import com.google.android.gms.maps.GoogleMap;
        import com.google.android.gms.maps.OnMapReadyCallback;
        import com.google.android.gms.maps.SupportMapFragment;
        import com.google.android.gms.maps.model.MarkerOptions;
        import com.google.android.gms.tasks.OnSuccessListener;
        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.database.DataSnapshot;
        import com.google.firebase.database.DatabaseError;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;
        import com.google.firebase.database.ValueEventListener;
        import com.google.android.gms.maps.model.LatLng;


        import java.util.ArrayList;
        import java.util.List;
        import java.util.Map;

public class MedicineLocator extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_locator);
}
}

