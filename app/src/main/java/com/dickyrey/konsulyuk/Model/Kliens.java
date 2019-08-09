package com.dickyrey.konsulyuk.Model;

public class Kliens {
    public String device_token, email, image, jenis_kelamin, kota, name, nomor_telepon, search, uid;

    public Kliens() {
    }

    public Kliens(String device_token, String email, String image, String jenis_kelamin, String kota, String name, String nomor_telepon, String search, String uid) {
        this.device_token = device_token;
        this.email = email;
        this.image = image;
        this.jenis_kelamin = jenis_kelamin;
        this.kota = kota;
        this.name = name;
        this.nomor_telepon = nomor_telepon;
        this.search = search;
        this.uid = uid;
    }

    public String getDevice_token() {
        return device_token;
    }

    public void setDevice_token(String device_token) {
        this.device_token = device_token;
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

    public String getJenis_kelamin() {
        return jenis_kelamin;
    }

    public void setJenis_kelamin(String jenis_kelamin) {
        this.jenis_kelamin = jenis_kelamin;
    }

    public String getKota() {
        return kota;
    }

    public void setKota(String kota) {
        this.kota = kota;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNomor_telepon() {
        return nomor_telepon;
    }

    public void setNomor_telepon(String nomor_telepon) {
        this.nomor_telepon = nomor_telepon;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
