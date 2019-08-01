package com.dickyrey.konsulyuk.Model;

public class User {
    private String uid;
    private String name;
    private String image;
    private String email;
    private String pendidikan;
    private String tempatpraktek;
    private String search;

    public User() {
    }

    public User(String uid, String name, String image, String email, String pendidikan, String tempatpraktek, String search) {
        this.uid = uid;
        this.name = name;
        this.image = image;
        this.email = email;
        this.pendidikan = pendidikan;
        this.tempatpraktek = tempatpraktek;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPendidikan() {
        return pendidikan;
    }

    public void setPendidikan(String pendidikan) {
        this.pendidikan = pendidikan;
    }

    public String getTempatpraktek() {
        return tempatpraktek;
    }

    public void setTempatpraktek(String tempatpraktek) {
        this.tempatpraktek = tempatpraktek;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }
}
