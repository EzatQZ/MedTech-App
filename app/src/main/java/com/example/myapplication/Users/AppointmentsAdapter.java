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

public class AppointmentsAdapter extends RecyclerView.Adapter<AppointmentsAdapter.AppointmentViewHolder> {

    private List<Appointment> appointments;
    private Context context;
    private OnChooseAppointmentClickListener chooseAppointmentClickListener;

    public AppointmentsAdapter(List<Appointment> appointments, Context context, OnChooseAppointmentClickListener listener) {
        this.appointments = appointments;
        this.context = context;
        this.chooseAppointmentClickListener = listener;
    }


    @NonNull
    @Override
    public AppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.appointment_item, parent, false);
        return new AppointmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentViewHolder holder, int position) {
        Appointment appointment = appointments.get(position);

        holder.textDate.setText(appointment.getDate());
        holder.textTime.setText(appointment.getTime());
        holder.textDoctorName.setText(appointment.getName());

        if (appointment.isChosen()) {
            holder.textStatus.setText("Chosen");
            holder.chooseButton.setEnabled(false);
        } else {
            holder.textStatus.setText("Available");
            holder.chooseButton.setEnabled(true);
            holder.chooseButton.setOnClickListener(v -> {
                if (chooseAppointmentClickListener != null) {
                    chooseAppointmentClickListener.onChooseAppointmentClick(appointment);
                    holder.chooseButton.setEnabled(false); // Disable the button after clicking
                }
            });
        }
    }



    @Override
    public int getItemCount() {
        return appointments.size();
    }

    static class AppointmentViewHolder extends RecyclerView.ViewHolder {
        TextView textDate, textTime, textDoctorName, textStatus;
        Button chooseButton;

        public AppointmentViewHolder(@NonNull View itemView) {
            super(itemView);

            textDate = itemView.findViewById(R.id.text_date);
            textTime = itemView.findViewById(R.id.text_time);
            textDoctorName = itemView.findViewById(R.id.text_doctor_name);
            textStatus = itemView.findViewById(R.id.text_status);
            chooseButton = itemView.findViewById(R.id.choose_button);
        }
    }

    public interface OnChooseAppointmentClickListener {
        void onChooseAppointmentClick(Appointment appointment);
    }
}
