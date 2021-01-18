package com.example.smarthealthconsultant.smarthealthconsultant.chat;

/**
 * Created by arif on 01-Nov-17.
 */

public class Doctors {

    public String name;
    public String category;
    public String image;
    public String userid;
    public Doctors(){}

    public Doctors(String name,String category,String image,String doc_id){
        this.name = name;
        this.category = category;
        this.image = image;
        this.userid=doc_id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getImage() {
        return image;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setImage(String imageUrl) {
        this.image = imageUrl;
    }

    public String getDoc_id() {
        return userid;
    }

    public void setDoc_id(String doc_id) {
        this.userid = doc_id;
    }
//
//    public Boolean getOnline() {
//        return online;
//    }
//
//    public void setOnline(Boolean online) {
//        this.online = online;
//    }
//
//    public String getId() {
//        return id;
//    }

//    public void setId(String id) {
//        this.id = id;
//    }

}
