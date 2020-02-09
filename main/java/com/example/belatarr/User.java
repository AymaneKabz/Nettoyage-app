package com.example.belatarr;

public class User {

    public User( int id,String username, String type,String team) {

        this.id = id;
        this.username = username;
        this.type = type;
        this.team=team;

    }

    public User( String username, String type) {


        this.username = username;
        this.type = type;
    }


    public int getId() {
        return id;
    }

    public String getIdString() {
        String x="";
        x+=id;
        return x;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private int id;
    private String username;
    private String password;
    private String type;
    private String image;

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    private String team;
}
