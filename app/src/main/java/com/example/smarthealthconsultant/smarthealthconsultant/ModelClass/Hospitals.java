package com.example.smarthealthconsultant.smarthealthconsultant.ModelClass;

public class Hospitals {
    public String hosname;
    public String hoscity;
    public String hosimage;

    public Hospitals() {
    }
    public Hospitals(String hosname,String hoscity,String hosimage){
        this.hosname=hosname;
        this.hoscity=hoscity;
        this.hosimage=hosimage;
    }
    public String getHosname() {
        return hosname;
    }

    public void setHosname(String hosname) {
        this.hosname = hosname;
    }

    public String getHoscity() {
        return hoscity;
    }

    public void setHoscity(String hoscity) {
        this.hoscity = hoscity;
    }

    public String getHosimage() {
        return hosimage;
    }

    public void setHosimage(String hosimage) {
        this.hosimage = hosimage;
    }
}
