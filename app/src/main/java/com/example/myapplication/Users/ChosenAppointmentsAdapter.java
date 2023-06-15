package com.example.myapplication.Users;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Doctor.Appointment;
import com.example.myapplication.R;

import java.util.List;

public class ChosenAppointmentsAdapter extends RecyclerView.Adapter<ChosenAppointmentsAdapter.AppointmentViewHolder> {

    private List<Appointment> appointments;
    private Context context;
    private OnCancelAppointmentClickListener cancelAppointmentClickListener;

    public ChosenAppointmentsAdapter(List<Appointment> appointments, Context context, OnCancelAppointmentClickListener listener) {
        this.appointments = appointments;
        this.context = context;
        this.cancelAppointmentClickListener = listener;
    }

    @NonNull
    @Override
    public AppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.chosenappointments_item, parent, false);
        return new AppointmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentViewHolder holder, int position) {
        Appointment appointment = appointments.get(position);

        holder.textDate.setText(appointment.getDate());
        holder.textTime.setText(appointment.getTime());
        holder.textDoctorName.setText(appointment.getName());

        holder.cancelButton.setOnClickListener(v -> {
            if (cancelAppointmentClickListener != null) {
                cancelAppointmentClickListener.onCancelAppointmentClick(appointment);
            }
        });
    }

    @Override
    public int getItemCount() {
        return appointments.size();
    }

    static class AppointmentViewHolder extends RecyclerView.ViewHolder {
        TextView textDate, textTime, textDoctorName;
        Button cancelButton;

        public AppointmentViewHolder(@NonNull View itemView) {
            super(itemView);

            textDate = itemView.findViewById(R.id.text_date);
            textTime = itemView.findViewById(R.id.text_time);
            textDoctorName = itemView.findViewById(R.id.text_doctor_name);
            cancelButton = itemView.findViewById(R.id.cancel_button);
        }
    }

    public interface OnCancelAppointmentClickListener {
        void onCancelAppointmentClick(Appointment appointment);
    }
}
