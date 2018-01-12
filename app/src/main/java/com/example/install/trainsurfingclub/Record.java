package com.example.install.trainsurfingclub;

/**
 * Created by web on 2017-01-19.
 */

public class Record {

    private int id;
    private String title;
    private String description;
    private String url;


    public Record(String title, String description, String url){
        this.title = title;
        this.description = description;
        this.url = url;
    }

    public Record(int id, String title, String description, String url){
        this.id = id;
        this.title = title;
        this.description = description;
        this.url = url;
    }

    public Record(){

    }

    public String toString(){
        return getTitle();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}