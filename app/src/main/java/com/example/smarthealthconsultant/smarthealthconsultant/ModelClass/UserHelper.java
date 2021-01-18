package com.example.smarthealthconsultant.smarthealthconsultant.ModelClass;

public class UserHelper {
    String name,email,password,user,userid,image;


    public UserHelper(String name, String email, String password, String user, String userid,String image) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.user = user;
        this.userid = userid;
        this.image=image;
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

    public void setName(String name) {
        this.name = name;
    }

    public void setImage(String image){
        this.image=image;
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

    public String getUser() {
        return user;
    }

    public String getUserid() {
        return userid;
    }

    public String getImage(){
        return image;
    }
}
