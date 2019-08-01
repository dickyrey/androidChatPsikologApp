package com.dickyrey.konsulyuk.Model;

public class Artikel {

    public String artikel_id,judul, date, desc, image_url, image_thumb, time, user_id;

    public Artikel() {
    }

    public Artikel(String artikel_id, String judul, String date, String desc, String image_url, String image_thumb, String time, String user_id) {
        this.artikel_id = artikel_id;
        this.judul = judul;
        this.date = date;
        this.desc = desc;
        this.image_url = image_url;
        this.image_thumb = image_thumb;
        this.time = time;
        this.user_id = user_id;
    }

    public String getArtikel_id() {
        return artikel_id;
    }

    public void setArtikel_id(String artikel_id) {
        this.artikel_id = artikel_id;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getImage_thumb() {
        return image_thumb;
    }

    public void setImage_thumb(String image_thumb) {
        this.image_thumb = image_thumb;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
