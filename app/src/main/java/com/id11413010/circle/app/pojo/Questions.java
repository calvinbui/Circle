package com.id11413010.circle.app.pojo;

/**
 * Created by CalvinLaptop on 21/05/2014.
 */
public class Questions {
    private String name;
    private int votes;
    private int ballot;

    public Questions(String name, int votes, int ballot) {
        this.name = name;
        this.votes = votes;
        this.ballot =  ballot;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getVotes() {
        return votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }
}
