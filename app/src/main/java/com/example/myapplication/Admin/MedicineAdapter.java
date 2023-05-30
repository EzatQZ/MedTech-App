package com.example.myapplication.Admin;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myapplication.R;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MedicineAdapter extends BaseAdapter {

    private Context context;
    private List<Medicine> medicineList;
    private DatabaseReference databaseRef;

    public MedicineAdapter(Context context, List<Medicine> medicineList, DatabaseReference databaseRef) {
        this.context = context;
        this.medicineList = medicineList;
        this.databaseRef = databaseRef;
    }

    @Override
    public int getCount() {
        return medicineList.size();
    }

    @Override
    public Object getItem(int position) {
        return medicineList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.medicine_list_item, null);
        }

        TextView medicineNameTextView = view.findViewById(R.id.medicine_name_text_view);
        TextView medicineQuantityTextView = view.findViewById(R.id.medicine_quantity_text_view);
        TextView medicinePriceTextView = view.findViewById(R.id.medicine_price_text_view);
        TextView medicineExpirationDateTextView = view.findViewById(R.id.medicine_expiration_date_text_view);
        Button deleteButton = view.findViewById(R.id.delete_button); // reference to delete button

        Medicine medicine = medicineList.get(position);

        medicineNameTextView.setText(medicine.getName());
        medicineQuantityTextView.setText(String.valueOf(medicine.getQuantity()));
        medicinePriceTextView.setText(String.format(Locale.getDefault(), "%.2f", medicine.getPrice()));


        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delete Medicine");
                builder.setMessage("Are you sure you want to delete this medicine?");
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(medicine != null && medicine.getUid() != null) {
                            DatabaseReference medicineRef = databaseRef.child(medicine.getUid()); // use the databaseRef passed from ViewMedicine activity
                            medicineRef.removeValue(new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                    if (error != null) {
                                        Toast.makeText(context, "Failed to delete medicine: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(context, "Medicine deleted successfully", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(context, "Failed to delete medicine: invalid data", Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
                    }
                });


                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });


        return view;
    }}


