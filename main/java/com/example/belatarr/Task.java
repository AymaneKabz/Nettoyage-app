package com.example.belatarr;

public class Task {

    public Task(String comment, String name,String description, String done,String user,String date,String datefin,String synced) {
        this.comment = comment;
        this.name = name;
        this.done = done;
        this.user=user;
        this.description=description;
        this.date=date;
        this.datefin=datefin;
        this.synced=synced;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDone() {
        return done;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDone(String done) {
        this.done = done;
    }

    private int id;
    private String comment;
    private String name;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    private String done;
    private String user;
    private String description;
    private String date;
    private String synced;

    public String getSynced() {
        return synced;
    }

    public void setSynced(String synced) {
        this.synced = synced;
    }

    public String getDatefin() {
        return datefin;
    }

    public void setDatefin(String datefin) {
        this.datefin = datefin;
    }

    private String datefin;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
