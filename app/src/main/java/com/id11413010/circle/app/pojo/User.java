package com.id11413010.circle.app.pojo;

/**
 * Created by Calvin on 7/05/2014.
 */
public class User {
    private int id;
    private String firstName;
    private String lastName;
    private String picture;

    public User(String firstName, String lastName) {
        super();
        this.firstName = firstName;
        this.lastName = lastName;
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

    public int getId() {
        return id;
    }

    public String getPicture() {
        return picture;
    }

}
