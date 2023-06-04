package com.example.myapplication.Users;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.Locale;

public class PrescriptionDetails extends AppCompatActivity {

    private ListView medicineListView;
    private TextView totalPriceTextView;
    private ArrayAdapter<String> medicineAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescription_details);

        // Initialize views
        medicineListView = findViewById(R.id.medicine_list_view);
        totalPriceTextView = findViewById(R.id.total_price_text_view);

        // Get the prescription details from the intent
        Intent intent = getIntent();
        if (intent != null) {
            ArrayList<String> medicineList = intent.getStringArrayListExtra("medicineList");
            double totalPrice = intent.getDoubleExtra("totalPrice", 0.0);

            // Set the medicine list and total price in the ListView and TextView
            medicineAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, medicineList);
            medicineListView.setAdapter(medicineAdapter);
            totalPriceTextView.setText(String.format(Locale.getDefault(), "%.2f", totalPrice));
        }
    }
}