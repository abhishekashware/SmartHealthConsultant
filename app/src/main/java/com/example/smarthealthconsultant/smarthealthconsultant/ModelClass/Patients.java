package com.example.smarthealthconsultant.smarthealthconsultant.ModelClass;

public class Patients {

    public String name;
    public String email;
    public String image;
    public String userid;
    public Patients(){

    }
    public Patients(String name, String email, String image,String userid) {
        this.name = name;
        this.email = email;
        this.image = image;
        this.userid=userid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}
