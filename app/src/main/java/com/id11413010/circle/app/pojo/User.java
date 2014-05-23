/*
 * Copyright (C) Trungthi (Calvin) Bui 2014
 */
package com.id11413010.circle.app.pojo;

/**
 * A plain old java object representing a User. Contains getter and setter methods for its various fields.
 */
public class User {
    /**
     * An integer representing the User ID
     */
    private int id;
    /**
     * A String representing the User's first name
     */
    private String firstName;
    /**
     * A String representing the User's last name
     */
    private String lastName;
    /**
     * A String representing the User's password
     */
    private String password;
    /**
     * A String representing the User's email
     */
    private String email;
    /**
     * A String representing the User's circle
     */
    private String circle;

    /**
     * The constructor for the User class. Creates a user given its various fields.
     * @param firstName The value to set the User's first name to.
     * @param lastName The value to set the User's last name to.
     * @param email The value to set the User's email to.
     * @param password The value to set the User's password to.
     * @param circle The value to set the User's circle to.
     */
    public User(String firstName, String lastName, String email, String password, String circle) {
        this.circle = circle;
        this.email = email;
        this.password = password;
        this.lastName = lastName;
        this.firstName = firstName;
    }

    /**
     * Returns the value of the User's circle
     * @return A String containing the user's circle
     */
    public String getCircle() {
        return circle;
    }

    /**
     * Set the value of the User's circle
     * @param circle The value to set the User's circle to.
     */
    public void setCircle(String circle) {
        this.circle = circle;
    }

    /**
     * Returns the value of the User's first name
     * @return A String containing the user's first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Returns the value of the User's last name
     * @return A String containing the user's last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Returns the value of the User's id
     * @return A String containing the user's id
     */
    public int getId() {
        return id;
    }

    /**
     * Set the value of the User's id
     * @param id The value to set the User's id to.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Set the value of the User's first name
     * @param firstName The value to set the User's first name to.
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Set the value of the User's last name
     * @param lastName name The value to set the User's last name to.
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Returns the value of the User's password
     * @return A String containing the user's password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Set the value of the User's password
     * @param password The value to set the User's password to.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Returns the value of the User's email
     * @return A String containing the user's email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Set the value of the User's email
     * @param email The value to set the User's email to.
     */
    public void setEmail(String email) {
        this.email = email;
    }
}
