package com.example.smarthealthconsultant.smarthealthconsultant.Appointment.apporequests;

public class AppoReqClass {

    public String email;
    public String doc_name;

    public String getDoc_name() {
        return doc_name;
    }

    public void setDoc_name(String doc_name) {
        this.doc_name = doc_name;
    }

    public AppoReqClass(){

    }
    public AppoReqClass(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


}
