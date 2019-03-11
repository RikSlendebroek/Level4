package com.example.bucketlist;

public class Bucket {
    private long id;
    private String title;
    private String description;
    private boolean crossed;

    //region Constructors
    public Bucket(String title, String description) {
        this.title = title;
        this.description = description;
        this.crossed  =false;
    }
    //endregion

    //region Getters setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isCrossed() {
        return crossed;
    }

    public void setCrossed(boolean crossed) {
        this.crossed = crossed;
    }
    //endregion
}
