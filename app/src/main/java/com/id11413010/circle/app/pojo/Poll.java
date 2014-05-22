package com.id11413010.circle.app.pojo;

/**
 * TODO
 */
public class Poll {
    private String name;
    private String circle;

    public Poll(String name, String circle) {
        this.name = name;
        this.circle = circle;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCircle() {
        return circle;
    }

    public void setCircle(String circle) {
        this.circle = circle;
    }
}
