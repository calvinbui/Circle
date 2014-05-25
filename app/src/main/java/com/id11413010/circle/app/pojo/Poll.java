/*
 * Copyright (C) Trungthi (Calvin) Bui 2014
 */
package com.id11413010.circle.app.pojo;

/**
 * A plain old java object representing a Poll. Contains getter and setter methods for its various fields.
 */
public class Poll {
    /**
     * A String representing a Poll name
     */
    private String name;
    /**
     * A String representing a Poll circle
     */
    private String circle;

    private Integer id;

     /**
     * The constructor for the Question class. Creates a poll given its various fields.
     * @param name The value to set the poll's name to.
     * @param circle The value to set the poll's circle to.
     */
    public Poll(String name, String circle, Integer id) {
        this.name = name;
        this.circle = circle;
        this.id = id;
    }
    /**
     * Returns the value of the poll's name
     * @return An String containing the poll's name
     */
    public String getName() {
        return name;
    }
    /**
     * Set the value of the poll's name
     * @param name The value to set the poll's name to.
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * Returns the value of the poll's circle
     * @return An String containing the poll's circle
     */
    public String getCircle() {
        return circle;
    }
    /**
     * Set the value of the poll's circle
     * @param circle The value to set the poll's circle to.
     */
    public void setCircle(String circle) {
        this.circle = circle;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
