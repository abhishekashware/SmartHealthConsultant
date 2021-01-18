package com.example.smarthealthconsultant.smarthealthconsultant.Appointment;

public class Appointments {
    public String doc_name;
    public String status;
    public String image;
    public Appointments(){

    }

    public Appointments(String doc_name, String email, String status,String image) {
        this.doc_name = doc_name;
       this.image=image;
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDoc_name() {
        return doc_name;
    }

    public void setDoc_name(String doc_name) {
        this.doc_name = doc_name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
