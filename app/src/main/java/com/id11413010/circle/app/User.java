package com.id11413010.circle.app;

/**
 * Created by Calvin on 7/05/2014.
 */
public class User {
    private int id;
    private String firstName;
    private String lastName;
    private int circle;
    private String username;
    private String password;
    private String picture;

    public User() {

    }

    public String getName() {
        return firstName + " " + lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getCircle() {
        return circle;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getPicture() {
        return picture;
    }


}
