package com.example.smarthealthconsultant.smarthealthconsultant.ModelClass;

public class UserHelperDoctor {

    String name,email,password,user,userid,imageUrl,category;


    public UserHelperDoctor(String name, String email, String password, String user, String userid,String image,String category) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.user = user;
        this.userid = userid;
        this.imageUrl=image;
        this.category=category;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getCategory() {
        return category;
    }
    public void setName(String name) {
        this.name = name;
    }

    public void setImage(String image){
        this.imageUrl=image;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }


    public void setCategory(String category) {
        this.category = category;
    }

    public String getUser() {
        return user;
    }

    public String getUserid() {
        return userid;
    }

    public String getImage(){
        return imageUrl;
    }
}
