package com.id11413010.circle.app.pojo;

/**
 * Created by CalvinLaptop on 21/05/2014.
 */
public class Ballot {
    private String name;
    private String circle;

    public Ballot(String name, String circle) {
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
