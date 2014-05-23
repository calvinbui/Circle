/*
 * Copyright (C) Trungthi (Calvin) Bui 2014
 */
package com.id11413010.circle.app.pojo;
/**
 * A plain old java object representing an Event. Contains getter and setter methods for its various fields.
 */
public class Event {
    /**
     * An integer representing the Event ID
     */
    private int id;
    /**
     * A String representing the Event name
     */
    private String name;
    /**
     * A String representing the Event description
     */
    private String description;
    /**
     * A String representing the Event location
     */
    private String location;
    /**
     * A String representing the Event starting date
     */
    private String startDate;
    /**
     * A String representing the Event ending date
     */
    private String endDate;
    /**
     * A String representing the Event ending time
     */
    private String endTime;
    /**
     * A String representing the Event starting time
     */
    private String startTime;
    /**
     * A String representing the Event circle/group
     */
    private String circle;

    /**
     * The constructor for the Event class. Creates a event given its various fields.
     * @param name The value to set the event's name to.
     * @param description The value to set the event's description to.
     * @param location The value to set the event's location to.
     * @param startDate The value to set the event's start date to.
     * @param endDate The value to set the event's end date to.
     * @param endTime The value to set the event's end time to.
     * @param startTime The value to set the event's start time to.
     * @param circle The value to set the event's circle to.
     */
    public Event(String name, String description, String location, String startDate, String endDate, String endTime, String startTime, String circle) {
        this.name = name;
        this.description = description;
        this.location = location;
        this.startDate = startDate;
        this.endDate = endDate;
        this.endTime = endTime;
        this.startTime = startTime;
        this.circle = circle;
    }
    /**
     * Returns the value of the Event's id
     * @return An integer containing the event's id
     */
    public int getId() {
        return id;
    }
    /**
     * Returns the value of the Event's circle
     * @return A String containing the event's circle
     */
    public String getDescription() {
        return description;
    }
    /**
     * Set the value of the Event's description
     * @param description The value to set the event's description to.
     */
    public void setDescription(String description) {
        this.description = description;
    }
    /**
     * Set the value of the Event's id
     * @param id The value to set the event's id to.
     */
    public void setId(int id) {
        this.id = id;
    }
    /**
     * Returns the value of the Event's name
     * @return A String containing the event's name
     */
    public String getName() {
        return name;
    }
    /**
     * Set the value of the Event's name
     * @param name The value to set the event's name to.
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * Returns the value of the Event's location
     * @return A String containing the event's location
     */
    public String getLocation() {
        return location;
    }
    /**
     * Set the value of the Event's location
     * @param location The value to set the event's location to.
     */
    public void setLocation(String location) {
        this.location = location;
    }
    /**
     * Returns the value of the Event's start date
     * @return A String containing the event's start date
     */
    public String getStartDate() {
        return startDate;
    }
    /**
     * Set the value of the Event's start date
     * @param startDate The value to set the event's start date to.
     */
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
    /**
     * Returns the value of the Event's end date
     * @return A String containing the event's end date
     */
    public String getEndDate() {
        return endDate;
    }
    /**
     * Set the value of the Event's end date
     * @param endDate The value to set the event's end date to.
     */
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
    /**
     * Returns the value of the Event's end time
     * @return A String containing the event's end time
     */
    public String getEndTime() {
        return endTime;
    }
    /**
     * Set the value of the Event's end time
     * @param endTime The value to set the event's end time to.
     */
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
    /**
     * Returns the value of the Event's start time
     * @return A String containing the event's start time
     */
    public String getStartTime() {
        return startTime;
    }
    /**
     * Set the value of the Event's start time
     * @param startTime The value to set the event's start time to.
     */
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
    /**
     * Returns the value of the Event's circle
     * @return A String containing the event's circle
     */
    public String getCircle() {
        return circle;
    }
    /**
     * Set the value of the Event's circle
     * @param circle The value to set the event's circle to.
     */
    public void setCircle(String circle) {
        this.circle = circle;
    }
}
