package com.id11413010.circle.app.pojo;

/**
 * A plain old java object representing a Leaderboard. Contains getter and setter methods for its various fields.
 */
public class Leaderboard {
    private Integer id;
    private int circle;
    private int creator;
    private String name;

    public Leaderboard(Integer id, int circle, int creator, String name) {
        this.id = id;
        this.circle = circle;
        this.creator = creator;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getCircle() {
        return circle;
    }

    public void setCircle(int circle) {
        this.circle = circle;
    }

    public int getCreator() {
        return creator;
    }

    public void setCreator(int creator) {
        this.creator = creator;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
