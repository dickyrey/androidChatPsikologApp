package com.dickyrey.konsulyuk.Model;

public class User {
    private String uid;
    private String name;
    private String image;
    private String status;
    private String search;

    public User() {
    }


    public User(String uid, String name, String image, String status, String search) {
        this.uid = uid;
        this.name = name;
        this.image = image;
        this.status = status;
        this.search = search;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }
}
