package com.example.myapplication.Doctor;

public class Appointment {
    private String appointmentId;
    private String date;
    private String time;
    private String doctorId;
    private String name;
    private String userID;
    private boolean chosen;

    public Appointment() {
        // Default constructor required for Firebase
    }

    public Appointment(String appointmentId, String date, String time, String doctorId, String name, String userID) {
        this.appointmentId = appointmentId;
        this.date = date;
        this.time = time;
        this.doctorId = doctorId;
        this.name = name;
        this.userID = userID;
        this.chosen = false; // By default, the appointment is not chosen
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public boolean isChosen() {
        return chosen;
    }

    public void setChosen(boolean chosen) {
        this.chosen = chosen;
    }
}
